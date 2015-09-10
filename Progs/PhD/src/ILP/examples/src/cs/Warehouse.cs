// --------------------------------------------------------------------------
// File: Warehouse.cs
// --------------------------------------------------------------------------
// Version 12.2  
// Copyright IBM Corporation 2003, 2010. All Rights Reserved.
//
// US Government Users Restricted Rights - Use, duplication or
// disclosure restricted by GSA ADP Schedule Contract with
// IBM Corp.
// --------------------------------------------------------------------------
// Licensed Materials - Property of IBM
// 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
//
// warehouse.cs - Example that uses the goal API to enforce constraints
//                dynamically, depending on the relaxation solution at
//                each MIP node.
//
//                Given a set of warehouses that each have a
//                capacity, a cost per unit stored, and a 
//                minimum usage level, this example find an
//                assignment of items to warehouses that minimizes
//                total cost.  The minimum usage levels are
//                enforced dynamically using the goal API.

using ILOG.Concert;
using ILOG.CPLEX;

public class Warehouse {
   internal class SemiContGoal : Cplex.Goal {
      internal INumVar[] _scVars;
      internal double[]  _scLbs;
      
      internal SemiContGoal(INumVar[] scVars,
                            double[]  scLbs) {
         _scVars = scVars;
         _scLbs  = scLbs;
      }
      
      public override  Cplex.Goal Execute(Cplex cplex) {
         int besti = -1;
         double maxObjCoef = System.Double.MinValue;
       
         // From among all variables that do not respect their minimum 
         // usage levels, select the one with maximum objective coefficient.
         for (int i = 0; i < _scVars.Length; i++) {
            double val = GetValue(_scVars[i]);
            if ( val >= 1e-5            &&
                 val <= _scLbs[i] - 1e-5  ) {
               if (GetObjCoef(_scVars[i]) >= maxObjCoef) {
                  besti = i;
                  maxObjCoef = GetObjCoef(_scVars[i]);
               }
            }
         }
       
         //  If any are found, branch to enforce the condition that
         //  the variable must either be 0.0 or greater than
         //  the minimum usage level.
         if ( besti != -1 ) {
            return cplex.And(cplex.Or(cplex.LeGoal(_scVars[besti], 0.0),
                                      cplex.GeGoal(_scVars[besti], 
                                                   _scLbs[besti])),
                             this );
         }
         else if ( !IsIntegerFeasible() ) {
            return cplex.And(cplex.BranchAsCplex(), this );
         }
       
         return null;
      }
   }
   
   public static void Main (string[] args) {
      try {
         Cplex cplex = new Cplex();
       
         int nbWhouses = 4;
         int nbLoads = 31;
       
         INumVar[] capVars = 
            cplex.NumVarArray(nbWhouses, 0, 10, 
                              NumVarType.Int); // Used capacities
         double[]    capLbs  = {2.0, 3.0, 5.0, 7.0}; // Minimum usage level
         double[]    costs   = {1.0, 2.0, 4.0, 6.0}; // Cost per warehouse
       
         // These variables represent the assigninment of a
         // load to a warehouse.
         INumVar[][] assignVars = new INumVar[nbWhouses][];
         for (int w = 0; w < nbWhouses; w++) {
            assignVars[w] = cplex.NumVarArray(nbLoads, 0, 1,
                                              NumVarType.Int);
            
            // Links the number of loads assigned to a warehouse with 
            // the capacity variable of the warehouse.
            cplex.AddEq(cplex.Sum(assignVars[w]), capVars[w]);
         }
       
         // Each load must be assigned to just one warehouse.
         for (int l = 0; l < nbLoads; l++) {
            INumVar[] aux = new INumVar[nbWhouses];
            for (int w = 0; w < nbWhouses; w++)
               aux[w] = assignVars[w][l];
          
            cplex.AddEq(cplex.Sum(aux), 1);
         }
       
         cplex.AddMinimize(cplex.ScalProd(costs, capVars));
	 cplex.SetParam(Cplex.IntParam.MIPSearch, Cplex.MIPSearch.Traditional);
       
         if ( cplex.Solve(new SemiContGoal(capVars, capLbs)) ) {
            System.Console.WriteLine("--------------------------------------------");
            System.Console.WriteLine();
            System.Console.WriteLine("Solution found:");
            System.Console.WriteLine(" Objective value = " + cplex.ObjValue);
            System.Console.WriteLine();
            for (int w = 0; w < nbWhouses; w++) {
               System.Console.WriteLine("Warehouse " + w + ": stored " 
                                  + cplex.GetValue(capVars[w]) + " loads");
               for (int l = 0; l < nbLoads; l++) {
                  if ( cplex.GetValue(assignVars[w][l]) > 1e-5 )
                     System.Console.Write("Load " + l + " | ");
               }
               System.Console.WriteLine(); System.Console.WriteLine();
            }
            System.Console.WriteLine("--------------------------------------------");
         }
         else {
            System.Console.WriteLine(" No solution found ");
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught: " + e);
      }    
   }
}

