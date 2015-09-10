' ---------------------------------------------------------------------------
' File: Diet.vb   
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
' A dietary model.
'
' Input data:
' foodMin[j]          minimum amount of food j to use
' foodMax[j]          maximum amount of food j to use 
' foodCost[j]         cost for one unit of food j
' nutrMin[i]          minimum amount of nutrient i
' nutrMax[i]          maximum amount of nutrient i
' nutrPerFood[i][j]   nutrition amount of nutrient i in food j
'
' Modeling variables:
' Buy[j]          amount of food j to purchase
'
' Objective:
' minimize sum(j) Buy[j] * foodCost[j]
'
' Constraints:
' forall foods i: nutrMin[i] <= sum(j) Buy[j] * nutrPer[i][j] <= nutrMax[j]
'
Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class Diet
   Friend Class Data
      Friend nFoods As Integer
      Friend nNutrs As Integer
      Friend foodCost() As Double
      Friend foodMin() As Double
      Friend foodMax() As Double
      Friend nutrMin() As Double
      Friend nutrMax() As Double
      Friend nutrPerFood()() As Double
      
      
      Friend Sub New(filename As String)
         Dim reader As New InputDataReader(filename)
         
         foodCost = reader.ReadDoubleArray()
         foodMin = reader.ReadDoubleArray()
         foodMax = reader.ReadDoubleArray()
         nutrMin = reader.ReadDoubleArray()
         nutrMax = reader.ReadDoubleArray()
         nutrPerFood = reader.ReadDoubleArrayArray()
         
         nFoods = foodMax.Length
         nNutrs = nutrMax.Length
         
         If nFoods <> foodMin.Length Or nFoods <> foodMax.Length Then
            Throw New ILOG.Concert.Exception("inconsistent data in file " + filename)
         End If
         If nNutrs <> nutrMin.Length Or nNutrs <> nutrPerFood.Length Then
            Throw New ILOG.Concert.Exception("inconsistent data in file " + filename)
         End If
         Dim i As Integer
         
         For i = 0 To nNutrs - 1
            If nutrPerFood(i).Length <> nFoods Then
                 Throw New ILOG.Concert.Exception("inconsistent data in file " + filename)
            End If
         Next i
      End Sub 'New
   End Class 'Data
    
   Friend Shared Sub BuildModelByRow(model As IModeler, data As Data, Buy() As INumVar, type As NumVarType)
      Dim nFoods As Integer = data.nFoods
      Dim nNutrs As Integer = data.nNutrs
      
      Dim j As Integer
      For j = 0 To nFoods - 1
         Buy(j) = model.NumVar(data.foodMin(j), data.foodMax(j), type)
      Next j
      model.AddMinimize(model.ScalProd(data.foodCost, Buy))
      
      Dim i As Integer
      For i = 0 To nNutrs - 1
         model.AddRange(data.nutrMin(i), model.ScalProd(data.nutrPerFood(i), Buy), data.nutrMax(i))
      Next i
   End Sub 'BuildModelByRow
   
   Friend Shared Sub BuildModelByColumn(model As IMPModeler, data As Data, Buy() As INumVar, type As NumVarType)
      Dim nFoods As Integer = data.nFoods
      Dim nNutrs As Integer = data.nNutrs
      
      Dim cost As IObjective = model.AddMinimize()
      Dim constraint(nNutrs) As IRange
      
      Dim i As Integer
      For i = 0 To nNutrs - 1
         constraint(i) = model.AddRange(data.nutrMin(i), data.nutrMax(i))
      Next i
      Dim j As Integer
      For j = 0 To nFoods - 1
         Dim col As Column = model.Column(cost, data.foodCost(j))
         For i = 0 To nNutrs - 1
            col = col.And(model.Column(constraint(i), data.nutrPerFood(i)(j)))
         Next i
         Buy(j) = model.NumVar(col, data.foodMin(j), data.foodMax(j), type)
      Next j 
   End Sub 'BuildModelByColumn
   
   Public Overloads Shared Sub Main(ByVal args() As String)

        Try
         Dim filename As String = "../../../../examples/data/diet.dat"
         Dim byColumn As Boolean = False
         Dim varType As NumVarType = NumVarType.Float

         Dim i As Integer
         For i = 0 To args.Length - 1
            If args(i).ToCharArray()(0) = "-"c Then
               Select Case args(i).ToCharArray()(1)
                   Case "c"c
                      byColumn = True
                   Case "i"c
                      varType = NumVarType.Int
                   Case Else
                      Usage()
                      Return
               End Select
            Else
               filename = args(i)
               Exit For
            End If
         Next i

         Dim data As New Data(filename)
         Dim nFoods As Integer = data.nFoods
         Dim nNutrs As Integer = data.nNutrs

         ' Build model
         Dim cplex As New Cplex()
         Dim Buy(nFoods - 1) As INumVar

         If byColumn Then
            BuildModelByColumn(cplex, data, Buy, varType)
         Else
            BuildModelByRow(cplex, data, Buy, varType)
         End If 

         ' Solve model
         If cplex.Solve() Then
            System.Console.WriteLine()
            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine()
            System.Console.WriteLine((" cost = " & cplex.ObjValue))
            For i = 0 To nFoods - 1
               System.Console.WriteLine((" Buy" & i & " = " & cplex.GetValue(Buy(i))))
            Next i
            System.Console.WriteLine()
         End If
         cplex.End()
      Catch ex As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert Error: " + ex.ToString))
      Catch ex As InputDataReader.InputDataReaderException
         System.Console.WriteLine(("Data Error: " + ex.ToString))
      Catch ex As System.IO.IOException
         System.Console.WriteLine(("IO Error: " + ex.ToString))
      End Try
   End Sub 'Main

   Friend Shared Sub Usage()
      System.Console.WriteLine(" ")
      System.Console.WriteLine("usage: Diet [options] <data file>")
      System.Console.WriteLine("options: -c  build model by column")
      System.Console.WriteLine("         -i  use integer variables")
      System.Console.WriteLine(" ")
   End Sub 'Usage
End Class 'Diet

'  Sample output
'
'Solution status = Optimal
'
'cost   = 14.8557
'  Buy0 = 4.38525
'  Buy1 = 0
'  Buy2 = 0
'  Buy3 = 0
'  Buy4 = 0
'  Buy5 = 6.14754
'  Buy6 = 0
'  Buy7 = 3.42213
'  Buy8 = 0
'
