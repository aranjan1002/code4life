// --------------------------------------------------------------------------
// File: LPex3.cs
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
// LPex3.cs, example of adding constraints to solve a problem
//
// Modified example from Chvatal, "Linear Programming", Chapter 26.
//   minimize  c*x
//   subject to  Hx = d
//               Ax = b
//               l <= x <= u
//   where
//
//   H = (  0  0  0  0  0  0  0 -1 -1 -1  0  0 )  d = ( -1 )
//       (  1  0  0  0  0  1  0  1  0  0  0  0 )      (  4 )
//       (  0  1  0  1  0  0  1  0  1  0  0  0 )      (  1 )
//       (  0  0  1  0  1  0  0  0  0  1  0  0 )      (  1 )
//       (  0  0  0  0  0 -1 -1  0  0  0 -1  1 )      ( -2 )
//       (  0  0  0 -1 -1  0  1  0  0  0  1  0 )      ( -2 )
//       ( -1 -1 -1  0  0  0  0  0  0  0  0 -1 )      ( -1 )
//
//   A = (  0  0  0  0  0  0  0  0  0  0  2  5 )  b = (  2 )
//       (  1  0  1  0  0  1  0  0  0  0  0  0 )      (  3 )
//
//   c = (  1  1  1  1  1  1  1  0  0  0  2  2 )
//   l = (  0  0  0  0  0  0  0  0  0  0  0  0 )
//   u = ( 50 50 50 50 50 50 50 50 50 50 50 50 )
//
//  Treat the constraints with A as the complicating constraints, and
//  the constraints with H as the "simple" problem.
//  
//  The idea is to solve the simple problem first, and then add the
//  constraints for the complicating constraints, and solve with dual.

using ILOG.Concert;
using ILOG.CPLEX;


public class LPex3 {
   public static void Main(string[] args) {
      try {
         int ncols = 12;

         Cplex cplex = new Cplex();
         ILPMatrix lp = cplex.AddLPMatrix();

         // add empty corresponding to new variables columns to lp
         INumVar[] x = cplex.NumVarArray(cplex.ColumnArray(lp, ncols), 0, 50);

         // add rows to lp
         double[]   d = {-1.0, 4.0, 1.0, 1.0, -2.0, -2.0, -1.0};
         double[][] valH = {new double[] {-1.0, -1.0, -1.0},
                            new double[] { 1.0,  1.0,  1.0},
                            new double[] { 1.0,  1.0,  1.0,  1.0},
                            new double[] { 1.0,  1.0,  1.0},
                            new double[] {-1.0, -1.0, -1.0,  1.0},
                            new double[] {-1.0, -1.0,  1.0},
                            new double[] {-1.0, -1.0, -1.0, -1.0}};
         int[][]    indH = {new int[] {7, 8, 9},
                            new int[] {0, 5, 7},
                            new int[] {1, 3, 6, 8},
                            new int[] {2, 4, 9},
                            new int[] {5, 6, 10, 11},
                            new int[] {3, 4, 10},
                            new int[] {0, 1, 2, 11}};

         lp.AddRows(d, d, indH, valH);

         // add the objective function
         double[] objvals = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                             1.0, 0.0, 0.0, 0.0, 2.0, 2.0};
         cplex.AddMinimize(cplex.ScalProd(x, objvals));

         // Solve initial problem with the network optimizer
         cplex.SetParam(Cplex.IntParam.RootAlg, Cplex.Algorithm.Network);
         cplex.Solve();
         System.Console.WriteLine("After network optimization, objective is "
                                  + cplex.ObjValue);

         // add rows from matrix A to lp
         double[]   b = {2.0, 3.0};
         double[][] valA = {new double[] {2.0, 5.0},
                            new double[] {1.0, 1.0, 1.0}};
         int[][]    indA = {new int[] {10, 11},
                            new int[] {0, 2, 5}};
         lp.AddRows(b, b, indA, valA);
   
         // Because the problem is dual feasible with the rows added, using
         // the dual simplex method is indicated.
         cplex.SetParam(Cplex.IntParam.RootAlg, Cplex.Algorithm.Dual);
         if ( cplex.Solve() ) {
            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);

            double[] sol = cplex.GetValues(lp);
            for (int j = 0; j < ncols; ++j) {
               System.Console.WriteLine("Variable " + j + ": Value = " + sol[j]);
            }
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
          System.Console.WriteLine("Concert exception '" + e + "' caught");
      }
   }
}
