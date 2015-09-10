' ---------------------------------------------------------------------------
' File: Goalex1.vb
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
' Goalex1.vb - A simple goal example.
'
'              Create a branch goal that chooses the variable
'              with the largest objective from amongst those
'              with the largest integer infeasibility.
'
Imports ILOG.CPLEX
Imports ILOG.Concert
Imports System.Collections


Public Class Goalex1
   Friend Class MyBranchGoal
      Inherits Cplex.Goal
      Friend _vars() As INumVar


      Friend Sub New(vars() As INumVar)
         _vars = vars
      End Sub 'New


      ' Branch on var with largest objective coefficient
      ' among those with largest infeasibility
      Public Overrides Function Execute(cplex As Cplex) As Cplex.Goal
         Dim x As Double() = GetValues(_vars)
         Dim obj As Double() = GetObjCoefs(_vars)
         Dim feas As Cplex.IntegerFeasibilityStatus() = GetFeasibilities(_vars)

         Dim maxinf As Double = 0.0
         Dim maxobj As Double = 0.0
         Dim bestj As Integer = - 1
         Dim cols As Integer = _vars.Length
         Dim j As Integer

         For j = 0 To cols - 1
            If feas(j).Equals(cplex.IntegerFeasibilityStatus.Infeasible) Then
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
            Return cplex.And(cplex.Or(cplex.GeGoal(_vars(bestj), System.Math.Floor(x(bestj)) + 1), _
                                      cplex.LeGoal(_vars(bestj), System.Math.Floor(x(bestj)))), _
                             Me)
         Else
            Return Nothing
         End If
      End Function 'Execute
   End Class 'MyBranchGoal


   Public Overloads Shared Sub Main(ByVal args() As String)
      If args.Length <> 1 Then
         System.Console.WriteLine("Usage: Goalex1 filename")
         System.Console.WriteLine("   where filename is a file with extension ")
         System.Console.WriteLine("      MPS, SAV, or LP (lower case is allowed)")
         System.Console.WriteLine(" Exiting...")
         Return
      End If

      Try
         Dim cplex As New Cplex()

         cplex.ImportModel(args(0))

         Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
         matrixEnum.MoveNext()

         Dim lp As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)

	 cplex.SetParam(cplex.IntParam.MIPSearch, cplex.MIPSearch.Traditional)
         If cplex.Solve(New MyBranchGoal(lp.NumVars)) Then
            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " + cplex.ObjValue.ToString))
         End If

         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      End Try
   End Sub 'Main
End Class 'Goalex1
