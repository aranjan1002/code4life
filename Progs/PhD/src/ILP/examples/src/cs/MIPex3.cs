// --------------------------------------------------------------------------
// File: MIPex3.cs
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
// MIPex3.cs - Entering and optimizing a MIP problem with SOS sets
//             and priority orders.  It is a modification of MIPex1.cs.
//             Note that the problem solved is slightly different than
//             MIPex1.cs so that the output is interesting.


using ILOG.Concert;
using ILOG.CPLEX;
 
 
public class MIPex3 {
   public static void Main(string[] args) {
      try {
         // create CPLEX optimizer/modeler an turn of presolve to make
         // output more interesting
         Cplex cplex = new Cplex();
         cplex.SetParam(Cplex.BooleanParam.PreInd, false);
       
         // build model
         INumVar[][] var = new INumVar[1][];
         IRange[][]  rng = new IRange[1][];
         PopulateByRow (cplex, var, rng);
       
         // setup branch priorities
         INumVar[] ordvar = {var[0][1], var[0][3]};
         int[]     ordpri = {8, 7};
         cplex.SetPriorities (ordvar, ordpri);
       
         // setup branch directions
         cplex.SetDirection(ordvar[0], Cplex.BranchDirection.Up);
         cplex.SetDirection(ordvar[1], Cplex.BranchDirection.Down);
       
         // write priority order to file
         cplex.WriteOrder("mipex3.ord");
       
         // optimize an output solution information
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
         cplex.ExportModel("mipex3.lp");
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
          System.Console.WriteLine("Concert exception caught: " + e);
      }
   }

   internal static void PopulateByRow (IMPModeler  model,
                                       INumVar[][] var,
                                       IRange[][]  rng) {

      // Define the variables one-by-one
      INumVar[] x = new INumVar[4];
      x[0] = model.NumVar(0.0, 40.0, "x0");
      x[1] = model.IntVar(0, System.Int32.MaxValue, "x1");
      x[2] = model.IntVar(0, System.Int32.MaxValue, "x2");
      x[3] = model.IntVar(2, 3, "x3");
      var[0] = x;
    
      // Objective Function
      model.AddMaximize(model.Sum(model.Prod( 1.0, x[0]),
                                  model.Prod( 2.0, x[1]),
                                  model.Prod( 3.0, x[2]),
                                  model.Prod( 1.0, x[3])));
    
      // Define three constraints one-by-one 
      rng[0] = new IRange[3];
      rng[0][0] = model.AddLe(model.Sum(model.Prod(-1.0, x[0]),
                                        model.Prod( 1.0, x[1]),
                                        model.Prod( 1.0, x[2]),
                                        model.Prod(10.0, x[3])),
                              20.0, "rng0");
      rng[0][1] = model.AddLe(model.Sum(model.Prod( 1.0, x[0]),
                                        model.Prod(-3.0, x[1]),
                                        model.Prod( 1.0, x[2])),
                              30.0, "rng1");
      rng[0][2] = model.AddEq(model.Sum(model.Prod( 1.0, x[1]),
                                        model.Prod(-3.5, x[3])),
                              0, "rng2");
    
      // add special ordered set of type 1
      INumVar[] sosvars    = {x[2], x[3]};
      double[]  sosweights = {25.0, 18.0};
      model.AddSOS1(sosvars, sosweights);
   }
}
