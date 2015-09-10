' ---------------------------------------------------------------------------
' File: QCPex1.vb
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
' QCPex1.vb - Entering and optimizing a quadratically constrained problem
Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class QCPex1

   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim cplex As New Cplex()
         Dim row(2) As IRange
         Dim var As INumVar() = populateByRow(cplex, row)

         If cplex.Solve() Then
            Dim x As Double() = cplex.GetValues(var)
            Dim slack As Double() = cplex.GetSlacks(row)

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
            cplex.ExportModel("qcpex1.lp")
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + e.ToString + "' caught"))
      End Try
   End Sub 'Main


   Friend Shared Function populateByRow(ByVal model As IMPModeler, ByVal row() As IRange) As INumVar()
      Dim lb As Double() = {0.0, 0.0, 0.0}
      Dim ub As Double() = {40.0, System.Double.MaxValue, System.Double.MaxValue}
      Dim x As INumVar() = model.NumVarArray(3, lb, ub)

      '- x0 +   x1 + x2 <= 20
      '  x0 - 3*x1 + x2 <= 30
      Dim val As Double()() = {New Double() {-1.0, 1.0, 1.0}, New Double() {1.0, -3.0, 1.0}}
      row(0) = model.AddLe(model.ScalProd(val(0), x), 20.0)
      row(1) = model.AddLe(model.ScalProd(val(1), x), 30.0)

      ' x0*x0 + x1*x1 + x2*x2 <= 1.0
      row(2) = model.AddLe(model.Sum(model.Prod(x(0), x(0)), model.Prod(x(1), x(1)), model.Prod(x(2), x(2))), 1.0)

      ' Q = 0.5 ( 33*x0*x0 + 22*x1*x1 + 11*x2*x2 - 12*x0*x1 - 23*x1*x2 )
      Dim x00 As INumExpr = model.Prod(33.0, x(0), x(0))
      Dim x11 As INumExpr = model.Prod(22.0, x(1), x(1))
      Dim x22 As INumExpr = model.Prod(11.0, x(2), x(2))
      Dim x01 As INumExpr = model.Prod(-12.0, x(0), x(1))
      Dim x12 As INumExpr = model.Prod(-23.0, x(1), x(2))
      Dim Q As INumExpr = model.Prod(0.5, model.Sum(x00, x11, x22, x01, x12))

      'maximize x0 + 2*x1 + 3*x2 + Q
      Dim objvals As Double() = {1.0, 2.0, 3.0}
      model.Add(model.Maximize(model.Diff(model.ScalProd(x, objvals), Q)))

      Return x
   End Function 'populateByRow
End Class 'QCPex1
