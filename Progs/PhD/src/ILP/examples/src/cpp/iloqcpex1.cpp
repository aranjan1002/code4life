// -------------------------------------------------------------- -*- C++ -*-
// File: iloqcpex1.cpp
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
// iloqcpex1.cpp - Entering and optimizing a quadratically constrained
//                 problem.

#include <ilcplex/ilocplex.h>
ILOSTLBEGIN

static void
   populatebyrow     (IloModel model, IloNumVarArray var, IloRangeArray con);

int
main (int argc, char **argv)
{
   IloEnv   env;
   try {
      IloModel model(env);
      IloNumVarArray var(env);
      IloRangeArray con(env);

      populatebyrow (model, var, con);

      IloCplex cplex(model);

      // Optimize the problem and obtain solution.
      if ( !cplex.solve() ) {
         env.error() << "Failed to optimize LP" << endl;
         throw(-1);
      }

      IloNumArray vals(env);
      env.out() << "Solution status = " << cplex.getStatus() << endl;
      env.out() << "Solution value  = " << cplex.getObjValue() << endl;
      cplex.getValues(vals, var);
      env.out() << "Values        = " << vals << endl;
      cplex.getSlacks(vals, con);
      env.out() << "Slacks        = " << vals << endl;

      cplex.exportModel("qcpex1.lp");
   }
   catch (IloException& e) {
      cerr << "Concert exception caught: " << e << endl;
   }
   catch (...) {
      cerr << "Unknown exception caught" << endl;
   }

   env.end();

   return 0;
}  // END main


// To populate by row, we first create the variables, and then use them to
// create the range constraints and objective.  The model we create is:
//
//    Maximize
//     obj: x1 + 2 x2 + 3 x3
//            - 0.5 ( 33*x1*x1 + 22*x2*x2 + 11*x3*x3
//                             - 12*x1*x2 - 23*x2*x3 )
//    Subject To
//     c1: - x1 + x2 + x3 <= 20
//     c2: x1 - 3 x2 + x3 <= 30
//     q1: [ x1^2 + x2^2 + x3^2 ] <= 1.0
//    Bounds
//     0 <= x1 <= 40
//    End

static void
populatebyrow (IloModel model, IloNumVarArray x, IloRangeArray c)
{
   IloEnv env = model.getEnv();

   x.add(IloNumVar(env, 0.0, 40.0));
   x.add(IloNumVar(env));
   x.add(IloNumVar(env));
   model.add(IloMaximize(env, x[0] + 2 * x[1] + 3 * x[2]
                            - 0.5 * (33*x[0]*x[0] + 22*x[1]*x[1] +
                                     11*x[2]*x[2] - 12*x[0]*x[1] -
                                     23*x[1]*x[2]                 ) ));

   c.add( - x[0] +     x[1] + x[2] <= 20);
   c.add(   x[0] - 3 * x[1] + x[2] <= 30);
   c.add(x[0]*x[0] + x[1]*x[1] + x[2]*x[2] <= 1.0);
   model.add(c);
}  // END populatebyrow
