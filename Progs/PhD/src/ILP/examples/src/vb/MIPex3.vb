' ---------------------------------------------------------------------------
' File: MIPex3.vb
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
' MIPex3.vb - Entering and optimizing a MIP problem with SOS sets
'             and priority orders.  It is a modification of MIPex1.vb.
'             Note that the problem solved is slightly different than
'             MIPex1.vb so that the output is interesting.

Imports ILOG.Concert
Imports ILOG.CPLEX



Public Class MIPex3
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         ' create CPLEX optimizer/modeler an turn of presolve to make
         ' output more interesting
         Dim cplex As New Cplex()
         cplex.SetParam(cplex.BooleanParam.PreInd, False)

         ' build model
         Dim var(1)() As INumVar
         Dim rng(1)() As IRange
         PopulateByRow(cplex, var, rng)

         ' setup branch priorities
         Dim ordvar As INumVar() = {var(0)(1), var(0)(3)}
         Dim ordpri As Integer() = {8, 7}
         cplex.SetPriorities(ordvar, ordpri)

         ' setup branch directions
         cplex.SetDirection(ordvar(0), cplex.BranchDirection.Up)
         cplex.SetDirection(ordvar(1), cplex.BranchDirection.Down)

         ' write priority order to file
         cplex.WriteOrder("mipex3.ord")

         ' optimize an output solution information
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
         cplex.ExportModel("mipex3.lp")
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      End Try
   End Sub 'Main


   Friend Shared Sub PopulateByRow(ByVal model As IMPModeler, ByVal var()() As INumVar, ByVal rng()() As IRange)

      ' Define the variables one-by-one
      Dim x(3) As INumVar
      x(0) = model.NumVar(0.0, 40.0, "x0")
      x(1) = model.IntVar(0, System.Int32.MaxValue, "x1")
      x(2) = model.IntVar(0, System.Int32.MaxValue, "x2")
      x(3) = model.IntVar(2, 3, "x3")
      var(0) = x

      ' Objective Function
      model.AddMaximize(model.Sum(model.Prod(1.0, x(0)), model.Prod(2.0, x(1)), model.Prod(3.0, x(2)), model.Prod(1.0, x(3))))

      ' Define three constraints one-by-one 
      rng(0) = New IRange(2) {}
      rng(0)(0) = model.AddLe(model.Sum(model.Prod(-1.0, x(0)), model.Prod(1.0, x(1)), model.Prod(1.0, x(2)), model.Prod(10.0, x(3))), 20.0, "rng0")
      rng(0)(1) = model.AddLe(model.Sum(model.Prod(1.0, x(0)), model.Prod(-3.0, x(1)), model.Prod(1.0, x(2))), 30.0, "rng1")
      rng(0)(2) = model.AddEq(model.Sum(model.Prod(1.0, x(1)), model.Prod(-3.5, x(3))), 0, "rng2")

      ' add special ordered set of type 1
      Dim sosvars As INumVar() = {x(2), x(3)}
      Dim sosweights As Double() = {25.0, 18.0}
      model.AddSOS1(sosvars, sosweights)
   End Sub 'PopulateByRow
End Class 'MIPex3
