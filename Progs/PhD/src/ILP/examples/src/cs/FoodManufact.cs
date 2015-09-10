// --------------------------------------------------------------------------
// File: FoodManufact.cs
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
// FoodManufact.cs - An implementation of an example from H.P.
//                   Williams' book Model Building in Mathematical
//                   Programming.  This example solves a
//                   food production planning problem.  It
//                   demonstrates the use of CPLEX's
//                   linearization capability.
// 

using ILOG.Concert;
using ILOG.CPLEX;

public class FoodManufact {
   internal static int v1 = 0;
   internal static int v2 = 1;
   internal static int o1 = 2;
   internal static int o2 = 3;
   internal static int o3 = 4;

   internal static double[][] cost =
             {new double[] {110.0, 120.0, 130.0, 110.0, 115.0},
              new double[] {130.0, 130.0, 110.0,  90.0, 115.0},
              new double[] {110.0, 140.0, 130.0, 100.0,  95.0},
              new double[] {120.0, 110.0, 120.0, 120.0, 125.0},
              new double[] {100.0, 120.0, 150.0, 110.0, 105.0},
              new double[] { 90.0, 100.0, 140.0,  80.0, 135.0}};

   public static void Main (string[] args) {
      int nMonths   = cost.Length;
      int nProducts = cost[0].Length;

      try {
         Cplex cplex = new Cplex();

         INumVar[]   produce = cplex.NumVarArray(nMonths, 0, System.Double.MaxValue);
         INumVar[][] use   = new INumVar[nMonths][];
         INumVar[][] buy   = new INumVar[nMonths][];
         INumVar[][] store = new INumVar[nMonths][];

         for (int i = 0; i < nMonths; i++) {
            use[i]   = cplex.NumVarArray(nProducts, 0.0, System.Double.MaxValue);
            buy[i]   = cplex.NumVarArray(nProducts, 0.0, System.Double.MaxValue);
            store[i] = cplex.NumVarArray(nProducts, 0.0, 1000.0);
         }

         for (int p = 0; p < nProducts; p++) {
           store[nMonths-1][p].LB = 500.0;
           store[nMonths-1][p].UB = 500.0;
         }

         INumExpr profit = cplex.NumExpr();
         for (int i = 0; i < nMonths; i++) {
            // Not more than 200 tons of vegetable oil can be refined
            cplex.AddLe(cplex.Sum(use[i][v1], use[i][v2]), 200.0);

            // Not more than 250 tons of non-vegetable oil can be refined
            cplex.AddLe(cplex.Sum(use[i][o1], use[i][o2], use[i][o3]), 250.0);

            // Constraints on food composition
            cplex.AddLe(cplex.Prod(3.0,produce[i]),
                        cplex.Sum(cplex.Prod(8.8, use[i][v1]),
                                  cplex.Prod(6.1, use[i][v2]),
                                  cplex.Prod(2.0, use[i][o1]),
                                  cplex.Prod(4.2, use[i][o2]),
                                  cplex.Prod(5.0, use[i][o3])));
            cplex.AddGe(cplex.Prod(6.0, produce[i]),
                        cplex.Sum(cplex.Prod(8.8, use[i][v1]),
                                  cplex.Prod(6.1, use[i][v2]),
                                  cplex.Prod(2.0, use[i][o1]),
                                  cplex.Prod(4.2, use[i][o2]),
                                  cplex.Prod(5.0, use[i][o3])));
            cplex.AddEq(produce[i], cplex.Sum(use[i]));

            // Raw oil can be stored for later use
            if (i == 0) {
               for (int p = 0; p < nProducts; p++)
                  cplex.AddEq(cplex.Sum(500.0, buy[i][p]),
                              cplex.Sum(use[i][p], store[i][p]));
            }
            else {
               for (int p = 0; p < nProducts; p++)
                 cplex.AddEq(cplex.Sum(store[i-1][p], buy[i][p]),
                             cplex.Sum(use[i][p], store[i][p]));
            }

            // Logical constraints:
            // The food cannot use more than 3 oils
            // (or at least two oils must not be used)
            cplex.AddGe(cplex.Sum(cplex.Eq(use[i][v1], 0),
                                  cplex.Eq(use[i][v2], 0),
                                  cplex.Eq(use[i][o1], 0),
                                  cplex.Eq(use[i][o2], 0),
                                  cplex.Eq(use[i][o3], 0)), 2);

            // When an oil is used, the quantity must be at least 20 tons
            for (int p = 0; p < nProducts; p++)
               cplex.Add(cplex.Or(cplex.Eq(use[i][p],  0),
                                  cplex.Ge(use[i][p], 20)));

            // If products v1 or v2 are used, then product o3 is also used
            cplex.Add(cplex.IfThen(cplex.Or(cplex.Ge(use[i][v1], 20),
                                            cplex.Ge(use[i][v2], 20)),
                                   cplex.Ge(use[i][o3], 20)));

            // Objective function
            profit = cplex.Sum (profit, cplex.Prod(150, produce[i]));
            profit = cplex.Diff(profit, cplex.ScalProd(cost[i], buy[i]));
            profit = cplex.Diff(profit, cplex.Prod(5, cplex.Sum(store[i])));
         }

         cplex.AddMaximize(profit);

         if ( cplex.Solve() ) {
            System.Console.WriteLine(" Maximum profit = " + cplex.ObjValue);
            for (int i = 0; i < nMonths; i++) {
               System.Console.WriteLine(" Month {0}", i);

               System.Console.Write("  . buy   ");
               for (int p = 0; p < nProducts; p++)
                  System.Console.Write("{0,8:F2} ", cplex.GetValue(buy[i][p]));
               System.Console.WriteLine();

               System.Console.Write("  . use   ");
               for (int p = 0; p < nProducts; p++)
                  System.Console.Write("{0,8:F2} ", cplex.GetValue(use[i][p]));
               System.Console.WriteLine();

               System.Console.Write("  . store ");
               for (int p = 0; p < nProducts; p++)
                  System.Console.Write("{0,8:F2} ", cplex.GetValue(store[i][p]));
               System.Console.WriteLine();
            }
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught: " + e);
      }
   }
}
