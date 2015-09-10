' ---------------------------------------------------------------------------
' File: InOut3.vb
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
' A company has to produce 3 products, using 2 resources.
' Each resource has a limited capacity.
' Each product consumes a given number of machines.
' Each product has a production cost (the inside cost).
' Both products can also be brought outside the company at a given 
' cost (the outside cost)
' 
' Minimize external production given product demand, a cost
' constraint, and minimum internal production constraints.
'
' --------------------------------------------------------------------------
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System


Public Class InOut3
   Private Shared _nbProds As Integer = 3
   Private Shared _nbResources As Integer = 2
   Private Shared _consumption As Double()() =  {New Double() {0.5, 0.4, 0.3}, New Double() {0.2, 0.4, 0.6}}
   Private Shared _demand As Double() =  {100.0, 200.0, 300.0}
   Private Shared _capacity As Double() =  {20.0, 40.0}
   Private Shared _insideCost As Double() =  {0.6, 0.8, 0.3}
   Private Shared _outsideCost As Double() =  {0.8, 0.9, 0.4}
   
   
   Shared Sub DisplayResults(cplex As Cplex, costVar As INumVar, inside() As INumVar, outside() As INumVar)
      System.Console.WriteLine(("cost: " & cplex.GetValue(costVar)))
      
      Dim p As Integer
      For p = 0 To _nbProds - 1
         System.Console.WriteLine(("P" & p))
         System.Console.WriteLine(("inside:  " & cplex.GetValue(inside(p))))
         System.Console.WriteLine(("outside: " & cplex.GetValue(outside(p))))
      Next p
   End Sub 'DisplayResults
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim cplex As New Cplex()

         Dim inside As INumVar() = cplex.NumVarArray(_nbProds, 10.0, [Double].MaxValue)
         Dim outside As INumVar() = cplex.NumVarArray(_nbProds, 0.0, [Double].MaxValue)
         Dim costVar As INumVar = cplex.NumVar(0.0, [Double].MaxValue)

         cplex.AddEq(costVar, cplex.Sum(cplex.ScalProd(inside, _insideCost), cplex.ScalProd(outside, _outsideCost)))

         Dim obj As IObjective = cplex.AddMinimize(costVar)

         ' Must meet demand for each product
         Dim p As Integer
         For p = 0 To _nbProds - 1
            cplex.AddEq(cplex.Sum(inside(p), outside(p)), _demand(p))
         Next p
         ' Must respect capacity constraint for each resource
         Dim r As Integer
         For r = 0 To _nbResources - 1
            cplex.AddLe(cplex.ScalProd(_consumption(r), inside), _capacity(r))
         Next r
         cplex.Solve()

         If Not cplex.GetStatus().Equals(cplex.Status.Optimal) Then
            System.Console.WriteLine("No optimal solution found")
            Return
         End If

         ' New constraint: cost must be no more than 10% over minimum
         Dim cost As Double = cplex.ObjValue
         costVar.UB = 1.1 * cost

         ' New objective: minimize outside production
         obj.Expr = cplex.Sum(outside)

         cplex.Solve()

         DisplayResults(cplex, costVar, inside, outside)
         System.Console.WriteLine("----------------------------------------")
         cplex.End()
      Catch exc As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + exc.ToString + "' caught"))
      End Try
   End Sub 'Main
End Class 'InOut3

'
'cost: 373.333
'P0
'inside:  10
'outside: 90
'P1
'inside:  10
'outside: 190
'P2
'inside:  36.6667
'outside: 263.333
'----------------------------------------
'
