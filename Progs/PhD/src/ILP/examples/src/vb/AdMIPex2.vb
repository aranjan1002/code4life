' ---------------------------------------------------------------------------
' File: AdMIPex2.vb
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
' AdMIPex2.vb -  Use the heuristic callback for optimizing a MIP problem
'
' To run this example, command line arguments are required.
' i.e.,   AdMIPex2   filename
'
' Example:
'     AdMIPex2  example.mps
'
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System.Collections



Public Class AdMIPex2
   Friend Class RoundDown
      Inherits Cplex.HeuristicCallback
      Friend _vars() As INumVar
      
      Friend Sub New(vars() As INumVar)
         _vars = vars
      End Sub 'New
       
      Public Overrides Sub Main()
         Dim obj As Double() = GetObjCoefs(_vars)
         Dim x As Double() = GetValues(_vars)
         Dim feas As Cplex.IntegerFeasibilityStatus() = GetFeasibilities(_vars)
         Dim objval As Double = ObjValue
         Dim cols As Integer = _vars.Length
         Dim j As Integer
         For j = 0 To cols - 1
            ' Set the fractional variable to zero and update the objective value
            If feas(j).Equals(Cplex.IntegerFeasibilityStatus.Infeasible) Then
               objval -= x(j) * obj(j)
               x(j) = 0.0
            End If
         Next j
         SetSolution(_vars, x, objval)
      End Sub 'Main
   End Class 'RoundDown
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      If args.Length <> 1 Then
         System.Console.WriteLine("Usage: AdMIPex2 filename")
         System.Console.WriteLine("   where filename is a file with extension ")
         System.Console.WriteLine("      MPS, SAV, or LP (lower case is allowed)")
         System.Console.WriteLine(" Exiting...")
         System.Environment.Exit(-1)
      End If

      Try
         Dim cplex As New Cplex()

         cplex.ImportModel(args(0))


         'Check model is all binary except for objective constant variable
         If cplex.NbinVars < cplex.Ncols - 1 Then 
            System.Console.WriteLine ("Problem contains non-binary variables, exiting.")
            System.Environment.Exit(-1)
         End If 

         Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
         matrixEnum.MoveNext()

         Dim lp As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)

         cplex.Use(New RoundDown(lp.NumVars))

         cplex.SetParam(cplex.IntParam.MIPSearch, cplex.MIPSearch.Traditional)
         If cplex.Solve() Then
            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " + cplex.ObjValue.ToString))
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      End Try
   End Sub 'Main
End Class 'AdMIPex2
