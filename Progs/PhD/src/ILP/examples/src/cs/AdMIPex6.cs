// --------------------------------------------------------------------------
// File: AdMIPex6.cs
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
// AdMIPex6.cs -- Solving a model by passing in a solution for the root node
//                and using that in a solve callback
//
// To run this example, command line arguments are required:
//     AdMIPex6  filename
// where 
//     filename  Name of the file, with .mps, .lp, or .sav
//               extension, and a possible additional .gz 
//               extension.
// Example:
//     AdMIPex6  mexample.mps.gz

using ILOG.Concert;
using ILOG.CPLEX;
using System.Collections;


public class AdMIPex6 {
   internal class Solve : Cplex.SolveCallback {
      internal bool _done = false;
      internal INumVar[] _vars;
      internal double[]  _x;
      internal Solve(INumVar[] vars, double[] x) { _vars = vars; _x = x; }
    
      public override void Main() {
         if ( !_done ) {
            SetVectors(_x, _vars, null, null);
            _done = true;
         }
      }
   }

   public static void Main(string[] args) {
      try {
         Cplex cplex = new Cplex();
       
         cplex.ImportModel(args[0]);

         IEnumerator matrixEnum = cplex.GetLPMatrixEnumerator();
         matrixEnum.MoveNext();

         ILPMatrix lp = (ILPMatrix)matrixEnum.Current;

         IConversion relax = cplex.Conversion(lp.NumVars,
                                              NumVarType.Float);
         cplex.Add(relax);
       
         cplex.Solve();
         System.Console.WriteLine("Relaxed solution status = " + cplex.GetStatus());
         System.Console.WriteLine("Relaxed solution value  = " + cplex.ObjValue);

         double[] vals = cplex.GetValues(lp.NumVars);
         cplex.Use(new Solve(lp.NumVars, vals));

         cplex.Delete(relax);
       
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
