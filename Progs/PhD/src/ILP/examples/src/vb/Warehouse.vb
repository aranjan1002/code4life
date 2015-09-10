' ---------------------------------------------------------------------------
' File: Warehouse.vb
' --------------------------------------------------------------------------
' Version 12.2  
' Copyright IBM Corporation 1999, 2010. All Rights Reserved.
' 
' US Government Users Restricted Rights - Use, duplication or
' disclosure restricted by GSA ADP Schedule Contract with
' IBM Corp.
' ---------------------------------------------------------------------------
' Licensed Materials - Property of IBM
' 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
'
' warehouse.vb - Example that uses the goal API to enforce constraints
'                dynamically, depending on the relaxation solution at
'                each MIP node.
'
'                Given a set of warehouses that each have a
'                capacity, a cost per unit stored, and a 
'                minimum usage level, this example find an
'                assignment of items to warehouses that minimizes
'                total cost.  The minimum usage levels are
'                enforced dynamically using the goal API.
Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class Warehouse

   Friend Class SemiContGoal
      Inherits Cplex.Goal
      Friend _scVars() As INumVar
      Friend _scLbs() As Double
      
      
      Friend Sub New(scVars() As INumVar, scLbs() As Double)
         _scVars = scVars
         _scLbs = scLbs
      End Sub 'New
      
      
      Public Overrides Function Execute(cplex As Cplex) As Cplex.Goal
         Dim besti As Integer = - 1
         Dim maxObjCoef As Double = System.Double.MinValue
         
         ' From among all variables that do not respect their minimum 
         ' usage levels, select the one with maximum objective coefficient.
         Dim i As Integer
         For i = 0 To _scVars.Length - 1
         Dim val As Double = GetValue(_scVars(i))
         If val >= 1E-05 AndAlso val <= _scLbs(i) - 1E-05 Then
            If GetObjCoef(_scVars(i)) >= maxObjCoef Then
               besti = i
               maxObjCoef = GetObjCoef(_scVars(i))
            End If
         End If
         Next i
         
         '  If any are found, branch to enforce the condition that
         '  the variable must either be 0.0 or greater than
         '  the minimum usage level.
         If besti <> - 1 Then
         Return cplex.And(cplex.Or(cplex.LeGoal(_scVars(besti), 0.0), cplex.GeGoal(_scVars(besti), _scLbs(besti))), Me)
         Else
         If Not IsIntegerFeasible() Then
            Return cplex.And(cplex.BranchAsCplex(), Me)
         End If
         End If 
         Return Nothing
      End Function 'Execute
   End Class 'SemiContGoal
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim cplex As New Cplex()

         Dim nbWhouses As Integer = 4
         Dim nbLoads As Integer = 31

         Dim capVars As INumVar() = cplex.NumVarArray(nbWhouses, 0, 10, NumVarType.Int)
         ' Used capacities
         Dim capLbs As Double() = {2, 3, 5, 7}  ' Minimum usage level
         Dim costs As Double() = {1, 2, 4, 6}  ' Cost per warehouse
         ' These variables represent the assigninment of a
         ' load to a warehouse.
         Dim assignVars(nbWhouses)() As INumVar
         Dim w As Integer
         For w = 0 To nbWhouses - 1
            assignVars(w) = cplex.NumVarArray(nbLoads, 0, 1, NumVarType.Int)

            ' Links the number of loads assigned to a warehouse with 
            ' the capacity variable of the warehouse.
            cplex.AddEq(cplex.Sum(assignVars(w)), capVars(w))
         Next w

         ' Each load must be assigned to just one warehouse.
         Dim l As Integer
         For l = 0 To nbLoads - 1
            Dim aux(nbWhouses - 1) As INumVar
            For w = 0 To nbWhouses - 1
               aux(w) = assignVars(w)(l)
            Next w
            cplex.AddEq(cplex.Sum(aux), 1)
         Next l

         cplex.AddMinimize(cplex.ScalProd(costs, capVars))
	 cplex.SetParam(cplex.IntParam.MIPSearch, cplex.MIPSearch.Traditional)

         If cplex.Solve(New SemiContGoal(capVars, capLbs)) Then
            System.Console.WriteLine("--------------------------------------------")
            System.Console.WriteLine()
            System.Console.WriteLine("Solution found:")
            System.Console.WriteLine((" Objective value = " & cplex.ObjValue))
            System.Console.WriteLine()
            For w = 0 To nbWhouses - 1
               System.Console.WriteLine(("Warehouse " & w & ": stored " & cplex.GetValue(capVars(w)) & " loads"))
               For l = 0 To nbLoads - 1
                  If cplex.GetValue(assignVars(w)(l)) > 0.00001 Then
                      System.Console.Write(("Load " & l & " | "))
                  End If
               Next l
               System.Console.WriteLine()
               System.Console.WriteLine()
            Next w
            System.Console.WriteLine("--------------------------------------------")
         Else
            System.Console.WriteLine(" No solution found ")
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      End Try
   End Sub 'Main
End Class 'Warehouse
