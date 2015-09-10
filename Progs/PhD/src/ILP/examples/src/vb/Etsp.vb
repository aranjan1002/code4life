' ---------------------------------------------------------------------------
' File: Etsp.vb
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
' Etsp.vb - Solving an earliness-tardiness scheduling problem
'           using CPLEX linearization capabilities.
'
' A command line argument indicating the input data file is required
' to run this example.
'
'     Etsp ../../../examples/data/etsp.dat
'
Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class Etsp
   Private Shared Horizon As Integer = 10000
   
   Friend Class Data
      Friend nJobs                  As Integer
      Friend nResources             As Integer
      Friend activityOnResource()() As Integer
      Friend duration()()           As Double
      Friend dueDate()              As Double
      Friend earlinessCost()        As Double
      Friend tardinessCost()        As Double
      
      
      Friend Sub New(filename As String)
         Dim reader As New InputDataReader(filename)
         
         activityOnResource = reader.readIntArrayArray()
         duration           = reader.readDoubleArrayArray()
         dueDate            = reader.readDoubleArray()
         earlinessCost      = reader.readDoubleArray()
         tardinessCost      = reader.readDoubleArray()
         
         nJobs      = dueDate.Length
         nResources = activityOnResource.Length
      End Sub 'New
   End Class 'Data
   
   
   Shared Sub Main(args() As String)
      Try
         Dim filename As String = "../../../../examples/data/etsp.dat"
         If args.Length > 0 Then
            filename = args(0)
         End If 

         Dim data  As New Data(filename)
         Dim cplex As New Cplex()
         
         ' Create start variables
         Dim s(data.nJobs)() As INumVar
         Dim j As Integer
         Dim i As Integer

         For j = 0 To data.nJobs - 1
            s(j) = cplex.NumVarArray(data.nResources, 0.0, Horizon)
         Next j 

         ' State precedence constraints
         For j = 0 To data.nJobs - 1
            For i = 1 To data.nResources - 1
               cplex.AddGe(s(j)(i), cplex.Sum(s(j)((i - 1)), _
                           data.duration(j)((i - 1))))
            Next i
         Next j 

         ' State disjunctive constraints for each resource
         For i = 0 To data.nResources - 1
            Dim [end] As Integer = data.nJobs - 1
            For j = 0 To [end] - 1
               Dim a As Integer = data.activityOnResource(i)(j)
               Dim k As Integer
               For k = j + 1 To data.nJobs - 1
                  Dim b As Integer = data.activityOnResource(i)(k)
                  cplex.Add(cplex.or(cplex.Ge(s(j)(a), cplex.Sum(s(k)(b), data.duration(k)(b))), _
                                     cplex.Ge(s(k)(b), cplex.Sum(s(j)(a), data.duration(j)(a)))))
               Next k
            Next j
         Next i
         
         ' The cost is the sum of earliness or tardiness costs of each job
         Dim last    As Integer  = data.nResources - 1
         Dim costSum As INumExpr = cplex.numExpr()
         For j = 0 To data.nJobs - 1
            Dim points As Double() = {1, data.dueDate(j)}
            Dim slopes As Double() = {2, - data.earlinessCost(j), data.tardinessCost(j)}
            costSum = cplex.Sum(costSum, _
                                cplex.PiecewiseLinear(cplex.Sum(s(j)(last), data.duration(j)(last)), _
                                                      points, slopes, data.dueDate(j), 0))
         Next j
         cplex.AddMinimize(costSum)
         
         cplex.SetParam(cplex.IntParam.MIPEmphasis, 4)
         
         If cplex.Solve() Then
            System.Console.WriteLine((" Optimal Value = " + cplex.ObjValue.ToString))
         End If 
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      Catch ex As InputDataReader.InputDataReaderException
         System.Console.WriteLine(("Data Error: " + ex.ToString))
      Catch ex As System.IO.IOException
         System.Console.WriteLine(("IO Error: " + ex.ToString))
      End Try
   End Sub 'Main
End Class 'Etsp
