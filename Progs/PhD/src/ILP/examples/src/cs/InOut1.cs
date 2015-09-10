// --------------------------------------------------------------------------
// File: InOut1.cs
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
// Minimize the total cost so that the company exactly meets the
// demand.
//
// --------------------------------------------------------------------------

using ILOG.Concert;
using ILOG.CPLEX;

public class InOut1 {
   internal static int _nbProds = 3;
   internal static int _nbResources = 2;
   internal static double[][] _consumption = {new double[] {0.5, 0.4, 0.3},
                                              new double[] {0.2, 0.4, 0.6}};
   internal static double[] _demand = {100.0, 200.0, 300.0};
   internal static double[] _capacity = {20.0, 40.0};
   internal static double[] _insideCost = {0.6, 0.8, 0.3};
   internal static double[] _outsideCost = {0.8, 0.9, 0.4};
   
   internal static void DisplayResults(Cplex cplex,
                                       INumVar[] inside,
                                       INumVar[] outside) {
      System.Console.WriteLine("cost: " + cplex.ObjValue);
      
      for(int p = 0; p < _nbProds; p++) {
         System.Console.WriteLine("P" + p);
         System.Console.WriteLine("inside:  " + cplex.GetValue(inside[p]));
         System.Console.WriteLine("outside: " + cplex.GetValue(outside[p]));
      }
   }
   
   public static void Main( string[] args ) {
      try {
         Cplex cplex = new Cplex();
       
         INumVar[]  inside = new INumVar[_nbProds];
         INumVar[] outside = new INumVar[_nbProds];
       
         IObjective obj = cplex.AddMinimize();
       
         // Must meet demand for each product
       
         for(int p = 0; p < _nbProds; p++) {
            IRange demRange = cplex.AddRange(_demand[p], _demand[p]);
            inside[p] = cplex.NumVar(cplex.Column(obj, _insideCost[p]).And(
                                     cplex.Column(demRange, 1.0)),
                                     0.0, System.Double.MaxValue);
            
            outside[p] = cplex.NumVar(cplex.Column(obj, _outsideCost[p]).And(
                                      cplex.Column(demRange, 1.0)),
                                      0.0, System.Double.MaxValue);
         }
       
         // Must respect capacity constraint for each resource
       
         for(int r = 0; r < _nbResources; r++)
            cplex.AddLe(cplex.ScalProd(_consumption[r], inside), _capacity[r]);
       
         cplex.Solve();
       
         if ( !cplex.GetStatus().Equals(Cplex.Status.Optimal) ) {
            System.Console.WriteLine("No optimal solution found");
            return;
         }
       
         DisplayResults(cplex, inside, outside);
         System.Console.WriteLine("----------------------------------------");
         cplex.End();
      }
      catch (ILOG.Concert.Exception exc) {
         System.Console.WriteLine("Concert exception '" + exc + "' caught");
      }
   }
}
  
/*
cost: 372
P0
inside:  40
outside: 60
P1
inside:  0
outside: 200
P2
inside:  0
outside: 300
*/
