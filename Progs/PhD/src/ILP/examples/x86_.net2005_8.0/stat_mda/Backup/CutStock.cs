// --------------------------------------------------------------------------
// File: CutStock.cs
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



internal class CutStock {
   internal static double RC_EPS = 1.0e-6;
   
   // Data of the problem
   internal static double _rollWidth;
   internal static double[] _size;
   internal static double[] _amount;
   
   internal static void ReadData(string fileName) {
      InputDataReader reader = new InputDataReader(fileName);
      
      _rollWidth = reader.ReadDouble();
      _size      = reader.ReadDoubleArray();
      _amount    = reader.ReadDoubleArray();
   }
   
   internal static void Report1(Cplex cutSolver,
                                System.Collections.ArrayList Cut,
                                IRange[] Fill) {
      System.Console.WriteLine();
      System.Console.WriteLine("Using " + cutSolver.ObjValue + " rolls");
    
      System.Console.WriteLine();
      for (int j = 0; j < Cut.Count; j++) {
         System.Console.WriteLine("  Cut" + j + " = " +
                                  cutSolver.GetValue((INumVar)Cut[j]));
      }
      System.Console.WriteLine();
      
      for (int i = 0; i < Fill.Length; i++) 
         System.Console.WriteLine("  Fill" + i + " = " + 
                                  cutSolver.GetDual(Fill[i]));
      System.Console.WriteLine();
   }
   
   internal static void Report2(Cplex patSolver, INumVar[] Use) {
      System.Console.WriteLine();
      System.Console.WriteLine("Reduced cost is " + patSolver.ObjValue);
      
      System.Console.WriteLine();
      if (patSolver.ObjValue <= -RC_EPS) {
         for (int i = 0; i < Use.Length; i++) 
            System.Console.WriteLine("  Use" + i + " = "
                               + patSolver.GetValue(Use[i]));
         System.Console.WriteLine();
      }
   }
   
   internal static void Report3(Cplex cutSolver, 
                                System.Collections.ArrayList Cut) {
      System.Console.WriteLine();
      System.Console.WriteLine("Best integer solution uses " + 
                         cutSolver.ObjValue + " rolls");
      System.Console.WriteLine();
      for (int j = 0; j < Cut.Count; j++) 
         System.Console.WriteLine("  Cut" + j + " = " + 
                                  cutSolver.GetValue((INumVar)Cut[j]));
   }


   public static void Main( string[] args ) {
      try {
         string datafile = "../../../../examples/data/cutstock.dat";
         if (args.Length > 0)
            datafile = args[0];
         ReadData(datafile);
         
         /// CUTTING-OPTIMIZATION PROBLEM ///
       
         Cplex cutSolver = new Cplex();
       
         IObjective RollsUsed = cutSolver.AddMinimize();
         IRange[]   Fill = new IRange[_amount.Length];
         for (int f = 0; f < _amount.Length; f++ ) {
            Fill[f] = cutSolver.AddRange(_amount[f], System.Double.MaxValue);
         }
       
         System.Collections.ArrayList Cut = new System.Collections.ArrayList();
       
         int nWdth = _size.Length;
         for (int j = 0; j < nWdth; j++)
            Cut.Add(cutSolver.NumVar(cutSolver.Column(RollsUsed, 1.0).And(
                                     cutSolver.Column(Fill[j],
                                                      (int)(_rollWidth/_size[j]))),
                                     0.0, System.Double.MaxValue));
       
         cutSolver.SetParam(Cplex.IntParam.RootAlg, Cplex.Algorithm.Primal);
       
         /// PATTERN-GENERATION PROBLEM ///
       
         Cplex patSolver = new Cplex();
       
         IObjective ReducedCost = patSolver.AddMinimize();
         INumVar[] Use = patSolver.NumVarArray(nWdth, 
                                               0.0, System.Double.MaxValue, 
                                               NumVarType.Int);
         patSolver.AddRange(-System.Double.MaxValue, 
                            patSolver.ScalProd(_size, Use),
                            _rollWidth);
       
         /// COLUMN-GENERATION PROCEDURE ///
       
         double[] newPatt = new double[nWdth];
       
         /// COLUMN-GENERATION PROCEDURE ///
       
         for (;;) {
            /// OPTIMIZE OVER CURRENT PATTERNS ///
          
            cutSolver.Solve();
            Report1(cutSolver, Cut, Fill);
          
            /// FIND AND ADD A NEW PATTERN ///
          
            double[] price = cutSolver.GetDuals(Fill);
            ReducedCost.Expr = patSolver.Diff(1.0,
                                              patSolver.ScalProd(Use, price));
          
            patSolver.Solve();
            Report2 (patSolver, Use);
          
            if ( patSolver.ObjValue > -RC_EPS )
               break;
          
            newPatt = patSolver.GetValues(Use);
            
            Column column = cutSolver.Column(RollsUsed, 1.0);
            for ( int p = 0; p < newPatt.Length; p++ )
               column = column.And(cutSolver.Column(Fill[p], newPatt[p]));
            
            Cut.Add( cutSolver.NumVar(column, 0.0, System.Double.MaxValue) );
         }
       
         for ( int i = 0; i < Cut.Count; i++ ) {
            cutSolver.Add(cutSolver.Conversion((INumVar)Cut[i],
                                               NumVarType.Int));
         }
       
         cutSolver.Solve();
         Report3 (cutSolver, Cut);
       
         cutSolver.End();
         patSolver.End();
      }
      catch ( ILOG.Concert.Exception exc ) {
         System.Console.WriteLine("Concert exception '" + exc + "' caught");
      }
      catch (System.IO.IOException exc) {
         System.Console.WriteLine("Error reading file " + args[0] + ": " + exc);
      }
      catch (InputDataReader.InputDataReaderException exc ) {
         System.Console.WriteLine(exc);
      }
   }
}


/* Example Input file:
115
[25, 40, 50, 55, 70]
[50, 36, 24, 8, 30]
*/
