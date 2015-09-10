// --------------------------------------------------------------------------
// File: Populate.cs
// Version 12.2  
// --------------------------------------------------------------------------
// Licensed Materials - Property of IBM
// 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
// Copyright IBM Corporation 2007, 2010. All Rights Reserved.
//
// US Government Users Restricted Rights - Use, duplication or
// disclosure restricted by GSA ADP Schedule Contract with
// IBM Corp.
// --------------------------------------------------------------------------
//
// Populate.cs - Reading in and generating multiple solutions to a MIP
//               problem.
//
// To run this example, command line arguments are required.
// i.e.,   Populate  filename
// where 
//     filename is the name of the file, with .mps, .lp, or .sav extension
// Example:
//     Populate  location.lp
//

using ILOG.Concert;
using ILOG.CPLEX;
using System.Collections;


public class Populate {
   const double EPSZERO = 1.0E-10;
   internal static void Usage() {
      System.Console.WriteLine("usage:  Populate <filename>");
   }

   public static void Main(string[] args) {
      if ( args.Length != 1 ) {
         Usage();
         return;
      }
      try {
         Cplex cplex = new Cplex();
       
         cplex.ImportModel(args[0]);
	 
         /* Set the solution pool relative gap parameter to obtain solutions
            of objective value within 10% of the optimal */

         cplex.SetParam(Cplex.DoubleParam.SolnPoolGap, 0.1);

         if ( cplex.Populate() ) {
            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Incumbent objective value  = " +
                                     cplex.ObjValue);
          
            // access ILPMatrix object that has been read from file in order to
            // access variables which are the columns of the lp.  Method
            // importModel guarantees to the model into exactly on ILPMatrix
            // object which is why there are no test or iterations needed in the
            // following line of code.

            IEnumerator matrixEnum = cplex.GetLPMatrixEnumerator();
            matrixEnum.MoveNext();

            ILPMatrix lp = (ILPMatrix)matrixEnum.Current;
          
            double[] incx = cplex.GetValues(lp);
            for (int j = 0; j < incx.Length; j++) {
               System.Console.WriteLine("Variable " + j + ": Value = " + incx[j]);
            }
            System.Console.WriteLine();

            /* Get the number of solutions in the solution pool */

            int numsol = cplex.SolnPoolNsolns;
            System.Console.WriteLine("The solution pool contains " + numsol +
                               " solutions.");

            /* Some solutions are deleted from the pool because of the
               solution pool relative gap parameter */

            int numsolreplaced = cplex.SolnPoolNreplaced;
            System.Console.WriteLine(numsolreplaced +
                               " solutions were removed due to the " +
                               "solution pool relative gap parameter.");

            System.Console.WriteLine("In total, " + (numsol + numsolreplaced) +
                               " solutions were generated.");

            /* Get the average objective value of solutions in the
               solution pool */

            System.Console.WriteLine(
                "The average objective value of the solutions is " +
                 cplex.SolnPoolMeanObjValue + ".");

            System.Console.WriteLine();

            /* Write out the objective value of each solution and its
               difference to the incumbent */

            for (int i = 0; i < numsol; i++) {

                double[] x = cplex.GetValues(lp, i);

                /* Compute the number of variables that differ in the
                   solution and in the incumbent */

                int numdiff = 0;
                for (int j = 0; j < x.Length; ++j) {
                    if ( System.Math.Abs(x[j] - incx[j]) > EPSZERO )
                        numdiff++;
                }
                System.Console.WriteLine("Solution " + i +
                                         " with objective " +
                                         cplex.GetObjValue(i) +
                                         " differs in " +
                                         numdiff + " of " + x.Length +
                                         " variables.");

	    }
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught: " + e);
      }
   }
}
