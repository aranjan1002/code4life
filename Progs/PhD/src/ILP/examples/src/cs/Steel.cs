// --------------------------------------------------------------------------
// File: Steel.cs
// Version 12.2  
// --------------------------------------------------------------------------
// Licensed Materials - Property of IBM
// 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
// Copyright IBM Corporation 2003, 2010. All Rights Reserved.
//
// US Government Users Restricted Rights - Use, duplication or
// disclosure restricted by GSA ADP Schedule Contract with
// IBM Corp.
// --------------------------------------------------------------------------
// 
// This example is an implementation of the model called "steelT.mod"
// in the AMPL book by Fourer, Gay and Kernighan.  In the AMPL
// example, a multiperiod production model is given, with data
// for 4 weeks.  
//
// The parameters for the model are:
//   nProd              the number of products
//   nTime              the number of time periods
//   rate[p]            rate of production for product p
//   inv0[p]            initial inventoryfor product p
//   avail[t]           hours available in time period t
//   market[p][t]       demand for product p in time period t
//   prodcost[p]        production cost per unit of product p 
//   invcost[p]         inventory cost per unit of product p
//   revenue[p][t]      revenue per unit of product p in time period t
//
// The decision variables of the model are:
//   Make[p][t]         amount produced of product p in time period t
//   Inv[p][t]          amount inventoried of product p in time period t
//   Sell[p][t]         amount sold of product p in time period t
// 
// The objective function is to
// 
// maximize  sum(over p,t) (  revenue[p][t] * Sell[p][t]
//                          - prodcost[p]   * Make[p][t]
//                          - invcost[p]    * Inv[p][t]  )
//
// The constraints are
// 
//  For each t:   (time availability constraint)
//      sum(over p)  ( (1/rate[p]) * Make[p][t] ) <= avail[t]
// 
//  For each pair (p,t) (t>0): (balance constraint)
//      Make[p][t] + Inv[p][t-1] - Sell[p][t] - Inv[p][t] = 0
//
//  For each p, (t=0): (balance constraint)
//      Make[p][0] - Sell[p][0] - Inv[p][0] = -inv0[p]
//
//  The bounds on the variables are:
//    All variables are nonnegative ( >= 0 )
//    For each (p,t),
//       Sell[p][t] <= market[p][t]
//    All other variables have infinite upper bounds.

using ILOG.Concert;
using ILOG.CPLEX;

public class Steel {
   internal static int _nProd;
   internal static int _nTime;
   
   internal static double[] _avail;
   internal static double[] _rate;
   internal static double[] _inv0;
   internal static double[] _prodCost;
   internal static double[] _invCost;
   
   internal static double[][] _revenue;
   internal static double[][] _market;

   internal static void ReadData(string fileName) {
      InputDataReader reader = new InputDataReader(fileName);
      
      _avail    = reader.ReadDoubleArray();
      _rate     = reader.ReadDoubleArray();
      _inv0     = reader.ReadDoubleArray();
      _prodCost = reader.ReadDoubleArray();
      _invCost  = reader.ReadDoubleArray();
      _revenue  = reader.ReadDoubleArrayArray();
      _market   = reader.ReadDoubleArrayArray();
      
      _nProd = _rate.Length;
      _nTime = _avail.Length;
   }
   
   public static void Main(string[] args) {
      try {
         string filename = "../../../../examples/data/steel.dat";
         if ( args.Length > 0 )
            filename = args[0];
         ReadData(filename);
         
         Cplex cplex = new Cplex();
       
         // VARIABLES
         INumVar[][] Make = new INumVar[_nProd][];
         for (int p = 0; p < _nProd; p++) {
            Make[p] = cplex.NumVarArray(_nTime, 0.0, System.Double.MaxValue);
         }
       
         INumVar[][] Inv = new INumVar[_nProd][];
         for (int p = 0; p < _nProd; p++) {
            Inv[p] = cplex.NumVarArray(_nTime, 0.0, System.Double.MaxValue);
         }
       
         INumVar[][] Sell = new INumVar[_nProd][];
         for (int p = 0; p < _nProd; p++) {
             Sell[p] = new INumVar[_nTime];
            for (int t = 0; t < _nTime; t++) {
               Sell[p][t] = cplex.NumVar(0.0, _market[p][t]);
            }
         }
       
         // OBJECTIVE
         ILinearNumExpr TotalRevenue  = cplex.LinearNumExpr();
         ILinearNumExpr TotalProdCost = cplex.LinearNumExpr();
         ILinearNumExpr TotalInvCost  = cplex.LinearNumExpr();
         
         for (int p = 0; p < _nProd; p++) {
            for (int t = 1; t < _nTime; t++) {
               TotalRevenue.AddTerm (_revenue[p][t], Sell[p][t]);
               TotalProdCost.AddTerm(_prodCost[p], Make[p][t]);
               TotalInvCost.AddTerm (_invCost[p], Inv[p][t]);
            }
         }
           
         cplex.AddMaximize(cplex.Diff(TotalRevenue, 
                                      cplex.Sum(TotalProdCost, TotalInvCost)));
       
         // TIME AVAILABILITY CONSTRAINTS
       
         for (int t = 0; t < _nTime; t++) {
            ILinearNumExpr availExpr = cplex.LinearNumExpr();
            for (int p = 0; p < _nProd; p++) {
               availExpr.AddTerm(1.0/_rate[p], Make[p][t]);
            }
            cplex.AddLe(availExpr, _avail[t]);
         }
       
         // MATERIAL BALANCE CONSTRAINTS
       
         for (int p = 0; p < _nProd; p++) {
            cplex.AddEq(cplex.Sum(Make[p][0], _inv0[p]), 
                        cplex.Sum(Sell[p][0], Inv[p][0]));
            for (int t = 1; t < _nTime; t++) {
               cplex.AddEq(cplex.Sum(Make[p][t], Inv[p][t-1]), 
                           cplex.Sum(Sell[p][t], Inv[p][t]));
            }
         }
       
         cplex.ExportModel("steel.lp");
       
         if ( cplex.Solve() ) {
            System.Console.WriteLine();
            System.Console.WriteLine("Total Profit = " + cplex.ObjValue);
          
            System.Console.WriteLine();
            System.Console.WriteLine("\tp\tt\tMake\tInv\tSell");
          
            for (int p = 0; p < _nProd; p++) {
               for (int t = 0; t < _nTime; t++) {
                  System.Console.WriteLine("\t" + p +"\t" + t +"\t" + cplex.GetValue(Make[p][t]) +
                                           "\t" + cplex.GetValue(Inv[p][t]) +"\t" + cplex.GetValue(Sell[p][t]));
               }
            }
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception exc) {
         System.Console.WriteLine("Concert exception '" + exc + "' caught");
      }
      catch (System.IO.IOException exc) {
         System.Console.WriteLine("Error reading file " + args[0] + ": " + exc);
      }
      catch (InputDataReader.InputDataReaderException exc) {
         System.Console.WriteLine(exc);
      }
   }
}

/*
Total Profit = 515033.00000000006

        p       t       Make    Inv     Sell
        0       0       0.0     10.0    0.0
        0       1       5990.0  0.0     6000.0
        0       2       6000.0  0.0     6000.0
        0       3       1400.0  0.0     1400.0
        0       4       2000.0  0.0     2000.0
        1       0       0.0     0.0     0.0
        1       1       1407.0  1100.0  307.0
        1       2       1400.0  0.0     2500.0
        1       3       3500.0  0.0     3500.0
        1       4       4200.0  0.0     4200.0
*/
