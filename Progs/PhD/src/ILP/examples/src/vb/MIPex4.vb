' ---------------------------------------------------------------------------
' File: MIPex4.vb
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
' MIPex4.vb - Reading in and optimizing a problem using a callback
'             to log or interrupt or a Cplex.Aborter to interrupt
'
' To run this example, command line arguments are required.
' i.e.,   MIPex4   filename option
'                     where option is one of
'                        t to use the time-limit-gap callback
'                        l to use the logging callback
'                        a to use the aborter
'
' Example:
'     MIPex4  mexample.mps l
'
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System.Collections


Public Class MIPex4

   Friend Shared Sub Usage()
      System.Console.WriteLine("usage:  MIPex4 <filename> <option>")
      System.Console.WriteLine("        t  to use the time-limit-gap callback")
      System.Console.WriteLine("        l  to use the logging callback")
      System.Console.WriteLine("        a  to use the aborter")
   End Sub 'Usage


   ' Spend at least timeLimit seconds on optimization, but once
   ' this limit is reached, quit as soon as the solution is acceptable
   Friend Class TimeLimitCallback
      Inherits Cplex.MIPInfoCallback
      Friend _cplex As Cplex
      Friend _aborted As Boolean
      Friend _timeStart As Double 
      Friend _timeLimit As Double 
      Friend _acceptableGap As Double


      Friend Sub New(cplex As Cplex, aborted As Boolean, timeStart as Double, timeLimit As Double, acceptableGap As Double)
         _cplex = cplex
         _aborted = aborted
         _timeStart = timeStart
         _timeLimit = timeLimit
         _acceptableGap = acceptableGap
      End Sub 'New


      Public Overrides Sub Main()
         If Not _aborted And HasIncumbent() Then
            Dim gap As Double = 100.0 * MIPRelativeGap
            Dim timeUsed As Double = _cplex.CplexTime - _timeStart
            If timeUsed > _timeLimit And gap < _acceptableGap Then
               System.Console.WriteLine("")
               System.Console.WriteLine(("Good enough solution at " & timeUsed & " sec., gap = " & gap & "%, quitting."))
               _aborted = True
               Abort()
            End If
         End If
      End Sub 'Main
   End Class 'TimeLimitCallback


   ' Log new incumbents if they are at better than the old by a
   ' relative tolerance of 1e-5; also log progress info every
   ' 100 nodes.
   Friend Class LogCallback
      Inherits Cplex.MIPInfoCallback
      Private _var() As INumVar
      Private _lastLog As Integer
      Private _lastIncumbent As Double


      Friend Sub New(var() As INumVar, lastLog As Integer, lastIncumbent As Double)
         _var = var
         _lastLog = lastLog
         _lastIncumbent = lastIncumbent
      End Sub 'New


      Public Overrides Sub Main()
         Dim newIncumbent As Boolean = False 
         Dim nodes As Integer = Nnodes

         If HasIncumbent() And System.Math.Abs((_lastIncumbent - IncumbentObjValue)) > 1E-05 *(1.0 + System.Math.Abs(IncumbentObjValue)) Then
            _lastIncumbent = IncumbentObjValue
            newIncumbent = True
         End If

         If nodes >= _lastLog + 100 Or newIncumbent Then
            If Not newIncumbent Then _lastLog = nodes
            System.Console.Write(("Nodes = " & nodes & "(" & NremainingNodes & ")" & "  Best objective = " & BestObjValue))
            If HasIncumbent() Then
               System.Console.WriteLine(("  Incumbent objective = " & IncumbentObjValue))
            Else
               System.Console.WriteLine((""))
            End If
         End If

            If newIncumbent Then
                System.Console.WriteLine("New incumbent values: ")

                Dim n As Integer = _var.Length
                Dim x As Double() = GetIncumbentValues(_var, 0, n)
                Dim j As Integer
                For j = 0 To n - 1
                    System.Console.WriteLine(("Variable " & j & ": Value = " & x(j)))
                Next j
            End If
      End Sub 'Main
   End Class 'LogCallback


   Public Overloads Shared Sub Main(ByVal args() As String)
      If args.Length <> 2 Then
         Usage()
         Return
      End If
      Try
         Dim useLoggingCallback As Boolean = False
         Dim useTimeLimitCallback As Boolean = False
         Dim useAborter As Boolean = False

         Dim myAborter As Cplex.Aborter

         Select Case args(1).ToCharArray()(0)
            Case "t"c
               useTimeLimitCallback = True
            Case "l"c
               useLoggingCallback = True
            Case "a"c
               useAborter = True
            Case Else
               Usage()
               Return
         End Select

         Dim cplex As New Cplex

         cplex.ImportModel(args(0))
         Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
         matrixEnum.MoveNext()
         Dim lp As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)
         Dim obj As IObjective = cplex.GetObjective()

         ' Set an overall node limit in case callback conditions
         ' are not met.
         cplex.SetParam(cplex.IntParam.NodeLim, 5000)

         If useLoggingCallback Then
            Dim lastObjVal As Double
            If obj.Sense.Equals(ObjectiveSense.Minimize) Then
               lastObjVal = System.Double.MaxValue
            Else
               lastObjVal = -System.Double.MaxValue
            End If

            cplex.Use(New LogCallback(lp.NumVars, -100000, lastObjVal))

            ' Turn off CPLEX logging
            cplex.SetParam(cplex.IntParam.MIPDisplay, 0)
         Else
            If useTimeLimitCallback Then
               cplex.Use(New TimeLimitCallback(cplex, False, cplex.CplexTime, 1000, 10.0))
            Else
               If useAborter Then
                  myAborter = New Cplex.Aborter
                  cplex.Use(myAborter)
                  ' Typically, you would pass the Aborter object to
                  ' another thread or pass it to an interrupt handler,
                  ' and  monitor for some event to occur.  When it does,
                  ' call the Aborter's abort method.
                  '
                  ' To illustrate its use without creating a thread or
                  ' an interrupt handler, abort immediately by calling
                  ' abort before the solve.
                  '
                   myAborter.Abort()
               End If
            End If
         End If
         cplex.Solve()

         System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
         System.Console.WriteLine(("Cplex status = " + cplex.GetCplexStatus().ToString))

         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      End Try
   End Sub 'Main
End Class 'MIPex4
