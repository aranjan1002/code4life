' ---------------------------------------------------------------------------
' File: LPex7.vb
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
' LPex7.vb - Reading in and optimizing a problem.  Printing
'            names with the answer.  This is a modification of
'            LPex2.vb
'
' To run this example, command line arguments are required.
' i.e.,   LPex7   filename   method
' where 
'     filename is the name of the file, with .mps, .lp, or .sav extension
'     method   is the optimization method
'                 o          default
'                 p          primal simplex
'                 d          dual   simplex
'                 h          barrier with crossover
'                 b          barrier without crossover
'                 n          network with dual simplex cleanup
'                 s          sifting
'                 c          concurrent
' Example:
'     LPex7  example.mps  o
'
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System.Collections



Public Class LPex7
   
   Friend Shared Sub Usage()
      System.Console.WriteLine("usage:  LPex7 <filename> <method>")
      System.Console.WriteLine("          o       default")
      System.Console.WriteLine("          p       primal simplex")
      System.Console.WriteLine("          d       dual   simplex")
      System.Console.WriteLine("          h       barrier with crossover")
      System.Console.WriteLine("          b       barrier without crossover")
      System.Console.WriteLine("          n       network with dual simplex cleanup")
      System.Console.WriteLine("          s       sifting")
      System.Console.WriteLine("          c       concurrent")
   End Sub 'Usage
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      If args.Length <> 2 Then
         Usage()
         Return
      End If
      Try
         ' Create the modeler/solver object
         Dim cplex As New Cplex()

         ' Evaluate command line option and set optimization method accordingly.
         Select Case args(1).ToCharArray()(0)
            Case "o"c
               cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Auto)
            Case "p"c
               cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Primal)
            Case "d"c
               cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Dual)
            Case "h"c
               cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Barrier)
            Case "b"c
               cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Barrier)
               cplex.SetParam(cplex.IntParam.BarCrossAlg, cplex.Algorithm.None)
            Case "n"c
               cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Network)
            Case "s"c
               cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Sifting)
            Case "c"c
               cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Concurrent)
            Case Else
               Usage()
               Return
         End Select

         ' Read model from file with name args[0] into cplex optimizer object
         cplex.ImportModel(args(0))

         ' Solve the model and display the solution if one was found
         If cplex.Solve() Then
            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " & cplex.ObjValue))

            ' Access the ILPMatrix object that has been read from a file in
            ' order to access variables which are the columns of the LP.  The
            ' method importModel() guarantees that exactly one ILPMatrix
            ' object will exist, which is why no tests or enumerators are
            ' needed in the following line of code.
            Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
            matrixEnum.MoveNext()

            Dim lp As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)
            Dim vars As INumVar() = lp.NumVars
            Dim vals As Double() = cplex.GetValues(vars)

            ' Basis information is not available for barrier without crossover.
            ' Thus we include the query in a try statement and print the solution
            ' without barrier information in case we get an exception.
            Try
               Dim bStat As Cplex.BasisStatus() = cplex.GetBasisStatuses(vars)
               Dim i As Integer
               For i = 0 To vals.Length - 1
                  System.Console.WriteLine(("Variable " + vars(i).Name + " has vals " _
                                           & vals(i) & " and status " + bStat(i).ToString))
               Next i
            Catch e As ILOG.Concert.Exception
               Dim i As Integer
               For i = 0 To vals.Length - 1
                  System.Console.WriteLine(("Variable " + vars(i).Name + " has vals " & vals(i)))
               Next i
            End Try
         End If
         cplex.End()
      Catch exc As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + exc.ToString + "' caught"))
      End Try
   End Sub 'Main
End Class 'LPex7
