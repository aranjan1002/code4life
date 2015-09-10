// --------------------------------------------------------------------------
// File: TuneSet.cs
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
// TuneSet.cs - Tune parameters for a set of problems
//
// To run this example, command line arguments are required.
// i.e.,   TuneSet [options] file1 file2 ... filen
// where
//     each filei is the name of a file, with .mps, .lp, or
//        .sav extension
//     options are described in Usage().
// Example:
//     TuneSet  mexample.mps
//

using ILOG.Concert;
using ILOG.CPLEX;


public class TuneSet {
   internal static void Usage() {
      System.Console.WriteLine("usage:  TuneSet [options] file1 file2 ... filen");
      System.Console.WriteLine("   where");
      System.Console.WriteLine
          ("      filei is a file with extension MPS, SAV, or LP");
      System.Console.WriteLine("      and options are:");
      System.Console.WriteLine("         -a for average measure");
      System.Console.WriteLine("         -m for minmax measure");
      System.Console.WriteLine
          ("         -f <file> where file is a fixed parameter file");
      System.Console.WriteLine
          ("         -o <file> where file is the tuned parameter file");
   }

   public static void Main(string[] args) {
      if ( args.Length < 1 ) {
         Usage();
         return;
      }
      try {
         Cplex cplex = new Cplex();

         string fixedfile = null;
         string tunedfile = null;
         int tunemeasure = 0;
         bool mset = false;
         System.Collections.ArrayList tmpfilenames = new System.Collections.ArrayList();
         for (int i = 0; i < args.Length; ++i) {
            if ( args[i][0] != '-' )
               tmpfilenames.Add(args[i]);
            switch ( args[i][1] ) {
            case 'a':
               tunemeasure = 1;
               mset = true;
               break;
            case 'm':
               tunemeasure = 2;
               mset = true;
               break;
            case 'f':
               fixedfile = args[++i];
               break;
            case 'o':
               tunedfile = args[++i];
               break;
            }
         }

         string[] filenames = new string[tmpfilenames.Count];
         System.Console.WriteLine("Problem set:");
         for (int i = 0; i < filenames.Length; ++i) {
            filenames[i] = (string)tmpfilenames[i];
            System.Console.WriteLine("  " + filenames[i]);
         }

         if ( mset )
            cplex.SetParam(Cplex.IntParam.TuningMeasure, tunemeasure);

         Cplex.ParameterSet paramset = null;

         if ( fixedfile != null ) {
            cplex.ReadParam(fixedfile);
            paramset = cplex.GetParameterSet();
            cplex.SetDefaults();
         }

         int tunestat = cplex.TuneParam(filenames, paramset);

         if      ( tunestat == Cplex.TuningStatus.Complete)
            System.Console.WriteLine("Tuning complete.");
         else if ( tunestat == Cplex.TuningStatus.Abort)
            System.Console.WriteLine("Tuning abort.");
         else if ( tunestat == Cplex.TuningStatus.TimeLim)
            System.Console.WriteLine("Tuning time limit.");
         else
            System.Console.WriteLine("Tuning status unknown.");

         if ( tunedfile != null ) {
            cplex.WriteParam(tunedfile);
            System.Console.WriteLine("Tuned parameters written to file '" +
                               tunedfile + "'");
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught: " + e);
      }
   }
}
