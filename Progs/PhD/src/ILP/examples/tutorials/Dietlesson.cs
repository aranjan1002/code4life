// --------------------------------------------------------------------------
// File: Dietlesson.cs
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


//Step 2 create the class

//Step 6 set up rows

//Step 7 build rows

//Step 8 add objective for rows

//Step 9 add ranged constraints for rows

//Step 10 set up columns

//Step 11 add empty columns for obj and ranged constraints

//Step 12 create variables for columns  


   public static void Main(string[] args) {


      try {

         string       filename  = "../../../examples/data/diet.dat";
         bool         byColumn  = false;
         NumVarType   varType   = NumVarType.Float;


//Step 16 interpret command line

         int nFoods = data.nFoods;
         int nNutrs = data.nNutrs;
       
//Step 3 create model

//Step 4 create variables

//Step 5 indicate by row or by column

//Step 13 solve
       
//Step 14 display the solution
         
//Step 15 free memory
         
//Step 18 catch

//Step17 show correct use of command line 
}
