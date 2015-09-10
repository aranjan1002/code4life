' ---------------------------------------------------------------------------
' File: AdMIPex6.vb
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
' AdMIPex6.vb -- Solving a model by passing in a solution for the root node
'                and using that in a solve callback
'
' To run this example, command line arguments are required:
'     AdMIPex6  filename
' where 
'     filename  Name of the file, with .mps, .lp, or .sav
'               extension, and a possible additional .gz 
'               extension.
' Example:
'     AdMIPex6  mexample.mps.gz
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System.Collections


Public Class AdMIPex6
   Friend Class Solve
      Inherits Cplex.SolveCallback
      Friend _done   As Boolean = False
      Friend _vars() As INumVar
      Friend _x()    As Double
      
      Friend Sub New(vars() As INumVar, x() As Double)
         _vars = vars
         _x = x
      End Sub 'New
       
      Public Overrides Sub Main()
         If Not _done Then
            SetVectors(_x, _vars, Nothing, Nothing)
            _done = True
         End If
      End Sub 'Main
   End Class 'Solve
   

   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim cplex As New Cplex()

         cplex.ImportModel(args(0))

         Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
         matrixEnum.MoveNext()

         Dim lp As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)

         Dim relax As IConversion = cplex.Conversion(lp.NumVars, NumVarType.Float)
         cplex.Add(relax)

         cplex.Solve()
         System.Console.WriteLine(("Relaxed solution status = " + cplex.GetStatus().ToString))
         System.Console.WriteLine(("Relaxed solution value  = " + cplex.ObjValue.ToString))

         Dim vals As Double() = cplex.GetValues(lp.NumVars)
         cplex.Use(New Solve(lp.NumVars, vals))

         cplex.Delete(relax)

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
End Class 'AdMIPex6
