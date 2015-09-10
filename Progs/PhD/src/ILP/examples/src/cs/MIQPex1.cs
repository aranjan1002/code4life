// --------------------------------------------------------------------------
// File: MIQPex1.cs
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
// MIQPex1.cs - Entering and optimizing a MIQP problem

using ILOG.Concert;
using ILOG.CPLEX;
using System;


public class MIQPex1 {
   public static void Main(string[] args) {
      try {
         Cplex cplex = new Cplex();
         ILPMatrix lp = PopulateByRow(cplex);

         cplex.SetParam(Cplex.IntParam.RootAlg, Cplex.Algorithm.Dual);


         if ( cplex.Solve() ) {
            double[] x     = cplex.GetValues(lp);
            double[] slack = cplex.GetSlacks(lp);

            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);

            int nvars = x.Length;
            for (int j = 0; j < nvars; ++j) {
               System.Console.WriteLine("Variable   " + j +
                                        ": Value = " + x[j]);
            }

            int ncons = slack.Length;
            for (int i = 0; i < ncons; ++i) {
               System.Console.WriteLine("Constraint " + i +
                                        ": Slack = " + slack[i]);
            }

            cplex.ExportModel("miqpex1.lp");
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception '" + e + "' caught");
      }
   }

   static ILPMatrix PopulateByRow(IMPModeler model) {
      ILPMatrix lp = model.AddLPMatrix();

      double[]  lb = {0.0, 0.0, 0.0};
      double[]  ub = {40.0, Double.MaxValue, Double.MaxValue};
      INumVar[] x  = model.NumVarArray(model.ColumnArray(lp, 3), lb, ub);
      INumVar   y  = model.IntVar(model.Column(lp), 0, 3);

      // - x0 +   x1 + x2 + 10*y <= 20
      //   x0 - 3*x1 + x2        <= 30
      double[]   lhs = {-Double.MaxValue, -Double.MaxValue};
      double[]   rhs = {20.0, 30.0};
      double[][] val = {new double[] {-1.0,  1.0,  1.0, 10.0},
                        new double[] { 1.0, -3.0,  1.0}};
      int[][]    ind = {new int[] {0, 1, 2, 3},
                        new int[] {0, 1, 2}};
      lp.AddRows(lhs, rhs, ind, val);
      // x1 + 3.5*y = 0
      lp.AddRow(model.Eq(model.Diff(x[1], model.Prod(3.5, y)), 0.0));

      // Q = 0.5 ( 33*x0*x0 + 22*x1*x1 + 11*x2*x2 - 12*x0*x1 - 23*x1*x2 )
      INumExpr x00 = model.Prod( 33.0, model.Square(x[0]));
      INumExpr x11 = model.Prod( 22.0, model.Square(x[1]));
      INumExpr x22 = model.Prod( 11.0, model.Square(x[2]));
      INumExpr x01 = model.Prod(-12.0, model.Prod(x[0], x[1]));
      INumExpr x12 = model.Prod(-23.0, model.Prod(x[1], x[2]));
      INumExpr Q   = model.Prod(0.5, model.Sum(x00, x11, x22, x01, x12));

      // maximize x0 + 2*x1 + 3*x2 + Q
      double[] objvals = {1.0, 2.0, 3.0};
      model.Add(model.Maximize(model.Diff(model.ScalProd(x, objvals), Q)));

      return (lp);
   }
}
