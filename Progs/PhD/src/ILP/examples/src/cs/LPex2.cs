// --------------------------------------------------------------------------
// File: LPex2.cs
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
// LPex2.cs - Reading in and optimizing an LP problem
//
//
// To run this example, command line arguments are required.
// i.e.,   LPex2   filename   method
// where 
//     filename is the name of the file, with .mps, .lp, or .sav extension
//     method   is the optimization method
//                 o          default
//                 p          primal simplex
//                 d          dual   simplex
//                 h          barrier with crossover
//                 b          barrier without crossover
//                 n          network with dual simplex cleanup
//                 s          sifting
//                 c          concurrent optimization
// Example:
//     LPex2  example.mps  o
//

using ILOG.Concert;
using ILOG.CPLEX;
using System.Collections;


public class LPex2 {
   internal static void Usage() {
      System.Console.WriteLine("usage:  LPex2 <filename> <method>");
      System.Console.WriteLine("          o       default");
      System.Console.WriteLine("          p       primal simplex");
      System.Console.WriteLine("          d       dual   simplex");
      System.Console.WriteLine("          h       barrier with crossover");
      System.Console.WriteLine("          b       barrier without crossover");
      System.Console.WriteLine("          n       network with dual simplex cleanup");
      System.Console.WriteLine("          s       sifting");
      System.Console.WriteLine("          c       concurrent optimization");
   }

   public static void Main(string[] args) {
      if ( args.Length != 2 ) {
         Usage();
         return;
      }
      try {
         // Create the modeler/solver object
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
         case 'h': cplex.SetParam(Cplex.IntParam.RootAlg,
                                  Cplex.Algorithm.Barrier);
                   break;
         case 'b': cplex.SetParam(Cplex.IntParam.RootAlg,
                                  Cplex.Algorithm.Barrier);
                   cplex.SetParam(Cplex.IntParam.BarCrossAlg,
                                  Cplex.Algorithm.None);
                   break;
         case 'n': cplex.SetParam(Cplex.IntParam.RootAlg,
                                  Cplex.Algorithm.Network);
                   break;
         case 's': cplex.SetParam(Cplex.IntParam.RootAlg,
                                  Cplex.Algorithm.Sifting);
                   break;
         case 'c': cplex.SetParam(Cplex.IntParam.RootAlg,
                                  Cplex.Algorithm.Concurrent);
                   break;
         default:  Usage();
                   return;
         }
       
         // Read model from file with name args[0] into cplex optimizer object
         cplex.ImportModel(args[0]);

       
         // Solve the model and display the solution if one was found
         if ( cplex.Solve() ) {
            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);
          
            // Access the ILPMatrix object that has been read from a file in
            // order to access variables which are the columns of the LP.  The
            // method importModel() guarantees that exactly one ILPMatrix
            // object will exist, which is why no tests or enumerators are
            // needed in the following line of code.

            IEnumerator matrixEnum = cplex.GetLPMatrixEnumerator();
            matrixEnum.MoveNext();

            ILPMatrix lp = (ILPMatrix)matrixEnum.Current;
          
            double[] x = cplex.GetValues(lp);
            for (int j = 0; j < x.Length; ++j)
               System.Console.WriteLine("Variable " + j + ": Value = " + x[j]);

            try {     // basis may not exist
               Cplex.BasisStatus[] cstat =
                     cplex.GetBasisStatuses(lp.NumVars);
               for (int j = 0; j < x.Length; ++j)
                  System.Console.WriteLine("Variable " + j + ": Basis Status = " + cstat[j]);
            }
            catch (System.Exception) {
            }

            System.Console.WriteLine("Maximum bound violation = " +
                   cplex.GetQuality(Cplex.QualityType.MaxPrimalInfeas));
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught: " + e);
      }
   }
}
