// --------------------------------------------------------------------------
// File: Etsp.cs
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
// Etsp.cs - Solving an earliness-tardiness scheduling problem
//           using CPLEX linearization capabilities.
//
// A command line argument indicating the input data file is required
// to run this example.
//
//     Etsp ../../../examples/data/etsp.dat
//

using ILOG.Concert;
using ILOG.CPLEX;

public class Etsp {
   static private int Horizon = 10000;

   internal class Data {
      internal int        nJobs;
      internal int        nResources;
      internal int[][]    activityOnResource;
      internal double[][] duration;
      internal double[]   dueDate;
      internal double[]   earlinessCost;
      internal double[]   tardinessCost;

      internal Data(string filename) {
         InputDataReader reader = new InputDataReader(filename);

         activityOnResource = reader.ReadIntArrayArray();
         duration           = reader.ReadDoubleArrayArray();
         dueDate            = reader.ReadDoubleArray();
         earlinessCost      = reader.ReadDoubleArray();
         tardinessCost      = reader.ReadDoubleArray();

         nJobs      = dueDate.Length;
         nResources = activityOnResource.Length;
      }
   }

  static void Main (string[] args) {
      try {
         string filename = "../../../../examples/data/etsp.dat";
         if ( args.Length > 0)
            filename = args[0];

         Data  data  = new Data(filename);
         Cplex cplex = new Cplex();

         // Create start variables
         INumVar[][] s = new INumVar[data.nJobs][];
         for (int j = 0; j < data.nJobs; j++)
            s[j] = cplex.NumVarArray(data.nResources, 0.0, Horizon);

         // State precedence constraints
         for (int j = 0; j < data.nJobs; j++) {
            for (int i = 1; i < data.nResources; i++)
               cplex.AddGe(s[j][i], cplex.Sum(s[j][i-1], data.duration[j][i-1]));
         }

         // State disjunctive constraints for each resource
         for (int i = 0; i < data.nResources; i++) {
            int end = data.nJobs - 1;
            for (int j = 0; j < end; j++) {
               int a = data.activityOnResource[i][j];
               for (int k = j + 1; k < data.nJobs; k++) {
                  int b = data.activityOnResource[i][k];
                  cplex.Add(cplex.Or(
                     cplex.Ge(s[j][a], cplex.Sum(s[k][b], data.duration[k][b])),
                     cplex.Ge(s[k][b], cplex.Sum(s[j][a], data.duration[j][a]))
                  ));
               }
            }
         }

         // The cost is the sum of earliness or tardiness costs of each job 
         int last = data.nResources - 1;
         INumExpr costSum = cplex.NumExpr();
         for (int j = 0; j < data.nJobs; j++) {
            double[] points = {1,  data.dueDate[j]};
            double[] slopes = {2, -data.earlinessCost[j],
                                   data.tardinessCost[j]};
            costSum = cplex.Sum(costSum,
               cplex.PiecewiseLinear(
                  cplex.Sum(s[j][last], data.duration[j][last]),
                  points, slopes, data.dueDate[j], 0)
            );
         }
         cplex.AddMinimize(costSum);

         cplex.SetParam(Cplex.IntParam.MIPEmphasis, 4);

         if ( cplex.Solve() )
            System.Console.WriteLine(" Optimal Value = " + cplex.ObjValue);

         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught: " + e);
      }    
      catch (InputDataReader.InputDataReaderException ex) {
         System.Console.WriteLine("Data Error: " + ex);
      }
      catch (System.IO.IOException ex) {
         System.Console.WriteLine("IO Error: " + ex);
      }
   }
}

