// --------------------------------------------------------------------------
// File: MIPex2.cs
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
// MIPex2.cs - Reading in and optimizing a MIP problem.  In fact, this
//             program also works for LP or QP problems, but is different
//             from LPex2 in that no dual solution information is queried.
//
// To run this example, command line arguments are required.
// i.e.,   MIPex2  filename
// where 
//     filename is the name of the file, with .mps, .lp, or .sav extension
// Example:
//     MIPex2  mexample.mps
//

using ILOG.Concert;
using ILOG.CPLEX;
using System.Collections;


public class MIPex2 {
   internal static void Usage() {
      System.Console.WriteLine("usage:  MIPex2 <filename>");
   }

   public static void Main(string[] args) {
      if ( args.Length != 1 ) {
         Usage();
         return;
      }
      try {
         Cplex cplex = new Cplex();
       
         cplex.ImportModel(args[0]);
       
         if ( cplex.Solve() ) {
            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);
          
            // access ILPMatrix object that has been read from file in order to
            // access variables which are the columns of the lp.  Method
            // importModel guarantees to the model into exactly on ILPMatrix
            // object which is why there are no test or iterations needed in the
            // following line of code.

            IEnumerator matrixEnum = cplex.GetLPMatrixEnumerator();
            matrixEnum.MoveNext();

            ILPMatrix lp = (ILPMatrix)matrixEnum.Current;
          
            double[] x = cplex.GetValues(lp);
            for (int j = 0; j < x.Length; ++j) {
               System.Console.WriteLine("Variable " + j + ": Value = " + x[j]);
            }
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught: " + e);
      }
   }
}
