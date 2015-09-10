// --------------------------------------------------------------------------
// File: Facility.cs
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

using ILOG.Concert;
using ILOG.CPLEX;


public class Facility {
   internal static double[]   _capacity;
   internal static double[]   _fixedCost;
   internal static double[][] _cost;

   internal static int _nbLocations;
   internal static int _nbClients;

   internal static void ReadData(string fileName) {
      System.Console.WriteLine("Reading data from " + fileName);
      InputDataReader reader = new InputDataReader(fileName);
    
      _capacity  = reader.ReadDoubleArray();
      _fixedCost = reader.ReadDoubleArray();
      _cost      = reader.ReadDoubleArrayArray();
    
      _nbLocations = _capacity.Length;
      _nbClients   = _cost.Length;
    
      for(int i = 0; i < _nbClients; i++)
         if ( _cost[i].Length != _nbLocations )
           throw new ILOG.Concert.Exception("inconsistent data in file " + fileName);
   }

   public static void Main( string[] args ) {
      try {
         string filename  = "../../../../examples/data/facility.dat";
         if (args.Length > 0)
            filename = args[0];
         ReadData(filename);
       
         Cplex cplex = new Cplex();
         INumVar[] open = cplex.BoolVarArray(_nbLocations);
       
         INumVar[][] supply = new INumVar[_nbClients][];
         for(int i = 0; i < _nbClients; i++)
            supply[i] = cplex.BoolVarArray(_nbLocations);
       
         for(int i = 0; i < _nbClients; i++)
            cplex.AddEq(cplex.Sum(supply[i]), 1);
       
         for(int j = 0; j < _nbLocations; j++) {
            ILinearNumExpr v = cplex.LinearNumExpr();
            for(int i = 0; i < _nbClients; i++)
               v.AddTerm(1.0, supply[i][j]);
            cplex.AddLe(v, cplex.Prod(_capacity[j], open[j]));
         }
       
         ILinearNumExpr obj = cplex.ScalProd(_fixedCost, open);
         for(int i = 0; i < _nbClients; i++)
            obj.Add(cplex.ScalProd(_cost[i], supply[i]));
       
         cplex.AddMinimize(obj);
       
         if (cplex.Solve()) {
            double tolerance = cplex.GetParam(Cplex.DoubleParam.EpInt);
            System.Console.WriteLine("Optimal value: " + cplex.ObjValue);
            for(int j = 0; j < _nbLocations; j++) {
               if (cplex.GetValue(open[j]) >= 1 - tolerance) {
                  System.Console.Write("Facility " + j +" is open, it serves clients ");
                  for(int i = 0; i < _nbClients; i++)
                     if (cplex.GetValue(supply[i][j]) >= 1 - tolerance)
                        System.Console.Write(" " + i);
                  System.Console.WriteLine(); 
               }
            }
         }
         cplex.End();
      }
      catch(ILOG.Concert.Exception exc) {
         System.Console.WriteLine("Concert exception '" + exc + "' caught");
      }
      catch (System.IO.IOException exc) {
         System.Console.WriteLine("Error reading file " + args[0] + ": " + exc);
      }
      catch (InputDataReader.InputDataReaderException exc) {
         System.Console.WriteLine(exc);
      }
   }
}

/*
Optimal value: 1383
Facility 0 is open, it serves clients 2 5 7
Facility 1 is open, it serves clients 3
Facility 3 is open, it serves clients 0 1 4 6
*/
