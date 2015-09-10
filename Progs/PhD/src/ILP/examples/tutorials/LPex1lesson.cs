// --------------------------------------------------------------------------
// File: LPex1lesson.cs
// --------------------------------------------------------------------------
// Licensed Materials - Property of IBM
// 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
// Copyright IBM Corporation 2001, 2010. All Rights Reserved.
//
// Note to U.S. Government Users Restricted Rights:
// Use, duplication or disclosure restricted by GSA ADP Schedule
// Contract with IBM Corp.
// --------------------------------------------------------------------------

using ILOG.Concert;
using ILOG.CPLEX;


public class LPex1 {
// Step 7

   public static void Main(string[] args) {
      if ( args.Length != 1 || args[0].ToCharArray()[0] != '-' ) {
         Usage();
         return;
      }
    
      try {
//Step 3
       
         INumVar[][] var = new INumVar[1][];
         IRange[][]  rng = new IRange[1][];
       
// Step 8
       
// Step 11

       
// Step 9

// Step 10

         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception '" + e + "' caught");
      }
   }

// Step 4


// Step 5


// Step 6

}

