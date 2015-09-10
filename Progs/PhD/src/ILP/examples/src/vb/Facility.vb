' ---------------------------------------------------------------------------
' File: Facility.vb
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
Imports ILOG.Concert
Imports ILOG.CPLEX

 _


Public Class Facility
   Friend Shared _capacity() As Double
   Friend Shared _fixedCost() As Double
   Friend Shared _cost()() As Double
   
   Friend Shared _nbLocations As Integer
   Friend Shared _nbClients As Integer
   
   
   Friend Shared Sub ReadData(fileName As String)
      System.Console.WriteLine(("Reading data from " + fileName))
      Dim reader As New InputDataReader(fileName)
      
      _capacity = reader.ReadDoubleArray()
      _fixedCost = reader.ReadDoubleArray()
      _cost = reader.ReadDoubleArrayArray()
      
      _nbLocations = _capacity.Length
      _nbClients = _cost.Length
      
      Dim i As Integer
      For i = 0 To _nbClients - 1
         If _cost(i).Length <> _nbLocations Then
         Throw New ILOG.Concert.Exception("inconsistent data in file " + fileName)
         End If
      Next i 
   End Sub 'ReadData
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim filename As String = "../../../../examples/data/facility.dat"
         If args.Length > 0 Then
            filename = args(0)
         End If
         ReadData(filename)

         Dim cplex As New Cplex()
         Dim open As INumVar() = cplex.BoolVarArray(_nbLocations)

         Dim supply(_nbClients)() As INumVar
         Dim i As Integer
         For i = 0 To _nbClients - 1
            supply(i) = cplex.BoolVarArray(_nbLocations)
         Next i
         For i = 0 To _nbClients - 1
            cplex.AddEq(cplex.Sum(supply(i)), 1)
         Next i
         Dim j As Integer
         For j = 0 To _nbLocations - 1
            Dim v As ILinearNumExpr = cplex.LinearNumExpr()
            For i = 0 To _nbClients - 1
               v.AddTerm(1.0, supply(i)(j))
            Next i
            cplex.AddLe(v, cplex.Prod(_capacity(j), open(j)))
         Next j

         Dim obj As ILinearNumExpr = cplex.ScalProd(_fixedCost, open)
         For i = 0 To _nbClients - 1
            obj.Add(cplex.ScalProd(_cost(i), supply(i)))
         Next i
         cplex.AddMinimize(obj)

         If cplex.Solve() Then
            Dim tolerance As Double = cplex.GetParam(cplex.DoubleParam.EpInt)
            System.Console.WriteLine(("Optimal value: " & cplex.ObjValue))
            For j = 0 To _nbLocations - 1
               If cplex.GetValue(open(j)) >= 1 - tolerance Then
                  System.Console.Write(("Facility " & j & " is open, it serves clients "))
                  For i = 0 To _nbClients - 1
                     If cplex.GetValue(supply(i)(j)) >= 1 - tolerance Then
                        System.Console.Write((" " & i))
                     End If
                  Next i
                  System.Console.WriteLine()
               End If
            Next j
         End If
         cplex.End()
      Catch exc As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + exc.ToString + "' caught"))
      Catch exc As System.IO.IOException
         System.Console.WriteLine(("Error reading file " + args(0) + ": " + exc.ToString))
      Catch exc As InputDataReader.InputDataReaderException
         System.Console.WriteLine(exc)
      End Try
   End Sub 'Main
End Class 'Facility

'
'Optimal value: 1383
'Facility 0 is open, it serves clients 2 5 7
'Facility 1 is open, it serves clients 3
'Facility 3 is open, it serves clients 0 1 4 6
'
