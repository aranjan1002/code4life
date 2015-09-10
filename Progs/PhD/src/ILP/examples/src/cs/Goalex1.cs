// --------------------------------------------------------------------------
// File: Goalex1.cs
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
// Goalex1.cs - A simple goal example.
//
//              Create a branch goal that chooses the variable
//              with the largest objective from amongst those
//              with the largest integer infeasibility.
//

using ILOG.CPLEX;
using ILOG.Concert;
using System.Collections;


public class Goalex1 {
   internal class MyBranchGoal : Cplex.Goal {
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
               if ( xj_inf > 0.5 )
                  xj_inf = 1.0 - xj_inf;
               if ( xj_inf >= maxinf                               &&
                    (xj_inf > maxinf || System.Math.Abs(obj[j]) >= maxobj)  ) {
                  bestj  = j;
                  maxinf = xj_inf;
                  maxobj = System.Math.Abs(obj[j]);
               }
            }
         }

         if ( bestj >= 0 ) {
            return cplex.And(
                     cplex.Or(cplex.GeGoal(_vars[bestj], System.Math.Floor(x[bestj])+1),
                              cplex.LeGoal(_vars[bestj], System.Math.Floor(x[bestj]))),
                     this);
         }
         else
            return null;
      }
   }


   public static void Main (string[] args) {
      if ( args.Length != 1 ) {
         System.Console.WriteLine("Usage: Goalex1 filename");
         System.Console.WriteLine("   where filename is a file with extension ");
         System.Console.WriteLine("      MPS, SAV, or LP (lower case is allowed)");
         System.Console.WriteLine(" Exiting...");
         return;
      }

      try {
         Cplex cplex = new Cplex();

         cplex.ImportModel(args[0]);

         IEnumerator matrixEnum = cplex.GetLPMatrixEnumerator();
         matrixEnum.MoveNext();

         ILPMatrix lp = (ILPMatrix)matrixEnum.Current;

	 cplex.SetParam(Cplex.IntParam.MIPSearch, Cplex.MIPSearch.Traditional);
         if ( cplex.Solve(new MyBranchGoal(lp.NumVars)) ) {
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
