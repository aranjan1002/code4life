' ---------------------------------------------------------------------------
' File: MixBlend.vb
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
' Problem Description
' -------------------
' 
' Goal is to blend four sources to produce an alloy: pure metal, raw
' materials, scrap, and ingots.
' 
' Each source has a cost.
' Each source is made up of elements in different proportions.
' Ingots are discrete, so they are modeled as integers.
' Alloy has minimum and maximum proportion of each element.
' 
' Minimize cost of producing a requested quantity of alloy.
' 
' --------------------------------------------------------------------------
Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class MixBlend
   Friend Shared _nbElements As Integer = 3
   Friend Shared _nbRaw      As Integer = 2
   Friend Shared _nbScrap    As Integer = 2
   Friend Shared _nbIngot    As Integer = 1
   Friend Shared _alloy      As Double = 71.0
   
   Friend Shared _cm() As Double = {22.0, 10.0, 13.0}
   Friend Shared _cr() As Double = {6.0, 5.0}
   Friend Shared _cs() As Double = {7.0, 8.0}
   Friend Shared _ci() As Double = {9.0}
   Friend Shared _p()  As Double = {0.05, 0.3, 0.6}
   Friend Shared _P1() As Double = {0.1, 0.4, 0.8}
   
   Friend Shared _PRaw()()   As Double = {New Double() {0.2, 0.01}, _
                                          New Double() {0.05, 0.0}, _
                                          New Double() {0.05, 0.3}}
   Friend Shared _PScrap()() As Double = {New Double() {0.0, 0.01}, _
                                          New Double() {0.6, 0.0}, _
                                          New Double() {0.4, 0.7}}
   Friend Shared _PIngot()() As Double = {New Double() {0.1}, _
                                          New Double() {0.45}, _
                                          New Double() {0.45}}
   

   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim cplex As New Cplex()

         Dim m As INumVar() = cplex.NumVarArray(_nbElements, 0.0, System.Double.MaxValue)
         Dim r As INumVar() = cplex.NumVarArray(_nbRaw, 0.0, System.Double.MaxValue)
         Dim s As INumVar() = cplex.NumVarArray(_nbScrap, 0.0, System.Double.MaxValue)
         Dim i As INumVar() = cplex.IntVarArray(_nbIngot, 0, System.Int32.MaxValue)
         Dim e(_nbElements - 1) As INumVar

         ' Objective Function: Minimize Cost
         cplex.AddMinimize(cplex.Sum(cplex.ScalProd(_cm, m), cplex.ScalProd(_cr, r), cplex.ScalProd(_cs, s), cplex.ScalProd(_ci, i)))

         ' Min and max quantity of each element in alloy
         Dim j As Integer
         For j = 0 To _nbElements - 1
            e(j) = cplex.NumVar(_p(j) * _alloy, _P1(j) * _alloy)
         Next j

         ' Constraint: produce requested quantity of alloy
         cplex.AddEq(cplex.Sum(e), _alloy)

         ' Constraints: Satisfy element quantity requirements for alloy
         For j = 0 To _nbElements - 1
            cplex.AddEq(e(j), cplex.Sum(m(j), cplex.ScalProd(_PRaw(j), r), cplex.ScalProd(_PScrap(j), s), cplex.ScalProd(_PIngot(j), i)))
         Next j

         If cplex.Solve() Then
            If cplex.GetStatus().Equals(cplex.Status.Infeasible) Then
               System.Console.WriteLine("No feasible solution found")
               Return
            End If

            Dim mVals As Double() = cplex.GetValues(m)
            Dim rVals As Double() = cplex.GetValues(r)
            Dim sVals As Double() = cplex.GetValues(s)
            Dim iVals As Double() = cplex.GetValues(i)
            Dim eVals As Double() = cplex.GetValues(e)

            ' Print results
            System.Console.WriteLine(("Cost:" & cplex.ObjValue))

            System.Console.WriteLine("Pure metal:")
            For j = 0 To _nbElements - 1
               System.Console.WriteLine(("(" & j & ") " & mVals(j)))
            Next j
            System.Console.WriteLine("Raw material:")
            For j = 0 To _nbRaw - 1
               System.Console.WriteLine(("(" & j & ") " & rVals(j)))
            Next j
            System.Console.WriteLine("Scrap:")
            For j = 0 To _nbScrap - 1
               System.Console.WriteLine(("(" & j & ") " & sVals(j)))
            Next j
            System.Console.WriteLine("Ingots : ")
            For j = 0 To _nbIngot - 1
               System.Console.WriteLine(("(" & j & ") " & iVals(j)))
            Next j
            System.Console.WriteLine("Elements:")
            For j = 0 To _nbElements - 1
               System.Console.WriteLine(("(" & j & ") " & eVals(j)))
            Next j
         End If
         cplex.End()
      Catch exc As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + exc.ToString + "' caught"))
      End Try
   End Sub 'Main
End Class 'MixBlend

'
'Cost : 653.61
'Pure metal :
'0) 0.0466667
'1) 0
'2) 0
'Raw material :
'0) 0
'1) 0
'Scrap : 
'0) 17.4167
'1) 30.3333
'Ingots : 
'0) 32
'Elements : 
'0) 3.55
'1) 24.85
'2) 42.6
'
