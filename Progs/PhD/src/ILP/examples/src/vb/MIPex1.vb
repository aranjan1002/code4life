' ---------------------------------------------------------------------------
' File: MIPex1.vb
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
' MIPex1.vb - Entering and optimizing a MIP problem
Imports ILOG.Concert
Imports ILOG.CPLEX



Public Class MIPex1

   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim cplex As New Cplex()

         Dim var(1)() As INumVar
         Dim rng(1)() As IRange

         PopulateByRow(cplex, var, rng)

         If cplex.Solve() Then
            Dim x As Double() = cplex.GetValues(var(0))
            Dim slack As Double() = cplex.GetSlacks(rng(0))

            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " & cplex.ObjValue))

            Dim j As Integer

            For j = 0 To x.Length - 1
               System.Console.WriteLine(("Variable   " & j & ": Value = " & x(j)))
            Next j

            Dim i As Integer

            For i = 0 To slack.Length - 1
               System.Console.WriteLine(("Constraint " & i & ": Slack = " & slack(i)))
            Next i
         End If

         cplex.ExportModel("mipex1.lp")
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught '" + e.ToString + "' caught"))
      End Try
   End Sub 'Main


   Friend Shared Sub PopulateByRow(ByVal model As IMPModeler, ByVal var()() As INumVar, ByVal rng()() As IRange)
      '  First define the variables, three continuous and one integer
      Dim xlb As Double() = {0.0, 0.0, 0.0, 2.0}
      Dim xub As Double() = {40.0, System.Double.MaxValue, System.Double.MaxValue, 3.0}
      Dim xt As NumVarType() = {NumVarType.Float, NumVarType.Float, NumVarType.Float, NumVarType.Int}
      Dim x As INumVar() = model.NumVarArray(4, xlb, xub, xt)
      var(0) = x

      ' Objective Function:  maximize x0 + 2*x1 + 3*x2 + x3
      Dim objvals As Double() = {1.0, 2.0, 3.0, 1.0}
      model.AddMaximize(model.ScalProd(x, objvals))

      ' Three constraints
      rng(0) = New IRange(2) {}
      ' - x0 + x1 + x2 + 10*x3 <= 20
      rng(0)(0) = model.AddLe(model.Sum(model.Prod(-1.0, x(0)), model.Prod(1.0, x(1)), model.Prod(1.0, x(2)), model.Prod(10.0, x(3))), 20.0)
      ' x0 - 3*x1 + x2 <= 30
      rng(0)(1) = model.AddLe(model.Sum(model.Prod(1.0, x(0)), model.Prod(-3.0, x(1)), model.Prod(1.0, x(2))), 30.0)
      ' x1 - 3.5*x3 = 0
      rng(0)(2) = model.AddEq(model.Sum(model.Prod(1.0, x(1)), model.Prod(-3.5, x(3))), 0.0)
   End Sub 'PopulateByRow
End Class 'MIPex1
