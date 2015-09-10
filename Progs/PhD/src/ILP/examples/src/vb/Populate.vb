' ---------------------------------------------------------------------------
' File: Populate.vb
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
' Populate.vb - Reading in and generating multiple solutions to a MIP
'               problem.
'
' To run this example, command line arguments are required.
' i.e.,   Populate  filename
' where
'     filename is the name of the file, with .mps, .lp, or .sav extension
' Example:
'     Populate  location.lp
'
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System.Collections

Public Class Populate
   Friend Shared EPSZERO As Double = 1E-10
   
   Friend Shared Sub Usage()
      System.Console.WriteLine("usage:  Populate <filename>")
   End Sub 'Usage
   
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      If args.Length <> 1 Then
         Usage()
         Return
      End If
      Try
         Dim cplex As New Cplex()
         
         cplex.ImportModel(args(0))
         
         ' Set the solution pool relative gap parameter to obtain solutions
         ' of objective value within 10% of the optimal 
         
         cplex.SetParam(Cplex.DoubleParam.SolnPoolGap, 0.1)
         
         If cplex.Populate() Then
            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Incumbent objective value  = " & cplex.ObjValue))
            
            ' access ILPMatrix object that has been read from file in order to
            ' access variables which are the columns of the lp.  Method
            ' importModel guarantees to the model into exactly on ILPMatrix
            ' object which is why there are no test or iterations needed in the
            ' following line of code.
            Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
            matrixEnum.MoveNext()
            
            Dim lp As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)
            
            Dim incx As Double() = cplex.GetValues(lp)
            Dim j As Integer
            For j = 0 To incx.Length - 1
               System.Console.WriteLine(("Variable " & j & ": Value = " & incx(j)))
            Next j
            System.Console.WriteLine()
            
            ' Get the number of solutions in the solution pool 
            
            Dim numsol As Integer = cplex.SolnPoolNsolns
            System.Console.WriteLine(("The solution pool contains " & numsol & " solutions."))
            
            ' Some solutions are deleted from the pool because of the
            ' solution pool relative gap parameter 
            
            Dim numsolreplaced As Integer = cplex.SolnPoolNreplaced
            System.Console.WriteLine((numsolreplaced & " solutions were removed due to the " & "solution pool relative gap parameter."))
            
            System.Console.WriteLine(("In total, " &(numsol + numsolreplaced) & " solutions were generated."))
            
            ' Get the average objective value of solutions in the solution pool 
            
            System.Console.WriteLine(("The average objective value of the solutions is " & cplex.SolnPoolMeanObjValue & "."))
            System.Console.WriteLine()
            
            ' Write out the objective value of each solution and its
            ' difference to the incumbent 
            
            
            Dim i As Integer
            For i = 0 To numsol - 1
               
               Dim x As Double() = cplex.GetValues(lp, i)
               
               ' Compute the number of variables that differ in the
               ' solution and in the incumbent 
               
               Dim numdiff As Integer = 0
               
               For j = 0 to x.Length - 1
                  If System.Math.Abs((x(j) - incx(j))) > EPSZERO Then
                     numdiff += 1
                  End If
               Next j
               System.Console.WriteLine(("Solution " & i & " with objective " & cplex.GetObjValue(i) & " differs in " & numdiff & " of " & x.length & " variables."))

            Next i
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + e.ToString + "' caught"))
      End Try
   End Sub 'Main
End Class 'Populate
