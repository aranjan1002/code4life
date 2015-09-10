' ---------------------------------------------------------------------------
' File: MIQPex1.vb
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
' MIQPex1.vb - Entering and optimizing a MIQP problem
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System



Public Class MIQPex1

   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim cplex As New Cplex()
         Dim lp As ILPMatrix = PopulateByRow(cplex)

         cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Dual)

         If cplex.Solve() Then
            Dim x As Double() = cplex.GetValues(lp)
            Dim slack As Double() = cplex.GetSlacks(lp)

            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " & cplex.ObjValue))

            Dim nvars As Integer = x.Length
            Dim j As Integer

            For j = 0 To nvars - 1
               System.Console.WriteLine(("Variable   " & j & ": Value = " & x(j)))
            Next j

            Dim ncons As Integer = slack.Length
            Dim i As Integer

            For i = 0 To ncons - 1
               System.Console.WriteLine(("Constraint " & i & ": Slack = " & slack(i)))
            Next i

            cplex.ExportModel("miqpex1.lp")
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + e.ToString + "' caught"))
      End Try
   End Sub 'Main


   Shared Function PopulateByRow(ByVal model As IMPModeler) As ILPMatrix
      Dim lp As ILPMatrix = model.AddLPMatrix()

      Dim lb As Double() = {0.0, 0.0, 0.0}
      Dim ub As Double() = {40.0, [Double].MaxValue, [Double].MaxValue}
      Dim x As INumVar() = model.NumVarArray(model.ColumnArray(lp, 3), lb, ub)
      Dim y As INumVar = model.IntVar(model.Column(lp), 0, 3)

      ' - x0 +   x1 + x2 + 10*y <= 20
      '   x0 - 3*x1 + x2         <= 30
      Dim lhs As Double() = {-[Double].MaxValue, -[Double].MaxValue}
      Dim rhs As Double() = {20.0, 30.0}
      Dim val As Double()() = {New Double() {-1.0, 1.0, 1.0, 10.0}, New Double() {1.0, -3.0, 1.0}}
      Dim ind As Integer()() = {New Integer() {0, 1, 2, 3}, New Integer() {0, 1, 2}}
      lp.AddRows(lhs, rhs, ind, val)
      ' x1 + 3.5*y = 0
      lp.AddRow(model.Eq(model.Diff(x(1), model.Prod(3.5, y)), 0.0))

      ' Q = 0.5 ( 33*x0*x0 + 22*x1*x1 + 11*x2*x2 - 12*x0*x1 - 23*x1*x2 )
      Dim x00 As INumExpr = model.Prod(33.0, model.Square(x(0)))
      Dim x11 As INumExpr = model.Prod(22.0, model.Square(x(1)))
      Dim x22 As INumExpr = model.Prod(11.0, model.Square(x(2)))
      Dim x01 As INumExpr = model.Prod(-12.0, model.Prod(x(0), x(1)))
      Dim x12 As INumExpr = model.Prod(-23.0, model.Prod(x(1), x(2)))
      Dim Q As INumExpr = model.Prod(0.5, model.Sum(x00, x11, x22, x01, x12))

      ' maximize x0 + 2*x1 + 3*x2 + Q
      Dim objvals As Double() = {1.0, 2.0, 3.0}
      model.Add(model.Maximize(model.Diff(model.ScalProd(x, objvals), Q)))

      Return lp
   End Function 'PopulateByRow
End Class 'MIQPex1
