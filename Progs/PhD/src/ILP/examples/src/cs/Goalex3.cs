// --------------------------------------------------------------------------
// File: Goalex3.cs
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
// Goalex3.cs - Node selectors in goal search
//
//              This is an extension of example Goalex1.cs.
//              It adds node evaluators to further control the
//              goal based search.  The node evaluator chooses
//              the node that is deepest in the tree among those
//              with maximum sum of integer infeasibilities

using ILOG.CPLEX;
using ILOG.Concert;
using System.Collections;

public class Goalex3 {
   public class MyBranchGoal : Cplex.Goal {
      internal INumVar[] _vars;
      
      internal MyBranchGoal(INumVar[] vars) {
         _vars = vars;
      }
    
      // Branch on var with largest objective coefficient
      // among those with largest infeasibility
      public override  Cplex.Goal Execute(Cplex cplex) {
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
       
         if (bestj >= 0) {
            return cplex.And(
                     cplex.Or(cplex.GeGoal(_vars[bestj], System.Math.Floor(x[bestj])+1),
                              cplex.LeGoal(_vars[bestj], System.Math.Floor(x[bestj]))),
                     this);
         }
         else 
            return null;
      }
   }
   
   public class MyDepthEvaluator : Cplex.NodeEvaluator {
      protected override  double Evaluate() {
         return -Depth;
      }
   }

   public class MyIISEvaluator : Cplex.NodeEvaluator {
      protected override  double Evaluate() {
         return -InfeasibilitySum;
      }
   }


   public static void Main (string[] args) {
      if ( args.Length != 1 ) {
         System.Console.WriteLine("Usage: Goalex3 filename");
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
       
         Cplex.Goal iiSumGoal = cplex.Apply(new MyBranchGoal(lp.NumVars), 
                                            new MyIISEvaluator());
         Cplex.Goal depthGoal = cplex.Apply(iiSumGoal,
                                            new MyDepthEvaluator());
       
	 cplex.SetParam(Cplex.IntParam.MIPSearch, Cplex.MIPSearch.Traditional);
         if ( cplex.Solve(depthGoal) ) {
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
