' ---------------------------------------------------------------------------
' File: LPex6.vb
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
' LPex6.vb - Illustrates that optimal basis can be copied and
'            used to start an optimization.
'
Imports ILOG.Concert
Imports ILOG.CPLEX



Public Class LPex6
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim cplex As New Cplex()

         Dim var(1)() As INumVar
         Dim rng(1)() As IRange

         PopulateByRow(cplex, var, rng)

         Dim cstat As Cplex.BasisStatus() = {cplex.BasisStatus.AtUpper, cplex.BasisStatus.Basic, cplex.BasisStatus.Basic}
         Dim rstat As Cplex.BasisStatus() = {cplex.BasisStatus.AtLower, cplex.BasisStatus.AtLower}
         cplex.SetBasisStatuses(var(0), cstat, rng(0), rstat)

         If cplex.Solve() Then
            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " & cplex.ObjValue))
            System.Console.WriteLine(("Iteration count = " & cplex.Niterations))

            Dim x As Double() = cplex.GetValues(var(0))
            Dim dj As Double() = cplex.GetReducedCosts(var(0))
            Dim pi As Double() = cplex.GetDuals(rng(0))
            Dim slack As Double() = cplex.GetSlacks(rng(0))

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
      Catch exc As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + exc.ToString + "' caught"))
      End Try
   End Sub 'Main


   Friend Shared Sub PopulateByRow(ByVal model As IMPModeler, ByVal var()() As INumVar, ByVal rng()() As IRange)
      Dim lb As Double() = {0.0, 0.0, 0.0}
      Dim ub As Double() = {40.0, System.Double.MaxValue, System.Double.MaxValue}
      var(0) = model.NumVarArray(3, lb, ub)

      Dim objvals As Double() = {1.0, 2.0, 3.0}
      model.AddMaximize(model.ScalProd(var(0), objvals))

      rng(0) = New IRange(1) {}
      rng(0)(0) = model.AddLe(model.Sum(model.Prod(-1.0, var(0)(0)), model.Prod(1.0, var(0)(1)), model.Prod(1.0, var(0)(2))), 20.0)
      rng(0)(1) = model.AddLe(model.Sum(model.Prod(1.0, var(0)(0)), model.Prod(-3.0, var(0)(1)), model.Prod(1.0, var(0)(2))), 30.0)
   End Sub 'PopulateByRow
End Class 'LPex6
