// --------------------------------------------------------------------------
// File: MixBlend.cs
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
// Goal is to blend four sources to produce an alloy: pure metal, raw
// materials, scrap, and ingots.
// 
// Each source has a cost.
// Each source is made up of elements in different proportions.
// Ingots are discrete, so they are modeled as integers.
// Alloy has minimum and maximum proportion of each element.
// 
// Minimize cost of producing a requested quantity of alloy.
// 
// --------------------------------------------------------------------------

using ILOG.Concert;
using ILOG.CPLEX;

public class MixBlend {
   internal static int _nbElements = 3;
   internal static int _nbRaw = 2;
   internal static int _nbScrap = 2;
   internal static int _nbIngot = 1;
   internal static double _alloy = 71.0;
   
   internal static double[] _cm = new double[] {22.0, 10.0, 13.0};
   internal static double[] _cr = new double[] {6.0, 5.0};
   internal static double[] _cs = new double[] {7.0, 8.0};
   internal static double[] _ci = new double[] {9.0};
   internal static double[] _p  = new double[] {0.05, 0.30, 0.60};
   internal static double[] _P  = new double[] {0.10, 0.40, 0.80};
   
   internal static double[][] _PRaw = new double[][] {new double[] {0.20, 0.01},
                                                      new double[] {0.05, 0.00},
                                                      new double[] {0.05, 0.30}};
   internal static double[][] _PScrap = new double[][] {new double[] {0.00, 0.01},
                                                        new double[] {0.60, 0.00},
                                                        new double[] {0.40, 0.70}};
   internal static double[][] _PIngot = new double[][] {new double[] {0.10},
                                                        new double[] {0.45},
                                                        new double[] {0.45}};

   public static void Main( string[] args ) {
      try {
         Cplex cplex = new Cplex();
       
         INumVar[] m = cplex.NumVarArray(_nbElements, 0.0, System.Double.MaxValue);
         INumVar[] r = cplex.NumVarArray(_nbRaw,      0.0, System.Double.MaxValue);
         INumVar[] s = cplex.NumVarArray(_nbScrap,    0.0, System.Double.MaxValue);
         INumVar[] i = cplex.IntVarArray(_nbIngot,      0, System.Int32.MaxValue);
         INumVar[] e = new INumVar[_nbElements];
       
         // Objective Function: Minimize Cost
         cplex.AddMinimize(cplex.Sum(cplex.ScalProd(_cm, m),
                                     cplex.ScalProd(_cr, r),
                                     cplex.ScalProd(_cs, s),
                                     cplex.ScalProd(_ci, i)));
       
         // Min and max quantity of each element in alloy
         for (int j = 0; j < _nbElements; j++) {
            e[j] = cplex.NumVar(_p[j] * _alloy, _P[j] * _alloy);
         }
       
         // Constraint: produce requested quantity of alloy
         cplex.AddEq(cplex.Sum(e), _alloy);
       
         // Constraints: Satisfy element quantity requirements for alloy
         for (int j = 0; j < _nbElements; j++) {
            cplex.AddEq(e[j],
                        cplex.Sum(m[j],
                                  cplex.ScalProd(_PRaw[j], r),
                                  cplex.ScalProd(_PScrap[j], s),
                                  cplex.ScalProd(_PIngot[j], i)));
         }

       
         if ( cplex.Solve() ) {
            if ( cplex.GetStatus().Equals(Cplex.Status.Infeasible) ) {
               System.Console.WriteLine("No feasible solution found");
               return;
            }
          
            double[] mVals = cplex.GetValues(m);
            double[] rVals = cplex.GetValues(r);
            double[] sVals = cplex.GetValues(s);
            double[] iVals = cplex.GetValues(i);
            double[] eVals = cplex.GetValues(e);
            
            // Print results
            System.Console.WriteLine("Cost:" + cplex.ObjValue);

            System.Console.WriteLine("Pure metal:");
            for(int j = 0; j < _nbElements; j++)
               System.Console.WriteLine("(" + j + ") " + mVals[j]);

            System.Console.WriteLine("Raw material:");
            for(int j = 0; j < _nbRaw; j++)
               System.Console.WriteLine("(" + j + ") " + rVals[j]);

            System.Console.WriteLine("Scrap:");
            for(int j = 0; j < _nbScrap; j++)
               System.Console.WriteLine("(" + j + ") " + sVals[j]);

            System.Console.WriteLine("Ingots : ");
            for(int j = 0; j < _nbIngot; j++)
               System.Console.WriteLine("(" + j + ") " + iVals[j]);

            System.Console.WriteLine("Elements:");
            for(int j = 0; j < _nbElements; j++)
               System.Console.WriteLine("(" + j + ") " + eVals[j]);
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception exc) {
         System.Console.WriteLine("Concert exception '" + exc + "' caught");
      }
   }
}

/*
Cost : 653.61
Pure metal :
0) 0.0466667
1) 0
2) 0
Raw material :
0) 0
1) 0
Scrap : 
0) 17.4167
1) 30.3333
Ingots : 
0) 32
Elements : 
0) 3.55
1) 24.85
2) 42.6
*/
