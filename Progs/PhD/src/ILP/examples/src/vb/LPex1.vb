' ---------------------------------------------------------------------------
' File: LPex1.vb
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
' LPex1.vb - Entering and optimizing an LP problem
'
' Demonstrates different methods for creating a problem.  The user has to
' choose the method on the command line:
'
'    LPex1  -r     generates the problem by adding constraints
'    LPex1  -c     generates the problem by adding variables
'    LPex1  -n     generates the problem by adding expressions
'
Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class LPex1

   Friend Shared Sub Usage()
      System.Console.WriteLine("usage:   LPex1 <option>")
      System.Console.WriteLine("options:       -r   build model row by row")
      System.Console.WriteLine("options:       -c   build model column by column")
      System.Console.WriteLine("options:       -n   build model nonzero by nonzero")
   End Sub 'Usage


   Public Overloads Shared Sub Main(ByVal args() As String)
      If args.Length <> 1 OrElse args(0).ToCharArray()(0) <> "-"c Then
         Usage()
         Return
      End If

      Try
         ' Create the modeler/solver object
         Dim cplex As New Cplex()
         Dim var(1)() As INumVar
         Dim rng(1)() As IRange

         ' Evaluate command line option and call appropriate populate method.
         ' The created ranges and variables are returned as element 0 of arrays
         ' var and rng.
         Select Case args(0).ToCharArray()(1)
            Case "r"c
               PopulateByRow(cplex, var, rng)
            Case "c"c
                PopulateByColumn(cplex, var, rng)
            Case "n"c
                PopulateByNonzero(cplex, var, rng)
            Case Else
                Usage()
                Return
         End Select

         ' write model to file
         cplex.ExportModel("lpex1.lp")

         ' solve the model and display the solution if one was found
         If cplex.Solve() Then
            Dim x As Double() = cplex.GetValues(var(0))
            Dim dj As Double() = cplex.GetReducedCosts(var(0))
            Dim pi As Double() = cplex.GetDuals(rng(0))
            Dim slack As Double() = cplex.GetSlacks(rng(0))

            cplex.Output().WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            cplex.Output().WriteLine(("Solution value  = " + cplex.ObjValue.ToString))

            Dim nvars As Integer = x.Length
            Dim j As Integer

            For j = 0 To nvars - 1
               cplex.Output().WriteLine(("Variable   " & j & ": Value = " & x(j) & " Reduced cost = " & dj(j)))
            Next j

            Dim ncons As Integer = slack.Length
            Dim i As Integer

            For i = 0 To ncons - 1
               cplex.Output().WriteLine(("Constraint " & i & ": Slack = " & slack(i) & " Pi = " & pi(i)))
            Next i
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + e.ToString + "' caught"))
      End Try
   End Sub 'Main



   ' The following methods all populate the problem with data for the following
   ' linear program:
   '
   '    Maximize
   '     x1 + 2 x2 + 3 x3
   '    Subject To
   '     - x1 + x2 + x3 <= 20
   '     x1 - 3 x2 + x3 <= 30
   '    Bounds
   '     0 <= x1 <= 40
   '    End
   '
   ' using the IMPModeler API
   Friend Shared Sub PopulateByRow(ByVal model As IMPModeler, ByVal var()() As INumVar, ByVal rng()() As IRange)
      Dim lb As Double() = {0.0, 0.0, 0.0}
      Dim ub As Double() = {40.0, System.Double.MaxValue, System.Double.MaxValue}
        Dim varname As String() = {"x1", "x2", "x3"}
      Dim x As INumVar() = model.NumVarArray(3, lb, ub, varname)
      var(0) = x

      Dim objvals As Double() = {1.0, 2.0, 3.0}
      model.AddMaximize(model.ScalProd(x, objvals))

      rng(0) = New IRange(1) {}
      rng(0)(0) = model.AddLe(model.Sum(model.Prod(-1.0, x(0)), model.Prod(1.0, x(1)), model.Prod(1.0, x(2))), 20.0, "c1")
      rng(0)(1) = model.AddLe(model.Sum(model.Prod(1.0, x(0)), model.Prod(-3.0, x(1)), model.Prod(1.0, x(2))), 30.0, "c2")
   End Sub 'PopulateByRow


   Friend Shared Sub PopulateByColumn(ByVal model As IMPModeler, ByVal var()() As INumVar, ByVal rng()() As IRange)
      Dim obj As IObjective = model.AddMaximize()

      rng(0) = New IRange(1) {}
      rng(0)(0) = model.AddRange(-System.Double.MaxValue, 20.0, "c1")
      rng(0)(1) = model.AddRange(-System.Double.MaxValue, 30.0, "c2")

      Dim r0 As IRange = rng(0)(0)
      Dim r1 As IRange = rng(0)(1)

      var(0) = New INumVar(2) {}
      var(0)(0) = model.NumVar(model.Column(obj, 1.0).And(model.Column(r0, -1.0).And(model.Column(r1, 1.0))), 0.0, 40.0, "x1")
      var(0)(1) = model.NumVar(model.Column(obj, 2.0).And(model.Column(r0, 1.0).And(model.Column(r1, -3.0))), 0.0, System.Double.MaxValue, "x2")
      var(0)(2) = model.NumVar(model.Column(obj, 3.0).And(model.Column(r0, 1.0).And(model.Column(r1, 1.0))), 0.0, System.Double.MaxValue, "x3")
   End Sub 'PopulateByColumn


   Friend Shared Sub PopulateByNonzero(ByVal model As IMPModeler, ByVal var()() As INumVar, ByVal rng()() As IRange)
      Dim lb As Double() = {0.0, 0.0, 0.0}
      Dim ub As Double() = {40.0, System.Double.MaxValue, System.Double.MaxValue}
      Dim x As INumVar() = model.NumVarArray(3, lb, ub)
      var(0) = x

      Dim objvals As Double() = {1.0, 2.0, 3.0}
      model.Add(model.Maximize(model.ScalProd(x, objvals)))

      rng(0) = New IRange(1) {}
      rng(0)(0) = model.AddRange(-System.Double.MaxValue, 20.0)
      rng(0)(1) = model.AddRange(-System.Double.MaxValue, 30.0)

      rng(0)(0).Expr = model.Sum(model.Prod(-1.0, x(0)), model.Prod(1.0, x(1)), model.Prod(1.0, x(2)))
      rng(0)(1).Expr = model.Sum(model.Prod(1.0, x(0)), model.Prod(-3.0, x(1)), model.Prod(1.0, x(2)))

      x(0).Name = "x1"
      x(1).Name = "x2"
      x(2).Name = "x3"

      rng(0)(0).Name = "c1"
      rng(0)(1).Name = "c2"

   End Sub 'PopulateByNonzero
End Class 'LPex1
