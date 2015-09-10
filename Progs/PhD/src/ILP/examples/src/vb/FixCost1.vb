' ---------------------------------------------------------------------------
' File: FixCost1.vb
' Version 12.2  
' ---------------------------------------------------------------------------
' Licensed Materials - Property of IBM
' 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
' Copyright IBM Corporation 2001, 2010. All Rights Reserved.
' 
' US Government Users Restricted Rights - Use, duplication or
' disclosure restricted by GSA ADP Schedule Contract with
' IBM Corp.
' ---------------------------------------------------------------------------
' 
' Problem Description
' -------------------
' 
' A company must produce a product on a set of machines.
' Each machine has limited capacity.
' Producing a product on a machine has both a fixed cost
' and a cost per unit of production.
' 
' Minimize the sum of fixed and variable costs so that the
' company exactly meets demand.
' 
' --------------------------------------------------------------------------

Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class FixCost1
   Friend Shared _nbMachines As Integer  = 6
   Friend Shared _cost       As Double() = {15.0, 20.0, 45.0, 64.0, 12.0, 56.0}
   Friend Shared _capacity   As Double() = {100.0, 20.0, 405.0, 264.0, 12.0, 256.0}
   Friend Shared _fixedCost  As Double() = {1900.0, 820.0, 805.0, 464.0, 3912.0, 556.0}
   Friend Shared _demand     As Double   = 22.0
   

    Public Overloads Shared Sub Main(ByVal args() As String)
        Try
         Dim cplex As New Cplex()

         Dim fused As INumVar() = cplex.BoolVarArray(_nbMachines)
         Dim x As INumVar() = cplex.NumVarArray(_nbMachines, 0.0, System.Double.MaxValue)

         ' Objective: minimize the sum of fixed and variable costs
         cplex.AddMinimize(cplex.Sum(cplex.ScalProd(_cost, x), cplex.ScalProd(fused, _fixedCost)))

         Dim i As Integer
         For i = 0 To _nbMachines - 1
            ' Constraint: respect capacity constraint on machine 'i'
            cplex.AddLe(x(i), _capacity(i))

            ' Constraint: only produce product on machine 'i' if it is 'used'
            '             (to capture fixed cost of using machine 'i')
            cplex.AddLe(x(i), cplex.Prod(10000, fused(i)))
         Next i

         ' Constraint: meet demand
         cplex.AddEq(cplex.Sum(x), _demand)

         If cplex.Solve() Then
            System.Console.WriteLine(("Obj " & cplex.ObjValue))
            Dim eps As Double = cplex.GetParam(cplex.DoubleParam.EpInt)
            For i = 0 To _nbMachines - 1
               If cplex.GetValue(fused(i)) > eps Then
                   System.Console.WriteLine(("E" & i & " is used for " & cplex.GetValue(x(i))))
               End If
            Next i
            System.Console.WriteLine()
            System.Console.WriteLine("----------------------------------------")
         End If
         cplex.End()
      Catch exc As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + exc.ToString + "' caught"))
      End Try
   End Sub 'Main
End Class 'FixCost1

' Solution
'Obj 1788
'E5 is used for 22
'
