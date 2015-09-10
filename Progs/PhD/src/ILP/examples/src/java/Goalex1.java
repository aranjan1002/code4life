/* --------------------------------------------------------------------------
 * File: Goalex1.java
 * Version 12.2
 * --------------------------------------------------------------------------
 * Licensed Materials - Property of IBM
 * 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
 * Copyright IBM Corporation 2001, 2010. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with
 * IBM Corp.
 * --------------------------------------------------------------------------
 *
 * Goalex1.java - A simple goal example.
 *
 *                Create a branch goal that chooses the variable
 *                with the largest objective from amongst those
 *                with the largest integer infeasibility.
 */

import ilog.cplex.*;
import ilog.concert.*;


public class Goalex1 {
   static class MyBranchGoal extends IloCplex.Goal {
      IloNumVar[] _vars;

      MyBranchGoal(IloNumVar[] vars) {
         _vars = vars;
      }

      // Branch on var with largest objective coefficient
      // among those with largest infeasibility
      public IloCplex.Goal execute(IloCplex cplex) throws IloException {
         double[] x   = getValues  (_vars);
         double[] obj = getObjCoefs(_vars);
         IloCplex.IntegerFeasibilityStatus[] feas = getFeasibilities(_vars);

         double maxinf = 0.0;
         double maxobj = 0.0;
         int    bestj  = -1;
         int    cols   = _vars.length;
         for (int j = 0; j < cols; ++j) {
            if ( feas[j].equals(IloCplex.IntegerFeasibilityStatus.Infeasible) ) {
               double xj_inf = x[j] - Math.floor(x[j]);
               if ( xj_inf > 0.5 )
                  xj_inf = 1.0 - xj_inf;
               if ( xj_inf >= maxinf                               &&
                    (xj_inf > maxinf || Math.abs(obj[j]) >= maxobj)  ) {
                  bestj  = j;
                  maxinf = xj_inf;
                  maxobj = Math.abs(obj[j]);
               }
            }
         }

         if ( bestj >= 0 ) {
            return cplex.and(
                     cplex.or(cplex.geGoal(_vars[bestj], Math.floor(x[bestj])+1),
                              cplex.leGoal(_vars[bestj], Math.floor(x[bestj]))),
                     this);
         }
         else
            return null;
      }
   }


   public static void main (String[] args) {
      if ( args.length != 1 ) {
         System.out.println("Usage: Goalex1 filename");
         System.out.println("   where filename is a file with extension ");
         System.out.println("      MPS, SAV, or LP (lower case is allowed)");
         System.out.println(" Exiting...");
         System.exit(-1);
      }

      try {
         IloCplex cplex = new IloCplex();

         cplex.importModel(args[0]);
         IloLPMatrix lp = (IloLPMatrix)cplex.LPMatrixIterator().next();

	 cplex.setParam(IloCplex.IntParam.MIPSearch, IloCplex.MIPSearch.Traditional);
         if ( cplex.solve(new MyBranchGoal(lp.getNumVars())) ) {
            System.out.println("Solution status = " + cplex.getStatus());
            System.out.println("Solution value  = " + cplex.getObjValue());
         }

         cplex.end();
      }
      catch (IloException e) {
         System.err.println("Concert exception caught: " + e);
      }
   }
}
