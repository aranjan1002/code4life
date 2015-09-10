' ---------------------------------------------------------------------------
' File: CutStock.vb
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


Friend Class CutStock
   Friend Shared RC_EPS As Double = 1E-06
   
   ' Data of the problem
   Friend Shared _rollWidth As Double
   Friend Shared _size() As Double
   Friend Shared _amount() As Double
   
   
   Friend Shared Sub ReadData(fileName As String)
      Dim reader As New InputDataReader(fileName)
      
      _rollWidth = reader.ReadDouble()
      _size = reader.ReadDoubleArray()
      _amount = reader.ReadDoubleArray()
   End Sub 'ReadData
   
   
   Friend Shared Sub Report1(cutSolver As Cplex, Cut As System.Collections.ArrayList, Fill() As IRange)
      System.Console.WriteLine()
      System.Console.WriteLine(("Using " & cutSolver.ObjValue & " rolls"))
      
      System.Console.WriteLine()
      Dim j As Integer
      For j = 0 To Cut.Count - 1
            System.Console.WriteLine(("  Cut" & j & " = " & cutSolver.GetValue(CType(Cut(j), INumVar))))
      Next j
      System.Console.WriteLine()
      
      Dim i As Integer
      For i = 0 To Fill.Length - 1
            System.Console.WriteLine(("  Fill" & i & " = " & cutSolver.GetDual(Fill(i))))
      Next i
      System.Console.WriteLine()
   End Sub 'Report1
   
   
   Friend Shared Sub Report2(patSolver As Cplex, Use() As INumVar)
      System.Console.WriteLine()
        System.Console.WriteLine(("Reduced cost is " & patSolver.ObjValue))
      
      System.Console.WriteLine()
      If patSolver.ObjValue <= - RC_EPS Then
         Dim i As Integer
         For i = 0 To Use.Length - 1
            System.Console.WriteLine(("  Use" & i & " = " & patSolver.GetValue(Use(i))))
         Next i
         System.Console.WriteLine()
      End If
   End Sub 'Report2
   
   
   Friend Shared Sub Report3(cutSolver As Cplex, Cut As System.Collections.ArrayList)
      System.Console.WriteLine()
        System.Console.WriteLine(("Best integer solution uses " & cutSolver.ObjValue & " rolls"))
      System.Console.WriteLine()
      Dim j As Integer
      For j = 0 To Cut.Count - 1
            System.Console.WriteLine(("  Cut" & j & " = " & cutSolver.GetValue(CType(Cut(j), INumVar))))
      Next j
   End Sub 'Report3
   

   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim datafile As String = "../../../../examples/data/cutstock.dat"
         If args.Length > 0 Then
            datafile = args(0)
         End If
         ReadData(datafile)

         '/// CUTTING-OPTIMIZATION PROBLEM ///
         Dim cutSolver As New Cplex()

         Dim RollsUsed As IObjective = cutSolver.AddMinimize()
         Dim Fill(_amount.Length - 1) As IRange
         Dim f As Integer
         For f = 0 To _amount.Length - 1
            Fill(f) = cutSolver.AddRange(_amount(f), System.Double.MaxValue)
         Next f

         Dim Cut As New System.Collections.ArrayList()

         Dim nWdth As Integer = _size.Length
         Dim j As Integer

         For j = 0 To nWdth - 1
            Cut.Add(cutSolver.NumVar(cutSolver.Column(RollsUsed, 1.0).And(cutSolver.Column(Fill(j), _rollWidth \ _size(j))), _
                                     0.0, System.Double.MaxValue))
         Next j

         cutSolver.SetParam(Cplex.IntParam.RootAlg, Cplex.Algorithm.Primal)

         '/// PATTERN-GENERATION PROBLEM ///
         Dim patSolver As New Cplex()

         Dim ReducedCost As IObjective = patSolver.AddMinimize()
         Dim Use As INumVar() = patSolver.NumVarArray(nWdth, 0.0, System.Double.MaxValue, NumVarType.Int)
         patSolver.AddRange(-System.Double.MaxValue, patSolver.ScalProd(_size, Use), _rollWidth)

         '/// COLUMN-GENERATION PROCEDURE ///
         Dim newPatt(nWdth) As Double

         Do
            '/// OPTIMIZE OVER CURRENT PATTERNS ///
            cutSolver.Solve()
            Report1(cutSolver, Cut, Fill)

            '/// FIND AND ADD A NEW PATTERN ///
            Dim price As Double() = cutSolver.GetDuals(Fill)
            ReducedCost.Expr = patSolver.Diff(1.0, patSolver.ScalProd(Use, price))

            patSolver.Solve()
            Report2(patSolver, Use)

            If patSolver.ObjValue > -RC_EPS Then
               Exit Do 
            End If
            newPatt = patSolver.GetValues(Use)

            Dim column As Column = cutSolver.Column(RollsUsed, 1.0)
            Dim p As Integer
            For p = 0 To newPatt.Length - 1
               column = column.And(cutSolver.Column(Fill(p), newPatt(p)))
            Next p
            Cut.Add(cutSolver.NumVar(column, 0.0, System.Double.MaxValue))
         Loop

         Dim i As Integer
         For i = 0 To Cut.Count - 1
            cutSolver.Add(cutSolver.Conversion(CType(Cut(i), INumVar), NumVarType.Int))
         Next i

         cutSolver.Solve()
         Report3(cutSolver, Cut)

         cutSolver.End()
         patSolver.End()
      Catch exc As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + exc.ToString + "' caught"))
      Catch exc As System.IO.IOException
         System.Console.WriteLine(("Error reading file " + args(0) + ": " + exc.ToString))
      Catch exc As InputDataReader.InputDataReaderException
         System.Console.WriteLine(exc)
      End Try
   End Sub 'Main
End Class 'CutStock


' Example Input file:
'115
'[25, 40, 50, 55, 70]
'[50, 36, 24, 8, 30]
'
