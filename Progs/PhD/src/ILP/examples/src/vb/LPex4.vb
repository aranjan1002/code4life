' ---------------------------------------------------------------------------
' File: LPex4.vb
' Version 12.2  
' ---------------------------------------------------------------------------
' Licensed Materials - Property of IBM
' 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
' Copyright IBM Corporation 1999, 2010. All Rights Reserved.
' 
' US Government Users Restricted Rights - Use, duplication or
' disclosure restricted by GSA ADP Schedule Contract with
' IBM Corp.
' ---------------------------------------------------------------------------
'
' LPex4.vb - Illustrating the CPLEX callback functionality.
' 
' This is a modification of LPex1.vb, where we use a callback
' function to print the iteration info, rather than have CPLEX
' do it.   Note that the actual LP that is solved is slightly
' different to make the output more interesting.
Imports ILOG.Concert
Imports ILOG.CPLEX

 _


Public Class LPex4
    _
   ' Implement the callback as an extension of class
   ' Cplex.ContinuousCallback by overloading method main().  In the
   ' implementation use protected methods of class Cplex.ContinuousCallback
   ' and its super classes, such as Niterations, isFeasible(),
   ' ObjValue, and Infeasibility used in this example.
   Friend Class MyCallback
      Inherits Cplex.ContinuousCallback
      
      Public Overrides Sub Main()
         System.Console.Write(("Iteration " & Niterations & ": "))
         If IsFeasible() Then
            System.Console.WriteLine(("Objective = " & ObjValue))
         Else
            System.Console.WriteLine(("Infeasibility measure = " & Infeasibility))
         End If
      End Sub 'Main
   End Class 'MyCallback
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim cplex As New Cplex()
         Dim lp As ILPMatrix = PopulateByRow(cplex)

         ' turn off presolve to prevent it from completely solving the model
         ' before entering the actual LP optimizer
         cplex.SetParam(cplex.BooleanParam.PreInd, False)

         ' turn off logging
         cplex.SetOut(Nothing)

         ' create and instruct cplex to use callback
         cplex.Use(New MyCallback())

         If cplex.Solve() Then
            Dim x As Double() = cplex.GetValues(lp)
            Dim dj As Double() = cplex.GetReducedCosts(lp)
            Dim pi As Double() = cplex.GetDuals(lp)
            Dim slack As Double() = cplex.GetSlacks(lp)

            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Iterations      = " & cplex.Niterations))
            System.Console.WriteLine(("Solution value  = " & cplex.ObjValue))

            Dim nvars As Integer = x.Length 
            Dim j As Integer

            For j = 0 To nvars - 1
               System.Console.WriteLine(("Variable   " & j & ": Value = " & x(j) & _
                                         " Reduced cost = " & dj(j)))
            Next j

            Dim ncons As Integer = slack.Length 
            Dim i As Integer

            For i = 0 To ncons - 1
               System.Console.WriteLine(("Constraint " & i & ": Slack = " & slack(i) & _
                                         " Pi = " & pi(i)))
            Next i
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + e.ToString + "' caught"))
      End Try
   End Sub 'Main


   Friend Shared Function PopulateByRow(ByVal model As IMPModeler) As ILPMatrix
      Dim lp As ILPMatrix = model.AddLPMatrix()

      Dim lb As Double() = {0.0, 0.0, 0.0}
      Dim ub As Double() = {40.0, System.Double.MaxValue, System.Double.MaxValue}
      Dim x As INumVar() = model.NumVarArray(model.ColumnArray(lp, 3), lb, ub)

      Dim lhs As Double() = {-System.Double.MaxValue, -System.Double.MaxValue}
      Dim rhs As Double() = {20.0, 30.0}
      Dim val As Double()() = {New Double() {-1.0, 1.0, 1.0}, New Double() {1.0, -3.0, 1.0}}
      Dim ind As Integer()() = {New Integer() {0, 1, 2}, New Integer() {0, 1, 2}}
      lp.AddRows(lhs, rhs, ind, val)

      Dim objvals As Double() = {1.0, 2.0, 3.0}
      model.AddMaximize(model.ScalProd(x, objvals))

      Return lp
   End Function 'PopulateByRow
End Class 'LPex4
