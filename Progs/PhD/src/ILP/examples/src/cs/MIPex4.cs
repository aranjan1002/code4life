// --------------------------------------------------------------------------
// File: MIPex4.cs
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
// MIPex4.cs - Reading in and optimizing a problem using a callback
//             to log or interrupt or a Cplex.Aborter to interrupt
//
// To run this example, command line arguments are required.
// i.e.,   MIPex4   filename option
//                     where option is one of
//                        t to use the time-limit-gap callback
//                        l to use the logging callback
//                        a to use the aborter
//
// Example:
//     MIPex4  mexample.mps l
//

using ILOG.Concert;
using ILOG.CPLEX;
using System.Collections;


public class MIPex4 {
   internal static void Usage() {
      System.Console.WriteLine("usage:  MIPex4 <filename> <option>");
      System.Console.WriteLine("        t  to use the time-limit-gap callback");
      System.Console.WriteLine("        l  to use the logging callback");
      System.Console.WriteLine("        a  to use the aborter");
   }

   // Spend at least timeLimit seconds on optimization, but once
   // this limit is reached, quit as soon as the solution is acceptable

   internal class TimeLimitCallback : Cplex.MIPInfoCallback {
      internal Cplex  _cplex;
      internal bool   _aborted;
      internal double _timeStart;
      internal double _timeLimit;
      internal double _acceptableGap;

      internal TimeLimitCallback(Cplex cplex, bool aborted, double timeStart,
                                 double timeLimit, double acceptableGap) {
         _cplex         = cplex;
         _aborted       = aborted;
         _timeStart     = timeStart;
         _timeLimit     = timeLimit;
         _acceptableGap = acceptableGap;
      }

      public override void Main() {
         if ( !_aborted  &&  HasIncumbent() ) {
            double gap = 100.0 * MIPRelativeGap; 
            double timeUsed = _cplex.CplexTime - _timeStart;
            if ( timeUsed > _timeLimit && gap < _acceptableGap ) {
               System.Console.WriteLine("");
               System.Console.WriteLine("Good enough solution at "
                                  + timeUsed + " sec., gap = "
                                  + gap  + "%, quitting.");
               _aborted = true;
               Abort();
            }
         }
      }
   }

   // Log new incumbents if they are at better than the old by a
   // relative tolerance of 1e-5; also log progress info every
   // 100 nodes.

   internal class LogCallback : Cplex.MIPInfoCallback {
      INumVar[] _var;
      int       _lastLog;
      double    _lastIncumbent;

      internal LogCallback(INumVar[] var, int lastLog, double lastIncumbent) {
         _var = var;
         _lastLog = lastLog;
         _lastIncumbent = lastIncumbent;
      }

      public override void Main() {
         bool newIncumbent = false;
         int  nodes        = Nnodes;

         if ( HasIncumbent()                                      &&
              System.Math.Abs(_lastIncumbent - IncumbentObjValue)
                  > 1e-5*(1.0 + System.Math.Abs(IncumbentObjValue)) ) {
            _lastIncumbent = IncumbentObjValue;
            newIncumbent = true;
         }
         if ( nodes >= _lastLog + 100  ||  newIncumbent ) { 
            if ( !newIncumbent )  _lastLog = nodes;
            System.Console.Write("Nodes = " + nodes
                                 + "(" +  NremainingNodes + ")"
                                 + "  Best Objective = " + BestObjValue);
            if ( HasIncumbent() ) {
               System.Console.WriteLine ("  Incumbent Objective = " +
                                         IncumbentObjValue);
            }
            else {
               System.Console.WriteLine("");
            }
         }

         if ( newIncumbent ) {
            System.Console.WriteLine("New incumbent values: ");

            int n = _var.Length;
            double[] x = GetIncumbentValues(_var, 0, n);
            for (int j = 0; j < n; j++) {
               System.Console.WriteLine("Variable " + j + ": Value = " + x[j]);
            }
         }
      }
   }

   public static void Main(string[] args) {
      if ( args.Length != 2 ) {
         Usage();
         return;
      }
      try {
         bool useLoggingCallback = false;
         bool useTimeLimitCallback = false;
         bool useAborter = false;

         Cplex.Aborter myAborter;

         switch ( args[1].ToCharArray()[0] ) {
         case 't':
            useTimeLimitCallback = true;
            break;
         case 'l':
            useLoggingCallback = true;
            break;
         case 'a':
            useAborter = true;
            break;
         default:
            Usage();
            return;
         }

         Cplex cplex = new Cplex();

         cplex.ImportModel(args[0]);
         IEnumerator matrixEnum = cplex.GetLPMatrixEnumerator();
         matrixEnum.MoveNext();
         ILPMatrix lp = (ILPMatrix)matrixEnum.Current;
         IObjective obj = cplex.GetObjective();

         // Set an overall node limit in case callback conditions
         // are not met.
         cplex.SetParam(Cplex.IntParam.NodeLim, 5000);

         if ( useLoggingCallback ) {
            double lastObjVal =
               (obj.Sense == ObjectiveSense.Minimize ) ?
                  System.Double.MaxValue : -System.Double.MaxValue;

            cplex.Use(new LogCallback(lp.NumVars, -100000, lastObjVal));
            // Turn off CPLEX logging
            cplex.SetParam(Cplex.IntParam.MIPDisplay, 0);
         }
         else if ( useTimeLimitCallback ) {
            cplex.Use(new TimeLimitCallback(cplex, false, cplex.CplexTime,
                                            1.0, 10.0));
         }
         else if ( useAborter ) {
            myAborter =  new Cplex.Aborter();
            cplex.Use(myAborter);
            // Typically, you would pass the Aborter object to
            // another thread or pass it to an interrupt handler,
            // and  monitor for some event to occur.  When it does,
            // call the Aborter's abort method.
            //
            // To illustrate its use without creating a thread or
            // an interrupt handler, abort immediately by calling
            // abort before the solve.
            //
            myAborter.Abort();
         }

         cplex.Solve();

         System.Console.WriteLine("Solution status = " + cplex.GetStatus());
         System.Console.WriteLine("Cplex status = " + cplex.GetCplexStatus());

         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught: " + e);
      }
   }
}
