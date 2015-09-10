' ---------------------------------------------------------------------------
' File: AdMIPex1.vb
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
' AdMIPex1.vb -- Use the node and branch callbacks to optimize
'                a MIP problem
'
' To run this example, command line arguments are required.
' i.e.,   AdMIPex1   filename
'
' Example:
'      AdMIPex1  example.mps
'
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System.Collections



Public Class AdMIPex1
   Friend Class MyBranch
      Inherits Cplex.BranchCallback
      Friend _vars() As INumVar

      Friend Sub New(ByVal vars() As INumVar)
          _vars = vars
      End Sub 'New

      Public Overrides Sub Main()
         If Not GetBranchType().Equals(Cplex.BranchType.BranchOnVariable) Then
            Return
         End If
         ' Branch on var with largest objective coefficient
         ' among those with largest infeasibility
         Dim x As Double() = GetValues(_vars)
         Dim obj As Double() = GetObjCoefs(_vars)
         Dim feas As Cplex.IntegerFeasibilityStatus() = GetFeasibilities(_vars)

         Dim maxinf As Double = 0.0
         Dim maxobj As Double = 0.0
         Dim bestj As Integer = -1
         Dim cols As Integer = _vars.Length
         Dim j As Integer

         For j = 0 To cols - 1
            If feas(j).Equals(Cplex.IntegerFeasibilityStatus.Infeasible) Then
               Dim xj_inf As Double = x(j) - System.Math.Floor(x(j))
               If xj_inf > 0.5 Then
                  xj_inf = 1.0 - xj_inf
               End If
               If xj_inf >= maxinf AndAlso (xj_inf > maxinf OrElse System.Math.Abs(obj(j)) >= maxobj) Then
                  bestj = j
                  maxinf = xj_inf
                  maxobj = System.Math.Abs(obj(j))
               End If
            End If
         Next j

         If bestj >= 0 Then
            MakeBranch(_vars(bestj), x(bestj), Cplex.BranchDirection.Up, ObjValue)
            MakeBranch(_vars(bestj), x(bestj), Cplex.BranchDirection.Down, ObjValue)
         End If
      End Sub 'Main
   End Class 'MyBranch

   Friend Class MySelect
      Inherits Cplex.NodeCallback

      Public Overrides Sub Main()
         Dim remainingNodes As Integer = NremainingNodes
         Dim bestnode As Integer = -1
         Dim maxdepth As Integer = -1
         Dim maxiisum As Double = 0.0

         Dim i As Integer

         For i = 0 To remainingNodes - 1
             Dim depth As Integer = GetDepth(i)
             Dim iisum As Double = GetInfeasibilitySum(i)
             If depth >= maxdepth AndAlso (depth > maxdepth OrElse iisum > maxiisum) Then
                 bestnode = i
                 maxdepth = depth
                 maxiisum = iisum
             End If
         Next i
         If bestnode >= 0 Then
             SelectNode(bestnode)
         End If
      End Sub 'Main
   End Class 'MySelect

   Public Overloads Shared Sub Main(ByVal args() As String)
      If args.Length <> 1 Then
         System.Console.WriteLine("Usage: AdMIPex1 filename")
         System.Console.WriteLine("   where filename is a file with extension ")
         System.Console.WriteLine("      MPS, SAV, or LP (lower case is allowed)")
         System.Console.WriteLine(" Exiting...")
         System.Environment.Exit(-1)
      End If

      Try
         Dim cplex As New Cplex()

         cplex.ImportModel(args(0))

         Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
         matrixEnum.MoveNext()

         Dim lp As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)

         cplex.Use(New MyBranch(lp.NumVars))
         cplex.Use(New MySelect())

	 cplex.SetParam(cplex.IntParam.MIPSearch, cplex.MIPSearch.Traditional)
         If cplex.Solve() Then
            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " & cplex.ObjValue().ToString))
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      End Try
   End Sub 'Main
End Class 'AdMIPex1
