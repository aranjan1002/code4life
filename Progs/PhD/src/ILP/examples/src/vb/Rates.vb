' ---------------------------------------------------------------------------
' File: Rates.vb
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
' Rates.vb -- modeling with semi-continuous variables
'
' Problem Description:
' 
' Assume you run a power supply company.  You have several power generators
' available, each of which has a minimum and maximum production level and a cost
' per unit output.  The question is which generators to use in order to
' minimize the overall operation cost while satisfying the demand.
' 
' --------------------------------------------------------------------------
Imports ILOG.Concert
Imports ILOG.CPLEX



Public Class Rates
   Friend Shared _generators As Integer
   
   Friend Shared _minArray() As Double
   Friend Shared _maxArray() As Double
   Friend Shared _cost() As Double
   Friend Shared _demand As Double
   
   
   Friend Shared Sub ReadData(fileName As String)
      Dim reader As New InputDataReader(fileName)
      
      _minArray = reader.ReadDoubleArray()
      _maxArray = reader.ReadDoubleArray()
      _cost = reader.ReadDoubleArray()
      _demand = reader.ReadDouble()
      
      _generators = _minArray.Length
   End Sub 'ReadData
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim filename As String = "../../../../examples/data/rates.dat"
         If args.Length > 0 Then
            filename = args(0)
         End If
         ReadData(filename)

         Dim cplex As New Cplex()

         Dim production(_generators - 1) As INumVar
         Dim j As Integer

         For j = 0 To _generators - 1
            production(j) = cplex.SemiContVar(_minArray(j), _maxArray(j), NumVarType.Float)
         Next j

         cplex.AddMinimize(cplex.ScalProd(_cost, production))
         cplex.AddGe(cplex.Sum(production), _demand)

         cplex.ExportModel("rates.lp")
         If cplex.Solve() Then
            For j = 0 To _generators - 1
               System.Console.WriteLine(("   generator " & j & ": " & cplex.GetValue(production(j))))
            Next j
            System.Console.WriteLine(("Total cost = " & cplex.ObjValue))
         Else
            System.Console.WriteLine("No solution")
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
End Class 'Rates

'
'   generator 0: 15.6
'   generator 1: 0
'   generator 2: 0
'   generator 3: 27.8
'   generator 4: 27.8
'   generator 5: 28.8
'   generator 6: 29
'   generator 7: 29
'   generator 8: 29
'Total cost = 1625.24
'
