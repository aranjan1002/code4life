// --------------------------------------------------------------------------
// File: LPex4.cs
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
// LPex4.cs - Illustrating the CPLEX callback functionality.
// 
// This is a modification of LPex1.cs, where we use a callback
// function to print the iteration info, rather than have CPLEX
// do it.   Note that the actual LP that is solved is slightly
// different to make the output more interesting.

using ILOG.Concert;
using ILOG.CPLEX;


public class LPex4 {
   // Implement the callback as an extension of class
   // Cplex.ContinuousCallback by overloading method main().  In the
   // implementation use protected methods of class Cplex.ContinuousCallback
   // and its super classes, such as Niterations, isFeasible(),
   // ObjValue, and Infeasibility used in this example.
   internal class MyCallback : Cplex.ContinuousCallback {
      public override void Main() {
         System.Console.Write("Iteration " + Niterations + ": ");
         if ( IsFeasible() )
            System.Console.WriteLine("Objective = " + ObjValue);
         else
            System.Console.WriteLine("Infeasibility measure = " + Infeasibility);
      }
   }

   public static void Main(string[] args) {
      try {
         Cplex    cplex = new Cplex();
         ILPMatrix lp   = PopulateByRow(cplex);
       
         // turn off presolve to prevent it from completely solving the model
         // before entering the actual LP optimizer
         cplex.SetParam(Cplex.BooleanParam.PreInd, false);
       
         // turn off logging
         cplex.SetOut(null);
       
         // create and instruct cplex to use callback
         cplex.Use(new MyCallback());
       
         if ( cplex.Solve() ) {
            double[] x     = cplex.GetValues(lp);
            double[] dj    = cplex.GetReducedCosts(lp);
            double[] pi    = cplex.GetDuals(lp);
            double[] slack = cplex.GetSlacks(lp);
          
            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Iterations      = " + cplex.Niterations);
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);
          
            int nvars = x.Length;
            for (int j = 0; j < nvars; ++j) {
               System.Console.WriteLine("Variable   " + j +
                                        ": Value = " + x[j] +
                                        " Reduced cost = " + dj[j]);
            }
          
            int ncons = slack.Length;
            for (int i = 0; i < ncons; ++i) {
               System.Console.WriteLine("Constraint " + i +
                                        ": Slack = " + slack[i] +
                                        " Pi = " + pi[i]);
            }
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception '" + e + "' caught");
      }
   }

   internal static ILPMatrix PopulateByRow(IMPModeler model) {
      ILPMatrix lp = model.AddLPMatrix();
    
      double[]  lb = {0.0, 0.0, 0.0};
      double[]  ub = {40.0, System.Double.MaxValue, System.Double.MaxValue};
      INumVar[] x  = model.NumVarArray(model.ColumnArray(lp, 3), lb, ub);
    
      double[]   lhs = {-System.Double.MaxValue, -System.Double.MaxValue};
      double[]   rhs = {20.0, 30.0};
      double[][] val = {new double[] {-1.0,  1.0,  1.0},
                        new double[] { 1.0, -3.0,  1.0}};
      int[][]    ind = {new int[] {0, 1, 2},
                        new int[] {0, 1, 2}};
      lp.AddRows(lhs, rhs, ind, val);
    
      double[] objvals = {1.0, 2.0, 3.0};
      model.AddMaximize(model.ScalProd(x, objvals));
    
      return (lp);
   }
}
