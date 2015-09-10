// --------------------------------------------------------------------------
// File: LPex1.cs
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
// LPex1.cs - Entering and optimizing an LP problem
//
// Demonstrates different methods for creating a problem.  The user has to
// choose the method on the command line:
//
//    LPex1  -r     generates the problem by adding constraints
//    LPex1  -c     generates the problem by adding variables
//    LPex1  -n     generates the problem by adding expressions
//

using ILOG.Concert;
using ILOG.CPLEX;


public class LPex1 {
   internal static void Usage() {
      System.Console.WriteLine("usage:   LPex1 <option>");
      System.Console.WriteLine("options:       -r   build model row by row");
      System.Console.WriteLine("options:       -c   build model column by column");
      System.Console.WriteLine("options:       -n   build model nonzero by nonzero");
   }

   public static void Main(string[] args) {
      if ( args.Length != 1 || args[0].ToCharArray()[0] != '-' ) {
         Usage();
         return;
      }

      try {
         // Create the modeler/solver object
         Cplex cplex = new Cplex();

         INumVar[][] var = new INumVar[1][];
         IRange[][]  rng = new IRange[1][];

         // Evaluate command line option and call appropriate populate method.
         // The created ranges and variables are returned as element 0 of arrays
         // var and rng.
         switch ( args[0].ToCharArray()[1] ) {
         case 'r': PopulateByRow(cplex, var, rng);
                   break;
         case 'c': PopulateByColumn(cplex, var, rng);
                   break;
         case 'n': PopulateByNonzero(cplex, var, rng);
                   break;
         default:  Usage();
                   return;
         }

         // write model to file
         cplex.ExportModel("lpex1.lp");

         // solve the model and display the solution if one was found
         if ( cplex.Solve() ) {
            double[] x     = cplex.GetValues(var[0]);
            double[] dj    = cplex.GetReducedCosts(var[0]);
            double[] pi    = cplex.GetDuals(rng[0]);
            double[] slack = cplex.GetSlacks(rng[0]);

            cplex.Output().WriteLine("Solution status = " + cplex.GetStatus());
            cplex.Output().WriteLine("Solution value  = " + cplex.ObjValue);

            int nvars = x.Length;
            for (int j = 0; j < nvars; ++j) {
               cplex.Output().WriteLine("Variable   " + j +
                                        ": Value = " + x[j] +
                                        " Reduced cost = " + dj[j]);
            }

            int ncons = slack.Length;
            for (int i = 0; i < ncons; ++i) {
               cplex.Output().WriteLine("Constraint " + i +
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


   // The following methods all populate the problem with data for the following
   // linear program:
   //
   //    Maximize
   //     x1 + 2 x2 + 3 x3
   //    Subject To
   //     - x1 + x2 + x3 <= 20
   //     x1 - 3 x2 + x3 <= 30
   //    Bounds
   //     0 <= x1 <= 40
   //    End
   //
   // using the IMPModeler API

   internal static void PopulateByRow(IMPModeler model,
                                      INumVar[][] var,
                                      IRange[][] rng) {
      double[]  lb      = {0.0, 0.0, 0.0};
      double[]  ub      = {40.0, System.Double.MaxValue, System.Double.MaxValue};
      string[]  varname = {"x1", "x2", "x3"};
      INumVar[] x       = model.NumVarArray(3, lb, ub, varname);
      var[0] = x;

      double[] objvals = {1.0, 2.0, 3.0};
      model.AddMaximize(model.ScalProd(x, objvals));

      rng[0] = new IRange[2];
      rng[0][0] = model.AddLe(model.Sum(model.Prod(-1.0, x[0]),
                                        model.Prod( 1.0, x[1]),
                                        model.Prod( 1.0, x[2])), 20.0, "c1");
      rng[0][1] = model.AddLe(model.Sum(model.Prod( 1.0, x[0]),
                                        model.Prod(-3.0, x[1]),
                                        model.Prod( 1.0, x[2])), 30.0, "c2");
   }

   internal static void PopulateByColumn(IMPModeler model,
                                         INumVar[][] var,
                                         IRange[][] rng) {
      IObjective obj = model.AddMaximize();

      rng[0] = new IRange[2];
      rng[0][0] = model.AddRange(-System.Double.MaxValue, 20.0, "c1");
      rng[0][1] = model.AddRange(-System.Double.MaxValue, 30.0, "c2");

      IRange r0 = rng[0][0];
      IRange r1 = rng[0][1];

      var[0] = new INumVar[3];
      var[0][0] = model.NumVar(model.Column(obj,  1.0).And(
                               model.Column(r0,  -1.0).And(
                               model.Column(r1,   1.0))),
                               0.0, 40.0, "x1");
      var[0][1] = model.NumVar(model.Column(obj,  2.0).And(
                               model.Column(r0,   1.0).And(
                               model.Column(r1,  -3.0))),
                               0.0, System.Double.MaxValue, "x2");
      var[0][2] = model.NumVar(model.Column(obj,  3.0).And(
                               model.Column(r0,   1.0).And(
                               model.Column(r1,   1.0))),
                               0.0, System.Double.MaxValue, "x3");
   }

   internal static void PopulateByNonzero(IMPModeler model,
                                          INumVar[][] var,
                                          IRange[][] rng) {
      double[]  lb = {0.0, 0.0, 0.0};
      double[]  ub = {40.0, System.Double.MaxValue, System.Double.MaxValue};
      INumVar[] x  = model.NumVarArray(3, lb, ub);
      var[0] = x;

      double[] objvals = {1.0, 2.0, 3.0};
      model.Add(model.Maximize(model.ScalProd(x, objvals)));

      rng[0] = new IRange[2];
      rng[0][0] = model.AddRange(-System.Double.MaxValue, 20.0);
      rng[0][1] = model.AddRange(-System.Double.MaxValue, 30.0);

      rng[0][0].Expr = model.Sum(model.Prod(-1.0, x[0]),
                                 model.Prod( 1.0, x[1]),
                                 model.Prod( 1.0, x[2]));
      rng[0][1].Expr = model.Sum(model.Prod( 1.0, x[0]),
                                 model.Prod(-3.0, x[1]),
                                 model.Prod( 1.0, x[2]));
      x[0].Name = "x1";
      x[1].Name = "x2";
      x[2].Name = "x3";

      rng[0][0].Name = "c1";
      rng[0][0].Name = "c2";
   }
}
