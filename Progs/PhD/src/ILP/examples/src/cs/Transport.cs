// --------------------------------------------------------------------------
// File: Transport.cs
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

using ILOG.CPLEX;
using ILOG.Concert;

public class Transport {
   public static void Main(string[] args) {
     if ( args.Length < 1 ) {
         System.Console.WriteLine("Usage: Transport <type>");
         System.Console.WriteLine("  type = 0 -> convex  piecewise linear model");
         System.Console.WriteLine("  type = 1 -> concave piecewise linear model");
         return;
     }

     try {
        Cplex cplex = new Cplex();
      
        int nbDemand = 4;
        int nbSupply = 3;
        double[] supply = {1000.0, 850.0, 1250.0};
        double[] demand = {900.0, 1200.0, 600.0, 400.0};
      
        INumVar[][] x = new INumVar[nbSupply][];
        INumVar[][] y = new INumVar[nbSupply][];
      
        for (int i = 0; i < nbSupply; i++) {
           x[i] = cplex.NumVarArray(nbDemand, 0.0, System.Double.MaxValue);
           y[i] = cplex.NumVarArray(nbDemand, 0.0, System.Double.MaxValue);
        } 
      
        for (int i = 0; i < nbSupply; i++)       // supply must meet demand
           cplex.AddEq(cplex.Sum(x[i]), supply[i]);
      
        for (int j = 0; j < nbDemand; j++) {     // demand must meet supply
           ILinearNumExpr v = cplex.LinearNumExpr(); 
           for(int i = 0; i < nbSupply; i++)
              v.AddTerm(1.0, x[i][j]);
           cplex.AddEq(v, demand[j]);
        }      
      
        double[] points;
        double[] slopes;
        if ( args[0].ToCharArray()[0] == '0' ) {         // convex case
           points = new double[] {200.0, 400.0};
           slopes = new double[] { 30.0, 80.0, 130.0};
        }
        else {                                  // concave case
           points = new double[] {200.0, 400.0};
           slopes = new double[] {120.0, 80.0, 50.0};
        }
        for (int i = 0; i < nbSupply; ++i) {
           for (int j = 0; j < nbDemand; ++j) {
              cplex.AddEq(y[i][j],
                          cplex.PiecewiseLinear(x[i][j],
                                                points, slopes, 0.0, 0.0));
           }
        }
      
        ILinearNumExpr expr = cplex.LinearNumExpr();
        for (int i = 0; i < nbSupply; ++i) {
           for (int j = 0; j < nbDemand; ++j) {
              expr.AddTerm(y[i][j], 1.0);   
           }
        }
        
        cplex.AddMinimize(expr);
      
        if ( cplex.Solve() ) {
           System.Console.WriteLine(" - Solution: "); 
           for (int i = 0; i < nbSupply; ++i) {
              System.Console.Write("   " + i + ": ");
              for (int j = 0; j < nbDemand; ++j)
                 System.Console.Write("" + cplex.GetValue(x[i][j]) + "\t");
              System.Console.WriteLine();
           }
           System.Console.WriteLine("   Cost = " + cplex.ObjValue); 
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception exc) {
         System.Console.WriteLine(exc);
      }
   }
}
