// --------------------------------------------------------------------------
// File: InOut3.cs
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
// Problem Description
// -------------------
// 
// A company has to produce 3 products, using 2 resources.
// Each resource has a limited capacity.
// Each product consumes a given number of machines.
// Each product has a production cost (the inside cost).
// Both products can also be brought outside the company at a given 
// cost (the outside cost)
// 
// Minimize external production given product demand, a cost
// constraint, and minimum internal production constraints.
//
// --------------------------------------------------------------------------

using ILOG.Concert;
using ILOG.CPLEX;
using System;

public class InOut3 {
   static int _nbProds = 3;
   static int _nbResources = 2;
   static double[][] _consumption = {new double[] {0.5, 0.4, 0.3},
                                     new double[] {0.2, 0.4, 0.6}};
   static double[] _demand      = {100.0, 200.0, 300.0 };
   static double[] _capacity    = {20.0, 40.0};
   static double[] _insideCost  = {0.6, 0.8, 0.3};
   static double[] _outsideCost = {0.8, 0.9, 0.4};
   
   static void DisplayResults(Cplex cplex,
                              INumVar costVar,
                              INumVar[] inside,
                              INumVar[] outside) {
      System.Console.WriteLine("cost: " + cplex.GetValue(costVar));
      
      for(int p = 0; p < _nbProds; p++) {
         System.Console.WriteLine("P" + p);
         System.Console.WriteLine("inside:  " + cplex.GetValue(inside[p]));
         System.Console.WriteLine("outside: " + cplex.GetValue(outside[p]));
      }
   }
   
   public static void Main( string[] args ) {
      try {
         Cplex cplex = new Cplex();
       
         INumVar[]  inside = cplex.NumVarArray(_nbProds, 10.0, Double.MaxValue);
         INumVar[] outside = cplex.NumVarArray(_nbProds, 0.0, Double.MaxValue);
         INumVar   costVar = cplex.NumVar(0.0, Double.MaxValue);
       
         cplex.AddEq(costVar, cplex.Sum(cplex.ScalProd(inside, _insideCost),
                                        cplex.ScalProd(outside, _outsideCost)));
         
         IObjective obj = cplex.AddMinimize(costVar);
       
         // Must meet demand for each product
       
         for(int p = 0; p < _nbProds; p++)
            cplex.AddEq(cplex.Sum(inside[p], outside[p]), _demand[p]);
       
         // Must respect capacity constraint for each resource
       
         for(int r = 0; r < _nbResources; r++)
            cplex.AddLe(cplex.ScalProd(_consumption[r], inside), _capacity[r]);
       
         cplex.Solve();
       
         if ( cplex.GetStatus() != Cplex.Status.Optimal ) {
            System.Console.WriteLine("No optimal solution found");
            return;
         }
       
         // New constraint: cost must be no more than 10% over minimum
       
         double cost = cplex.ObjValue;
         costVar.UB = 1.1 * cost;
       
         // New objective: minimize outside production
       
         obj.Expr = cplex.Sum(outside);
       
         cplex.Solve();
       
         DisplayResults(cplex, costVar, inside, outside);
         System.Console.WriteLine("----------------------------------------");
         cplex.End();
      }
      catch (ILOG.Concert.Exception exc) {
         System.Console.WriteLine("Concert exception '" + exc + "' caught");
      }
   }
}

/*
cost: 373.333
P0
inside:  10
outside: 90
P1
inside:  10
outside: 190
P2
inside:  36.6667
outside: 263.333
----------------------------------------
*/
