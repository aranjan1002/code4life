// --------------------------------------------------------------------------
// File: Rates.cs
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
// Rates.cs -- modeling with semi-continuous variables
//
// Problem Description:
// 
// Assume you run a power supply company.  You have several power generators
// available, each of which has a minimum and maximum production level and a
// cost per unit output.  The question is which generators to use in order to
// minimize the overall operation cost while satisfying the demand.
// 
// --------------------------------------------------------------------------

using ILOG.Concert;
using ILOG.CPLEX;


public class Rates {
   internal static int _generators;
   
   internal static double[] _minArray;
   internal static double[] _maxArray;
   internal static double[] _cost;
   internal static double _demand;
   
   internal static void ReadData(string fileName) {
      InputDataReader reader = new InputDataReader(fileName);
    
      _minArray = reader.ReadDoubleArray();
      _maxArray = reader.ReadDoubleArray();
      _cost     = reader.ReadDoubleArray();
      _demand   = reader.ReadDouble();
    
      _generators = _minArray.Length;
   }
   
   public static void Main( string[] args ) {
      try {
         string filename = "../../../../examples/data/rates.dat";
         if (args.Length > 0)
            filename = args[0];
         ReadData(filename);
         
         Cplex cplex = new Cplex();
         
         INumVar[] production = new INumVar[_generators];
         for (int j = 0; j < _generators; ++j) {
            production[j] = cplex.SemiContVar(_minArray[j], _maxArray[j],
                                              NumVarType.Float);
         }
       
         cplex.AddMinimize(cplex.ScalProd(_cost, production));
         cplex.AddGe(cplex.Sum(production), _demand);
       
         cplex.ExportModel("rates.lp");
         if ( cplex.Solve() ) {
            for (int j = 0; j < _generators; ++j) {
               System.Console.WriteLine("   generator " + j + ": " +
                                  cplex.GetValue(production[j]));
            }
            System.Console.WriteLine("Total cost = " + cplex.ObjValue);
         }
         else
            System.Console.WriteLine("No solution");
       
         cplex.End();
      }
      catch (ILOG.Concert.Exception exc) {
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
   generator 0: 15.6
   generator 1: 0
   generator 2: 0
   generator 3: 27.8
   generator 4: 27.8
   generator 5: 28.8
   generator 6: 29
   generator 7: 29
   generator 8: 29
Total cost = 1625.24
*/
