// --------------------------------------------------------------------------
// File: MIPex1.cs
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
// MIPex1.cs - Entering and optimizing a MIP problem

using ILOG.Concert;
using ILOG.CPLEX;


public class MIPex1 {
   public static void Main(string[] args) {
      try {
         Cplex cplex = new Cplex();

         INumVar[][] var = new INumVar[1][];
         IRange[][]  rng = new IRange[1][];

         PopulateByRow(cplex, var, rng);

         if ( cplex.Solve() ) {
            double[] x     = cplex.GetValues(var[0]);
            double[] slack = cplex.GetSlacks(rng[0]);

            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);

            for (int j = 0; j < x.Length; ++j) {
               System.Console.WriteLine("Variable   " + j +
                                        ": Value = " + x[j]);
            }

            for (int i = 0; i < slack.Length; ++i) {
               System.Console.WriteLine("Constraint " + i +
                                        ": Slack = " + slack[i]);
            }
         }

         cplex.ExportModel("mipex1.lp");
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught '" + e + "' caught");
      }
   }


   internal static void PopulateByRow (IMPModeler  model,
                                       INumVar[][] var,
                                       IRange[][]  rng) {
      //  First define the variables, three continuous and one integer
      double[]        xlb = {0.0, 0.0, 0.0, 2.0};
      double[]        xub = {40.0, System.Double.MaxValue,
                                   System.Double.MaxValue, 3.0};
      NumVarType[] xt  = {NumVarType.Float, NumVarType.Float,
                          NumVarType.Float, NumVarType.Int};
      INumVar[]     x  = model.NumVarArray(4, xlb, xub, xt);
      var[0] = x;

      // Objective Function:  maximize x0 + 2*x1 + 3*x2 + x3
      double[] objvals = {1.0, 2.0, 3.0, 1.0};
      model.AddMaximize(model.ScalProd(x, objvals));

      // Three constraints
      rng[0] = new IRange[3];
      // - x0 + x1 + x2 + 10*x3 <= 20
      rng[0][0] = model.AddLe(model.Sum(model.Prod(-1.0, x[0]),
                                        model.Prod( 1.0, x[1]),
                                        model.Prod( 1.0, x[2]),
                                        model.Prod(10.0, x[3])), 20.0);
      // x0 - 3*x1 + x2 <= 30
      rng[0][1] = model.AddLe(model.Sum(model.Prod( 1.0, x[0]),
                                        model.Prod(-3.0, x[1]),
                                        model.Prod( 1.0, x[2])), 30.0);
      // x1 - 3.5*x3 = 0
      rng[0][2] = model.AddEq(model.Sum(model.Prod( 1.0, x[1]),
                                        model.Prod(-3.5, x[3])), 0.0);
   }
}
