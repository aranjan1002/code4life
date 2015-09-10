' ---------------------------------------------------------------------------
' File: TuneSet.vb
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
' TuneSet.vb - Tune parameters for a set of problems
'
' To run this example, command line arguments are required.
' i.e.,   TuneSet [options] file1 file2 ... filen
' where
'     each filei is the name of a file, with .mps, .lp, or
'        .sav extension
'     options are described in usage().
' Example:
'     TuneSet  mexample.mps
'

Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class TuneSet
   Friend Shared Sub Usage()
      System.Console.WriteLine("usage:  TuneSet [options] file1 file2 ... filen")
      System.Console.WriteLine("   where")
      System.Console.WriteLine("      filei is a file with extension MPS, SAV, or LP")
      System.Console.WriteLine("      and options are:")
      System.Console.WriteLine("         -a for average measure")
      System.Console.WriteLine("         -m for minmax measure")
      System.Console.WriteLine("         -f <file> where file is a fixed parameter file")
      System.Console.WriteLine("         -o <file> where file is the tuned parameter file")
   End Sub

   Public Shared Sub Main(ByVal args As String())
      If args.Length < 1 Then
         Usage()
         Return
      End If
      Try
         Dim cplex As New Cplex()

         Dim fixedfile As String = Nothing
         Dim tunedfile As String = Nothing
         Dim tunemeasure As Integer = 0
         Dim mset As Boolean = False
         Dim tmpfilenames As New System.Collections.ArrayList()
         For i As Integer = 0 To args.Length - 1
            If args(i).ToCharArray()(0) <> "-"C Then
               tmpfilenames.Add(args(i))
            End If
            Select Case args(i).ToCharArray()(1)
               Case "a"C
                  tunemeasure = 1
                  mset = True
                  Exit Select
               Case "m"C
                  tunemeasure = 2
                  mset = True
                  Exit Select
               Case "f"C
                  fixedfile = args(System.Threading.Interlocked.Increment(i))
                  Exit Select
               Case "o"C
                  tunedfile = args(System.Threading.Interlocked.Increment(i))
                  Exit Select
            End Select
         Next

         Dim filenames As String() = New String(tmpfilenames.Count - 1) {}
         System.Console.WriteLine("Problem set:")
         For i As Integer = 0 To filenames.Length - 1
            filenames(i) = DirectCast(tmpfilenames(i), String)
            System.Console.WriteLine("  " + filenames(i))
         Next

         If mset Then
            cplex.SetParam(Cplex.IntParam.TuningMeasure, tunemeasure)
         End If

         Dim paramset As Cplex.ParameterSet = Nothing

         If Not (fixedfile Is Nothing) Then
            cplex.ReadParam(fixedfile)
            paramset = cplex.GetParameterSet()
            cplex.SetDefaults()
         End If

         Dim tunestat as Integer
         tunestat = cplex.TuneParam(filenames, paramset)

         If     tunestat = Cplex.TuningStatus.Complete   Then
            System.Console.WriteLine("Tuning complete.")
         ElseIf tunestat = cplex.TuningStatus.Abort Then
            System.Console.WriteLine("Tuning abort.")
         ElseIf tunestat = cplex.TuningStatus.TimeLim Then
            System.Console.WriteLine("Tuning time limit.")
         Else
            System.Console.WriteLine("Tuning status unknown.")
         End If

         If Not (tunedfile Is Nothing) Then
            cplex.WriteParam(tunedfile)
            System.Console.WriteLine("Tuned parameters written to file '" + tunedfile + "'")
         End If
         cplex.[End]()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine("Concert exception caught: " + e.ToString())
      End Try
   End Sub
End Class
