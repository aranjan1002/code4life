// --------------------------------------------------------------------------
// File: QPex2.cs
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
// QPex2.cs - Reading in and optimizing a QP problem
//
// To run this example, command line arguments are required.
// i.e.,   QPex2  filename method
// where
//     filename is the name of the file, with .mps, .lp, or .sav extension
//     method   is the optimization method
//                 o          default
//                 p          primal simplex
//                 d          dual   simplex
//                 b          barrier without crossover
//                 n          network with dual simplex cleanup
//
// Example:
//     QPex2  qpex.lp o
//

using ILOG.Concert;
using ILOG.CPLEX;
using System.Collections;


public class QPex2 {
   internal static void Usage() {
      System.Console.WriteLine("usage:  QPex2 <filename> <method>");
      System.Console.WriteLine("          o       default");
      System.Console.WriteLine("          p       primal simplex");
      System.Console.WriteLine("          d       dual   simplex");
      System.Console.WriteLine("          b       barrier without crossover");
      System.Console.WriteLine("          n       network with dual simplex cleanup");
   }

   public static void Main(string[] args) {
      if ( args.Length != 2 ) {
         Usage();
         return;
      }
      try {
         Cplex cplex = new Cplex();

         // Evaluate command line option and set optimization method accordingly.
         switch ( args[1].ToCharArray()[0] ) {
         case 'o': cplex.SetParam(Cplex.IntParam.RootAlg,
                                  Cplex.Algorithm.Auto);
                   break;
         case 'p': cplex.SetParam(Cplex.IntParam.RootAlg,
                                  Cplex.Algorithm.Primal);
                   break;
         case 'd': cplex.SetParam(Cplex.IntParam.RootAlg,
                                  Cplex.Algorithm.Dual);
                   break;
         case 'b': cplex.SetParam(Cplex.IntParam.RootAlg,
                                  Cplex.Algorithm.Barrier);
                   cplex.SetParam(Cplex.IntParam.BarCrossAlg,
                                  Cplex.Algorithm.None);
                   break;
         case 'n': cplex.SetParam(Cplex.IntParam.RootAlg,
                                  Cplex.Algorithm.Network);
                   break;
         default:  Usage();
                   return;
         }

         cplex.ImportModel(args[0]);


         if ( cplex.Solve() ) {
            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);

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
