// --------------------------------------------------------------------------
// File: AdMIPex3.cs
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
// AdMIPex3.cs -  Using the branch callback for optimizing a MIP
//                problem with Special Ordered Sets Type 1, with
//                all the variables binary
//
// To run this example, command line arguments are required.
// i.e.,   AdMIPex3   filename
//
// Example:
//     AdMIPex3  example.mps
//

using ILOG.Concert;
using ILOG.CPLEX;
using System.Collections;


public class AdMIPex3 {
   internal static double EPS = 1.0e-4;

   public class SOSbranch : Cplex.BranchCallback {
      internal ISOS1[] _sos;
    
      public SOSbranch(ISOS1[] sos) { 
         _sos = sos; 
      }
    
      public override void Main() {
         double bestx = EPS;
         int    besti = -1;
         int    bestj = -1;
         int    num   = _sos.Length;
       
         INumVar[] var = null;
         double[]  x   = null;
       
         for (int i = 0; i < num; ++i) {
            if ( GetSOSFeasibility(_sos[i]) 
                 .Equals(Cplex.IntegerFeasibilityStatus.Infeasible) ) {
               var = _sos[i].NumVars;
               x = GetValues(var);
             
               int n = var.Length;
               for (int j = 0; j < n; ++j) {
                  double inf = System.Math.Abs(x[j] - System.Math.Round(x[j]));
                  if ( inf > bestx ) {
                     bestx = inf;
                     besti = i;
                     bestj = j;
                  }
               }
            }
         }
       
         if ( besti >= 0 ) {
            var = _sos[besti].NumVars;
            int n = var.Length;
          
            Cplex.BranchDirection[] dir = new Cplex.BranchDirection[n];
            double[]                   val = new double[n];
          
            for (int j = 0; j < n; ++j) {
               if ( j != bestj ) {
                  dir[j] = Cplex.BranchDirection.Down;
                  val[j] = 0.0;
               } else {
                  dir[j] = Cplex.BranchDirection.Up;
                  val[j] = 1.0;
               }
            }
            MakeBranch(var, val, dir, ObjValue);
            MakeBranch(var[bestj], 0.0, 
                       Cplex.BranchDirection.Down, ObjValue);
         }      
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
       
         if ( cplex.NSOS1 > 0 ) {
            ISOS1[] sos1 = new ISOS1[cplex.NSOS1];
            int i = 0;
            for (IEnumerator sosenum = cplex.GetSOS1Enumerator(); 
                 sosenum.MoveNext(); ++i) {
               sos1[i] = (ISOS1)sosenum.Current;
            }
            cplex.Use(new SOSbranch(sos1));
            System.Console.WriteLine("using SOS branch callback for " +
                                     sos1.Length + " SOS1s");
         }
       
	 cplex.SetParam(Cplex.IntParam.MIPSearch, Cplex.MIPSearch.Traditional);
         if (cplex.Solve()) {
            IEnumerator matrixEnum = cplex.GetLPMatrixEnumerator();
            matrixEnum.MoveNext();

            ILPMatrix matrix = (ILPMatrix)matrixEnum.Current;
            double[] vals = cplex.GetValues(matrix);
          
            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception exc) {
         System.Console.WriteLine("Concert exception caught: " + exc);
      }
   }
}
