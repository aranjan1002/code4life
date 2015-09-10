' ---------------------------------------------------------------------------
' File: InOut1.vb
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
' Minimize the total cost so that the company exactly meets the
' demand.
'
' --------------------------------------------------------------------------
Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class InOut1
   Friend Shared _nbProds As Integer = 3
   Friend Shared _nbResources As Integer = 2
   Friend Shared _consumption As Double()() =  {New Double() {0.5, 0.4, 0.3}, New Double() {0.2, 0.4, 0.6}}
   Friend Shared _demand As Double() =  {100.0, 200.0, 300.0}
   Friend Shared _capacity As Double() =  {20.0, 40.0}
   Friend Shared _insideCost As Double() =  {0.6, 0.8, 0.3}
   Friend Shared _outsideCost As Double() =  {0.8, 0.9, 0.4}
   
   
   Friend Shared Sub DisplayResults(cplex As Cplex, inside() As INumVar, outside() As INumVar)
      System.Console.WriteLine(("cost: " & cplex.ObjValue))
      
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

         Dim inside(_nbProds - 1) As INumVar
         Dim outside(_nbProds - 1) As INumVar

         Dim obj As IObjective = cplex.AddMinimize()

         ' Must meet demand for each product
         Dim p As Integer
         For p = 0 To _nbProds - 1
            Dim demRange As IRange = cplex.AddRange(_demand(p), _demand(p))
            inside(p) = cplex.NumVar(cplex.Column(obj, _insideCost(p)).And(cplex.Column(demRange, 1.0)), 0.0, System.Double.MaxValue)

            outside(p) = cplex.NumVar(cplex.Column(obj, _outsideCost(p)).And(cplex.Column(demRange, 1.0)), 0.0, System.Double.MaxValue)
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

         DisplayResults(cplex, inside, outside)
         System.Console.WriteLine("----------------------------------------")
         cplex.End()
      Catch exc As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + exc.ToString + "' caught"))
      End Try
   End Sub 'Main
End Class 'InOut1

'
'cost: 372
'P0
'inside:  40
'outside: 60
'P1
'inside:  0
'outside: 200
'P2
'inside:  0
'outside: 300
'
