// --------------------------------------------------------------------------
// File: AdMIPex2.cs
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
// AdMIPex2.cs -  Use the heuristic callback for optimizing a MIP problem
//
// To run this example, command line arguments are required.
// i.e.,   AdMIPex2   filename
//
// Example:
//     AdMIPex2  example.mps
//

using ILOG.Concert;
using ILOG.CPLEX;
using System.Collections;


public class AdMIPex2 {
   internal class RoundDown : Cplex.HeuristicCallback {
      internal INumVar[] _vars;
      internal RoundDown(INumVar[] vars) { _vars = vars; }
    
      public override void Main() {
         double[] obj = GetObjCoefs(_vars);
         double[] x   = GetValues(_vars);
         Cplex.IntegerFeasibilityStatus[] feas = GetFeasibilities(_vars);
        
         double objval = ObjValue;
         int    cols   = _vars.Length;
         for (int j = 0; j < cols; j++) {
            // Set the fractional variable to zero and update the objective value
            if ( feas[j].Equals(Cplex.IntegerFeasibilityStatus.Infeasible) ) {
               objval -= x[j] * obj[j];
               x[j] = 0.0;
            }
         }
         SetSolution(_vars, x, objval);
      }
   }

   public static void Main(string[] args) {
      if ( args.Length != 1 ) {
         System.Console.WriteLine("Usage: AdMIPex2 filename");
         System.Console.WriteLine("   where filename is a file with extension ");
         System.Console.WriteLine("      MPS, SAV, or LP (lower case is allowed)");
         System.Console.WriteLine(" Exiting...");
         System.Environment.Exit(-1);
      }
    
      try {
         Cplex cplex = new Cplex();
       
         cplex.ImportModel(args[0]);

         // Check model is all binary except for objective constant variable
         if ( cplex.NbinVars < cplex.Ncols - 1 ) {
            System.Console.WriteLine (
               "Problem contains non-binary variables, exiting.");
            System.Environment.Exit(-1);
         }


         IEnumerator matrixEnum = cplex.GetLPMatrixEnumerator();
         matrixEnum.MoveNext();

         ILPMatrix lp = (ILPMatrix)matrixEnum.Current;
       
         cplex.Use(new RoundDown(lp.NumVars));
       
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
