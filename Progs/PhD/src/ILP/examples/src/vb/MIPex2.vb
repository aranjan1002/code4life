' ---------------------------------------------------------------------------
' File: MIPex2.vb
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
' MIPex2.vb - Reading in and optimizing a MIP problem.  In fact, this
'             program also works for LP or QP problems, but is different
'             from LPex2 in that no dual solution information is queried.
'
' To run this example, command line arguments are required.
' i.e.,   MIPex2  filename
' where 
'     filename is the name of the file, with .mps, .lp, or .sav extension
' Example:
'     MIPex2  mexample.mps
'
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System.Collections

 _


Public Class MIPex2
   
   Friend Shared Sub Usage()
      System.Console.WriteLine("usage:  MIPex2 <filename>")
   End Sub 'Usage
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      If args.Length <> 1 Then
         Usage()
         Return
      End If
      Try
         Dim cplex As New Cplex()

         cplex.ImportModel(args(0))

         If cplex.Solve() Then
            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " & cplex.ObjValue))

            ' access ILPMatrix object that has been read from file in order to
            ' access variables which are the columns of the lp.  Method
            ' importModel guarantees to the model into exactly on ILPMatrix
            ' object which is why there are no test or iterations needed in the
            ' following line of code.
            Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
            matrixEnum.MoveNext()

            Dim lp As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)

            Dim x As Double() = cplex.GetValues(lp)
            Dim j As Integer

            For j = 0 To x.Length - 1
               System.Console.WriteLine(("Variable " & j & ": Value = " & x(j)))
            Next j
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      End Try
   End Sub 'Main
End Class 'MIPex2
