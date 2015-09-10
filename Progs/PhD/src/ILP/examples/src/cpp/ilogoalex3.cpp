// -------------------------------------------------------------- -*- C++ -*-
// File: ilogoalex3.cpp
// Version 12.2  
// --------------------------------------------------------------------------
// Licensed Materials - Property of IBM
// 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
// Copyright IBM Corporation 2000, 2010. All Rights Reserved.
//
// US Government Users Restricted Rights - Use, duplication or
// disclosure restricted by GSA ADP Schedule Contract with
// IBM Corp.
// --------------------------------------------------------------------------
//
// ilogoalex3.cpp - Node selectors in goal search
//
//                  This is an extension of example ilogoalex1.cpp
//                  It adds node evaluators to further control the
//                  goal based search.  The node evaluator chooses
//                  the node that is deepest in the tree among those
//                  with maximum sum of integer infeasibilities
//                   

#include <ilcplex/ilocplex.h>

ILOSTLBEGIN

static void usage (const char *progname);


// Branch on var with largest objective coefficient
// among those with largest infeasibility

ILOCPLEXGOAL1(MyBranchGoal, IloNumVarArray, vars) {
   IloNumArray x; 
   IloNumArray obj;
   IntegerFeasibilityArray feas;

   x    = IloNumArray(getEnv());
   obj  = IloNumArray(getEnv());
   feas = IntegerFeasibilityArray(getEnv());
   getValues(x, vars);
   getObjCoefs(obj, vars);
   getFeasibilities(feas, vars);

   IloInt bestj  = -1;
   IloNum maxinf = 0.0;
   IloNum maxobj = 0.0;
   IloInt cols = vars.getSize();
   for (IloInt j = 0; j < cols; j++) {
      if ( feas[j] == Infeasible ) {
         IloNum xj_inf = x[j] - IloFloor (x[j]);
         if ( xj_inf > 0.5 )  xj_inf = 1.0 - xj_inf;
         if ( xj_inf >= maxinf                             &&
             (xj_inf > maxinf || IloAbs (obj[j]) >= maxobj)  ) {
            bestj  = j;
            maxinf = xj_inf;
            maxobj = IloAbs (obj[j]);
         }
      }
   }

   IloCplex::Goal res;
   if ( bestj >= 0 ) {
      res = AndGoal(OrGoal(vars[bestj] >= IloFloor(x[bestj])+1,
                           vars[bestj] <= IloFloor(x[bestj])),
                    this);
   }

   x.end();
   obj.end();
   feas.end();

   return res;
}


// Depth first search node evaluator

class DepthEvaluatorI : public IloCplex::NodeEvaluatorI {
public:
   IloNum evaluate() const {
      return -getDepth();
   }

   IloCplex::NodeEvaluatorI *duplicateEvaluator() {
      return new DepthEvaluatorI();
   }
};

IloCplex::NodeEvaluator DepthEvaluator() {
   return new DepthEvaluatorI();
}


// integer infeasibility sum node evaluator

class IISumEvaluatorI : public IloCplex::NodeEvaluatorI {
public:
   IloNum evaluate() const {
      return -getInfeasibilitySum();
   }

   IloCplex::NodeEvaluatorI *duplicateEvaluator() {
      return new IISumEvaluatorI();
   }
};

IloCplex::NodeEvaluator IISumEvaluator() {
   return new IISumEvaluatorI();
}


int
main (int argc, char **argv)
{
   IloEnv env;
   try {
      IloModel model(env);
      IloCplex cplex(env);
    
      if ( argc != 2 ) {
         usage (argv[0]);
         throw(-1);
      }
    
      IloObjective   obj;
      IloNumVarArray var(env);
      IloRangeArray  rng(env);
      cplex.importModel(model, argv[1], obj, var, rng);
    
      cplex.extract(model); 
      cplex.setParam(IloCplex::MIPSearch, IloCplex::Traditional);
    
      IloCplex::Goal iiSumGoal = IloCplex::Apply(cplex, 
                                                 MyBranchGoal(env, var), 
                                                 IISumEvaluator());
      IloCplex::Goal depthGoal = IloCplex::Apply(cplex,
                                                 iiSumGoal,
                                                 DepthEvaluator());
      cplex.solve(depthGoal);
    
      IloNumArray vals(env);
      cplex.getValues(vals, var);
      cout << "Solution status = " << cplex.getStatus() << endl;
      cout << "Solution value  = " << cplex.getObjValue() << endl;
      cout << "Values          = " << vals << endl;
   }
   catch (IloException& e) {
      cerr << "Concert exception caught: " << e << endl;
   }

   env.end();

   return 0;
}


static void usage (const char *progname)
{
   cerr << "Usage: " << progname << " filename" << endl;
   cerr << "   where filename is a file with extension " << endl;
   cerr << "      MPS, SAV, or LP (lower case is allowed)" << endl;
   cerr << " Exiting..." << endl;
}
