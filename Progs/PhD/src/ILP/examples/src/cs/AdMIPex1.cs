// --------------------------------------------------------------------------
// File: AdMIPex1.cs
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
// AdMIPex1.cs -- Use the node and branch callbacks to optimize
//                a MIP problem
//
// To run this example, command line arguments are required.
// i.e.,   AdMIPex1   filename
//
// Example:
//     AdMIPex1  example.mps
//

using ILOG.Concert;
using ILOG.CPLEX;
using System.Collections;


public class AdMIPex1 {
   internal class MyBranch : Cplex.BranchCallback {
      internal INumVar[] _vars;
      internal MyBranch(INumVar[] vars) { _vars = vars; }

      public override void Main() {
         if ( !GetBranchType().Equals(Cplex.BranchType.BranchOnVariable) )
            return;

         // Branch on var with largest objective coefficient
         // among those with largest infeasibility

         double[] x   = GetValues  (_vars);
         double[] obj = GetObjCoefs(_vars);
         Cplex.IntegerFeasibilityStatus[] feas = GetFeasibilities(_vars);

         double maxinf = 0.0;
         double maxobj = 0.0;
         int    bestj  = -1;
         int    cols   = _vars.Length;
         for (int j = 0; j < cols; ++j) {
            if ( feas[j].Equals(Cplex.IntegerFeasibilityStatus.Infeasible) ) {
               double xj_inf = x[j] - System.Math.Floor(x[j]);
               if ( xj_inf > 0.5 )  xj_inf = 1.0 - xj_inf;
               if ( xj_inf >= maxinf                              &&
                    (xj_inf > maxinf || System.Math.Abs(obj[j]) >= maxobj)  ) {
                  bestj  = j;
                  maxinf = xj_inf;
                  maxobj = System.Math.Abs(obj[j]);
               }
            }
         }

         if ( bestj >= 0 ) {
            MakeBranch(_vars[bestj], x[bestj],
                       Cplex.BranchDirection.Up,   ObjValue);
            MakeBranch(_vars[bestj], x[bestj],
                       Cplex.BranchDirection.Down, ObjValue);
         }
      }
   }

   internal class MySelect : Cplex.NodeCallback {
      public override void Main() {
         int    remainingNodes = NremainingNodes;
         int    bestnode       = -1;
         int    maxdepth       = -1;
         double maxiisum       = 0.0;

         for (int i = 0; i < remainingNodes; ++i) {
            int depth = GetDepth(i);
            double iisum = GetInfeasibilitySum(i);
            if ( (depth >= maxdepth)                   &&
                 (depth > maxdepth || iisum > maxiisum)  ) {
               bestnode = i;
               maxdepth = depth;
               maxiisum = iisum;
            }
         }
         if ( bestnode >= 0 ) SelectNode(bestnode);
      }
   }

   public static void Main(string[] args) {
      if ( args.Length != 1 ) {
         System.Console.WriteLine("Usage: AdMIPex1 filename");
         System.Console.WriteLine("   where filename is a file with extension ");
         System.Console.WriteLine("      MPS, SAV, or LP (lower case is allowed)");
         System.Console.WriteLine(" Exiting...");
         System.Environment.Exit(-1);
      }

      try {
         Cplex cplex = new Cplex();

         cplex.ImportModel(args[0]);

         IEnumerator matrixEnum = cplex.GetLPMatrixEnumerator();
         matrixEnum.MoveNext();

         ILPMatrix lp = (ILPMatrix)matrixEnum.Current;

         cplex.Use(new MyBranch(lp.NumVars));
         cplex.Use(new MySelect());

	 cplex.SetParam(Cplex.IntParam.MIPSearch, Cplex.MIPSearch.Traditional);
         if ( cplex.Solve() ) {
            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught: " + e);
      }
   }
}
