' ---------------------------------------------------------------------------
' File: Steel.vb
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
' This example is an implementation of the model called "steelT.mod"
' in the AMPL book by Fourer, Gay and Kernighan.  In the AMPL
' example, a multiperiod production model is given, with data
' for 4 weeks.  
'
' The parameters for the model are:
'   nProd              the number of products
'   nTime              the number of time periods
'   rate[p]            rate of production for product p
'   inv0[p]            initial inventoryfor product p
'   avail[t]           hours available in time period t
'   market[p][t]       demand for product p in time period t
'   prodcost[p]        production cost per unit of product p 
'   invcost[p]         inventory cost per unit of product p
'   revenue[p][t]      revenue per unit of product p in time period t
'
' The decision variables of the model are:
'   Make[p][t]         amount produced of product p in time period t
'   Inv[p][t]          amount inventoried of product p in time period t
'   Sell[p][t]         amount sold of product p in time period t
' 
' The objective function is to
' 
' maximize  sum(over p,t) (  revenue[p][t] * Sell[p][t]
'                          - prodcost[p]   * Make[p][t]
'                          - invcost[p]    * Inv[p][t]  )
'
' The constraints are
' 
'  For each t:   (time availability constraint)
'      sum(over p)  ( (1/rate[p]) * Make[p][t] ) <= avail[t]
' 
'  For each pair (p,t) (t>0): (balance constraint)
'      Make[p][t] + Inv[p][t-1] - Sell[p][t] - Inv[p][t] = 0
'
'  For each p, (t=0): (balance constraint)
'      Make[p][0] - Sell[p][0] - Inv[p][0] = -inv0[p]
'
'  The bounds on the variables are:
'    All variables are nonnegative ( >= 0 )
'    For each (p,t),
'       Sell[p][t] <= market[p][t]
'    All other variables have infinite upper bounds.
Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class Steel
   Friend Shared _nProd As Integer
   Friend Shared _nTime As Integer
   
   Friend Shared _avail() As Double
   Friend Shared _rate() As Double
   Friend Shared _inv0() As Double
   Friend Shared _prodCost() As Double
   Friend Shared _invCost() As Double
   
   Friend Shared _revenue()() As Double
   Friend Shared _market()() As Double
   
   
   Friend Shared Sub ReadData(fileName As String)
      Dim reader As New InputDataReader(fileName)
      
      _avail = reader.ReadDoubleArray()
      _rate = reader.ReadDoubleArray()
      _inv0 = reader.ReadDoubleArray()
      _prodCost = reader.ReadDoubleArray()
      _invCost = reader.ReadDoubleArray()
      _revenue = reader.ReadDoubleArrayArray()
      _market = reader.ReadDoubleArrayArray()
      
      _nProd = _rate.Length
      _nTime = _avail.Length
   End Sub 'ReadData
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim filename As String = "../../../../examples/data/steel.dat"
         If args.Length > 0 Then
            filename = args(0)
         End If
         ReadData(filename)

         Dim cplex As New Cplex()

         ' VARIABLES
         Dim Make(_nProd)() As INumVar
         Dim p As Integer
         Dim t As Integer
         For p = 0 To _nProd - 1
            Make(p) = cplex.NumVarArray(_nTime, 0.0, System.Double.MaxValue)
         Next p

         Dim Inv(_nProd)() As INumVar
         For p = 0 To _nProd - 1
            Inv(p) = cplex.NumVarArray(_nTime, 0.0, System.Double.MaxValue)
         Next p

         Dim Sell(_nProd)() As INumVar
         For p = 0 To _nProd - 1
            Sell(p) = New INumVar(_nTime) {}
            For t = 0 To _nTime - 1
               Sell(p)(t) = cplex.NumVar(0.0, _market(p)(t))
            Next t
         Next p

         ' OBJECTIVE
         Dim TotalRevenue As ILinearNumExpr = cplex.LinearNumExpr()
         Dim TotalProdCost As ILinearNumExpr = cplex.LinearNumExpr()
         Dim TotalInvCost As ILinearNumExpr = cplex.LinearNumExpr()

         For p = 0 To _nProd - 1
            For t = 1 To _nTime - 1
               TotalRevenue.AddTerm(_revenue(p)(t), Sell(p)(t))
               TotalProdCost.AddTerm(_prodCost(p), Make(p)(t))
               TotalInvCost.AddTerm(_invCost(p), Inv(p)(t))
            Next t
         Next p

         cplex.AddMaximize(cplex.Diff(TotalRevenue, cplex.Sum(TotalProdCost, TotalInvCost)))

         ' TIME AVAILABILITY CONSTRAINTS
         For t = 0 To _nTime - 1
            Dim availExpr As ILinearNumExpr = cplex.LinearNumExpr()
            For p = 0 To _nProd - 1
               availExpr.AddTerm(1.0 / _rate(p), Make(p)(t))
            Next p
            cplex.AddLe(availExpr, _avail(t))
         Next t

         ' MATERIAL BALANCE CONSTRAINTS
         For p = 0 To _nProd - 1
            cplex.AddEq(cplex.Sum(Make(p)(0), _inv0(p)), cplex.Sum(Sell(p)(0), Inv(p)(0)))
            For t = 1 To _nTime - 1
               cplex.AddEq(cplex.Sum(Make(p)(t), Inv(p)((t - 1))), cplex.Sum(Sell(p)(t), Inv(p)(t)))
            Next t
         Next p

         cplex.ExportModel("steel.lp")

         If cplex.Solve() Then
            System.Console.WriteLine()
            System.Console.WriteLine(("Total Profit = " & cplex.ObjValue))

            System.Console.WriteLine()
            System.Console.WriteLine(ControlChars.Tab + "p" + _
                                     ControlChars.Tab + "t" + _
                                     ControlChars.Tab + "Make" + _
                                     ControlChars.Tab + "Inv" + _
                                     ControlChars.Tab + "Sell")

            For p = 0 To _nProd - 1
               For t = 0 To _nTime - 1
                  System.Console.WriteLine(ControlChars.Tab & p & _
                                           ControlChars.Tab & t & _
                                           ControlChars.Tab & cplex.GetValue(Make(p)(t)) & _
                                           ControlChars.Tab & cplex.GetValue(Inv(p)(t)) & _
                                           ControlChars.Tab & cplex.GetValue(Sell(p)(t)))
               Next t
            Next p
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
End Class 'Steel

'
'Total Profit = 515033.00000000006
'
'        p       t       Make    Inv     Sell
'        0       0       0.0     10.0    0.0
'        0       1       5990.0  0.0     6000.0
'        0       2       6000.0  0.0     6000.0
'        0       3       1400.0  0.0     1400.0
'        0       4       2000.0  0.0     2000.0
'        1       0       0.0     0.0     0.0
'        1       1       1407.0  1100.0  307.0
'        1       2       1400.0  0.0     2500.0
'        1       3       3500.0  0.0     3500.0
'        1       4       4200.0  0.0     4200.0
'
