// --------------------------------------------------------------------------
// File: LPex6.cs
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
// LPex6.cs - Illustrates that optimal basis can be copied and
//            used to start an optimization.
//
 
using ILOG.Concert;
using ILOG.CPLEX;


public class LPex6 {
   public static void Main(string[] args) {
      try {
         Cplex cplex = new Cplex();
       
         INumVar[][] var = new INumVar[1][];
         IRange[][]  rng = new IRange[1][];
         
         PopulateByRow(cplex, var, rng);
       
         Cplex.BasisStatus[] cstat = {
            Cplex.BasisStatus.AtUpper,
            Cplex.BasisStatus.Basic,
            Cplex.BasisStatus.Basic
         };
         Cplex.BasisStatus[] rstat = {
            Cplex.BasisStatus.AtLower,
            Cplex.BasisStatus.AtLower
         };
         cplex.SetBasisStatuses(var[0], cstat, rng[0], rstat);
         
         if ( cplex.Solve() ) {
            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);
            System.Console.WriteLine("Iteration count = " + cplex.Niterations);
          
            double[] x     = cplex.GetValues(var[0]);
            double[] dj    = cplex.GetReducedCosts(var[0]);
            double[] pi    = cplex.GetDuals(rng[0]);
            double[] slack = cplex.GetSlacks(rng[0]);
          
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
      catch (ILOG.Concert.Exception exc) {
         System.Console.WriteLine("Concert exception '" + exc + "' caught");
      }
   }

   internal static void PopulateByRow(IMPModeler  model,
                                      INumVar[][] var,
                                      IRange[][]  rng) {
      double[] lb = {0.0, 0.0, 0.0};
      double[] ub = {40.0, System.Double.MaxValue, System.Double.MaxValue};
      var[0] = model.NumVarArray(3, lb, ub);
    
      double[] objvals = {1.0, 2.0, 3.0};
      model.AddMaximize(model.ScalProd(var[0], objvals));
    
      rng[0] = new IRange[2];
      rng[0][0] = model.AddLe(model.Sum(model.Prod(-1.0, var[0][0]),
                                        model.Prod( 1.0, var[0][1]),
                                        model.Prod( 1.0, var[0][2])), 20.0);
      rng[0][1] = model.AddLe(model.Sum(model.Prod( 1.0, var[0][0]),
                                        model.Prod(-3.0, var[0][1]),
                                        model.Prod( 1.0, var[0][2])), 30.0);
   }
}
