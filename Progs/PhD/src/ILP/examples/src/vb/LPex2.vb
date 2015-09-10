' ---------------------------------------------------------------------------
' File: LPex2.vb
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
' LPex2.vb - Reading in and optimizing an LP problem
'
'
' To run this example, command line arguments are required.
' i.e.,   LPex2   filename   method
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
'                 c          concurrent optimization
' Example:
'     LPex2  example.mps  o
'
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System.Collections



Public Class LPex2
   
   Friend Shared Sub Usage()
      System.Console.WriteLine("usage:  LPex2 <filename> <method>")
      System.Console.WriteLine("          o       default")
      System.Console.WriteLine("          p       primal simplex")
      System.Console.WriteLine("          d       dual   simplex")
      System.Console.WriteLine("          h       barrier with crossover")
      System.Console.WriteLine("          b       barrier without crossover")
      System.Console.WriteLine("          n       network with dual simplex cleanup")
      System.Console.WriteLine("          s       sifting")
      System.Console.WriteLine("          c       concurrent optimization")
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
            System.Console.WriteLine(("Solution value  = " + cplex.ObjValue.ToString))

            ' Access the ILPMatrix object that has been read from a file in
            ' order to access variables which are the columns of the LP.  The
            ' method importModel() guarantees that exactly one ILPMatrix
            ' object will exist, which is why no tests or enumerators are
            ' needed in the following line of code.
            Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
            matrixEnum.MoveNext()

            Dim lp As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)

            Dim x As Double() = cplex.GetValues(lp)
            Dim j As Integer

            For j = 0 To x.Length - 1
               System.Console.WriteLine(("Variable " & j & ": Value = " & x(j)))
            Next j
            Try ' basis may not exist
                Dim cstat As Cplex.BasisStatus() = cplex.GetBasisStatuses(lp.NumVars)
                For j = 0 To x.Length - 1
                   System.Console.WriteLine(("Variable " & j & ": Basis Status = " + cstat(j).ToString))
                Next j
            Catch e As System.Exception
            End Try

            System.Console.WriteLine(("Maximum bound violation = " + cplex.GetQuality(cplex.QualityType.MaxPrimalInfeas).ToString))
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      End Try
   End Sub 'Main
End Class 'LPex2
