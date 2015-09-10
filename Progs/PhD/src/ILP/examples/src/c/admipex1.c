/* --------------------------------------------------------------------------
 * File: admipex1.c
 * Version 12.2
 * --------------------------------------------------------------------------
 * Licensed Materials - Property of IBM
 * 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
 * Copyright IBM Corporation 1997, 2010. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with
 * IBM Corp.
 * --------------------------------------------------------------------------
 */

/* admipex1.c - Use the node, branch and solve callbacks
                for optimizing a MIP problem */

/* To run this example, command line arguments are required:
       admipex1 [-r] filename
   where 
       filename  Name of the file, with .mps, .lp, or .sav
                 extension, and a possible additional .gz 
                 extension.
       -r        Indicates that callbacks will refer to the
                 presolved model.
   Example:
       admipex1  mexample.mps
       admipex1 -r mexample.mps */

/* Bring in the CPLEX function declarations and the C library 
   header file stdio.h with the following single include */

#include <ilcplex/cplex.h>

/* Bring in the declarations for the string and character functions,
   malloc, fabs, and floor */

#include <ctype.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

/* Declarations for functions in this program */


static int CPXPUBLIC 
   usersolve      (CPXCENVptr env, void *cbdata, int wherefrom,
                   void *cbhandle, int *useraction_p);
static int CPXPUBLIC
   usersetbranch  (CPXCENVptr env, void *cbdata, int wherefrom,
                   void *cbhandle, int brtype, int sos, int nodes,
                   int bdcnt, const double *nodeest, const int *nodebeg,
                   const int *indices, const char *lu, const int *bd,
                   int *useraction_p);
static int CPXPUBLIC 
   userselectnode (CPXCENVptr env, void *cbdata, int wherefrom,
                   void *cbhandle, int *nodeid_p,
                   int *useraction_p);

static void
   free_and_null (char **ptr),
   usage         (char *progname);



int
main (int  argc,
      char *argv[])
{
   int status = 0;

   /* Declare and allocate space for the variables and arrays where
      we will store the optimization results, including the status, 
      objective value, and variable values */
   
   int    solstat;
   double objval;
   double *x = NULL;
   
   CPXENVptr env = NULL;
   CPXLPptr  lp = NULL;

   int j;
   int cur_numcols;
   int wantorig = 1;
   int nameind = 1;

   /* Check the command line arguments */

   if ( argc != 2 ) {
      if ( argc != 3         ||
           argv[1][0] != '-' ||
           argv[1][1] != 'r'   ) {
         usage (argv[0]);
         goto TERMINATE;
      }
      wantorig = 0;
      nameind = 2;
   }

   /* Initialize the CPLEX environment */

   env = CPXopenCPLEX (&status);

   /* If an error occurs, the status value indicates the reason for
      failure.  A call to CPXgeterrorstring will produce the text of
      the error message.  Note that CPXopenCPLEX produces no
      output, so the only way to see the cause of the error is to use
      CPXgeterrorstring.  For other CPLEX routines, the errors will
      be seen if the CPX_PARAM_SCRIND parameter is set to CPX_ON */

   if ( env == NULL ) {
      char errmsg[1024];
      fprintf (stderr, "Could not open CPLEX environment.\n");
      CPXgeterrorstring (env, status, errmsg);
      fprintf (stderr, "%s", errmsg);
      goto TERMINATE;
   }

   /* Turn on output to the screen */

   status = CPXsetintparam (env, CPX_PARAM_SCRIND, CPX_ON);
   if ( status ) {
      fprintf (stderr, 
               "Failure to turn on screen indicator, error %d.\n",
               status);
      goto TERMINATE;
   }

   /* Create the problem, using the filename as the problem name */

   lp = CPXcreateprob (env, &status, argv[nameind]);

   /* A returned pointer of NULL may mean that not enough memory
      was available or there was some other problem.  In the case of
      failure, an error message will have been written to the error
      channel from inside CPLEX.  In this example, the setting of
      the parameter CPX_PARAM_SCRIND causes the error message to
      appear on stdout.  Note that most CPLEX routines return
      an error code to indicate the reason for failure */

   if ( lp == NULL ) {
      fprintf (stderr, "Failed to create LP.\n");
      goto TERMINATE;
   }

   /* Now read the file, and copy the data into the created lp */

   status = CPXreadcopyprob (env, lp, argv[nameind], NULL);
   if ( status ) {
      fprintf (stderr,
               "Failed to read and copy the problem data.\n");
      goto TERMINATE;
   }

   /* Set up to use MIP callbacks */

   status = CPXsetnodecallbackfunc (env, userselectnode, NULL)  ||
            CPXsetbranchcallbackfunc (env, usersetbranch, NULL) ||
            CPXsetsolvecallbackfunc (env, usersolve, NULL);

   if ( wantorig ) {
      /* Assure linear mappings between the presolved and original
         models */

      status = CPXsetintparam (env, CPX_PARAM_PRELINEAR, 0);
      if ( status )  goto TERMINATE;

      /* Let MIP callbacks work on the original model */

      status = CPXsetintparam (env, CPX_PARAM_MIPCBREDLP, CPX_OFF);
      if ( status )  goto TERMINATE;
   }

   /* Set MIP log interval to 1 */

   status = CPXsetintparam (env, CPX_PARAM_MIPINTERVAL, 1);
   if ( status )  goto TERMINATE;

   /* Turn on traditional search for use with control callbacks */

   status = CPXsetintparam (env, CPX_PARAM_MIPSEARCH, CPX_MIPSEARCH_TRADITIONAL);
   if ( status )  goto TERMINATE;



   /* Optimize the problem and obtain solution */

   status = CPXmipopt (env, lp);
   if ( status ) {
      fprintf (stderr, "Failed to optimize MIP.\n");
      goto TERMINATE;
   }

   solstat = CPXgetstat (env, lp);
   printf ("Solution status %d.\n", solstat);

   status  = CPXgetobjval (env, lp, &objval);
   if ( status ) {
      fprintf (stderr,"Failed to obtain objective value.\n");
      goto TERMINATE;
   }

   printf ("Objective value %.10g\n", objval);

   cur_numcols = CPXgetnumcols (env, lp);

   /* Allocate space for solution */

   x = (double *) malloc (cur_numcols * sizeof (double));
   if ( x == NULL ) {
      fprintf (stderr, "No memory for solution values.\n");
      goto TERMINATE;
   }

   status = CPXgetx (env, lp, x, 0, cur_numcols-1);
   if ( status ) {
      fprintf (stderr, "Failed to obtain solution.\n");
      goto TERMINATE;
   }

   /* Write out the solution */ 

   for (j = 0; j < cur_numcols; j++) {
      if ( fabs (x[j]) > 1e-10 ) {
         printf ( "Column %d:  Value = %17.10g\n", j, x[j]);
      }
   }


TERMINATE:

   /* Free the solution vector */

   free_and_null ((char **) &x);

   /* Free the problem as allocated by CPXcreateprob and
      CPXreadcopyprob, if necessary */

   if ( lp != NULL ) {
      status = CPXfreeprob (env, &lp);
      if ( status ) {
         fprintf (stderr, "CPXfreeprob failed, error code %d.\n",
                  status);
      }
   }

   /* Free the CPLEX environment, if necessary */

   if ( env != NULL ) {
      status = CPXcloseCPLEX (&env);

      /* Note that CPXcloseCPLEX produces no output, so the only 
         way to see the cause of the error is to use
         CPXgeterrorstring.  For other CPLEX routines, the errors 
         will be seen if the CPX_PARAM_SCRIND parameter is set to 
         CPX_ON */

      if ( status ) {
         char errmsg[1024];
         fprintf (stderr, "Could not close CPLEX environment.\n");
         CPXgeterrorstring (env, status, errmsg);
         fprintf (stderr, "%s", errmsg);
      }
   }
     
   return (status);

} /* END main */


/* This simple routine frees up the pointer *ptr, and sets *ptr to 
   NULL */

static void
free_and_null (char **ptr)
{
   if ( *ptr != NULL ) {
      free (*ptr);
      *ptr = NULL;
   }
} /* END free_and_null */ 


static void
usage (char *progname)
{
   fprintf (stderr,
    "Usage: %s [-r] filename\n", progname);
   fprintf (stderr,
    "  filename   Name of a file, with .mps, .lp, or .sav\n");
   fprintf (stderr,
    "             extension, and a possible, additional .gz\n"); 
   fprintf (stderr,
    "             extension\n");
   fprintf (stderr,
    "  -r         Indicates that callbacks will refer to the\n");
   fprintf (stderr,
    "             presolved model\n");
} /* END usage */


static int CPXPUBLIC 
usersolve (CPXCENVptr env,
           void       *cbdata,
           int        wherefrom,
           void       *cbhandle,
           int        *useraction_p)
{
   int      status = 0;
   int      nodecount;
   CPXLPptr nodelp;

   *useraction_p = CPX_CALLBACK_DEFAULT;

   /* Get pointer to LP subproblem */

   status = CPXgetcallbacknodelp (env, cbdata, wherefrom, &nodelp);
   if ( status )  goto TERMINATE;

   /* Find out what node is being processed */

   status = CPXgetcallbackinfo (env, cbdata, wherefrom,
                                CPX_CALLBACK_INFO_NODE_COUNT,
                                &nodecount);
   if ( status )  goto TERMINATE;

   /* Solve initial node with primal, others with dual */

   if ( nodecount < 1 )  status = CPXprimopt (env, nodelp);
   else                  status = CPXdualopt (env, nodelp);

   /* If the solve was OK, set return to say optimization has
      been done in callback, otherwise return the CPLEX error
      code */

   if ( !status )  *useraction_p = CPX_CALLBACK_SET;

TERMINATE:

   return (status);

} /* END usersolve */


static int CPXPUBLIC
usersetbranch (CPXCENVptr   env,
               void         *cbdata,
               int          wherefrom,
               void         *cbhandle,
               int          brtype,
               int          sos,
               int          nodecnt,
               int          bdcnt,
               const double *nodeest,
               const int    *nodebeg,
               const int    *indices,
               const char   *lu,
               const int    *bd,
               int          *useraction_p)   
{
   int status = 0;
 
   int      j, bestj = -1;
   int      cols;
   double   maxobj = -CPX_INFBOUND;
   double   maxinf = -CPX_INFBOUND;
   double   xj_inf;
   int      xj_lo;
   double   objval;
   double   *x   = NULL;
   double   *obj = NULL;
   int      *feas = NULL;
 
   char     varlu[1];
   int      varbd[1];
   int      seqnum1, seqnum2;
 
   CPXCLPptr lp;
 
   /* Initialize useraction to indicate no user action taken */
 
   *useraction_p = CPX_CALLBACK_DEFAULT;
 
   /* If CPLEX is choosing an SOS branch, take it */
 
   if ( sos >= 0 )  return (status);
 
   /* Get pointer to the problem */
 
   status = CPXgetcallbacklp (env, cbdata, wherefrom, &lp);
   if ( status ) {
      fprintf (stdout, "Can't get LP pointer.\n");
      goto TERMINATE;
   }
 
   cols = CPXgetnumcols (env, lp);
   if ( cols <= 0 ) {
      fprintf (stdout, "Can't get number of columns.\n");
      status = CPXERR_CALLBACK;
      goto TERMINATE;
   }
 
   /* Get solution values and objective coefficients */
 
   x    = (double *) malloc (cols * sizeof (double));
   obj  = (double *) malloc (cols * sizeof (double));
   feas = (int *)    malloc (cols * sizeof (int));
   if ( x     == NULL ||
        obj   == NULL ||
        feas  == NULL   ) {
      fprintf (stdout, "Out of memory.");
      status = CPXERR_CALLBACK;
      goto TERMINATE;
   }
 
   status = CPXgetcallbacknodex (env, cbdata, wherefrom, x, 0,
                                 cols-1);
   if ( status ) {
      fprintf (stdout, "Can't get node solution.");
      goto TERMINATE;
   }
 
   status = CPXgetcallbacknodeobjval (env, cbdata, wherefrom,
                                      &objval);
   if ( status ) {
      fprintf (stdout, "Can't get node objective value.");
      goto TERMINATE;
   }
 
   status = CPXgetobj (env, lp, obj, 0, cols-1);
   if ( status ) {
      fprintf (stdout, "Can't get obj.");
      goto TERMINATE;
   }
 
   status = CPXgetcallbacknodeintfeas (env, cbdata, wherefrom, feas,
                                       0, cols-1);
   if ( status ) {
      fprintf (stdout,
               "Can't get variable feasible status for node.");
      goto TERMINATE;
   }
 
   /* Branch on var with largest objective coefficient among those
      with largest infeasibility */
 
   for (j = 0; j < cols; j++) {
      if ( feas[j] == CPX_INTEGER_INFEASIBLE ) {
         xj_inf = x[j] - floor (x[j]);
         if ( xj_inf > 0.5 )  xj_inf = 1.0 - xj_inf;
 
         if ( xj_inf >= maxinf                            &&
              (xj_inf > maxinf || fabs (obj[j]) >= maxobj)  ) {
            bestj  = j;
            maxinf = xj_inf; 
            maxobj = fabs (obj[j]);
         }
      }
   }
 
   /* If there weren't any eligible variables, take default branch */
 
   if ( bestj < 0 ) {
      goto TERMINATE;
   }
 
   /* Now set up node descriptions */
 
   xj_lo = (int) floor (x[bestj]);
 
   /* Up node */
 
   varlu[0] = 'L';
   varbd[0] = xj_lo + 1;
   status = CPXbranchcallbackbranchbds (env, cbdata, wherefrom,
                                        objval, 1, &bestj, varlu, varbd,
                                        NULL, &seqnum1);
   if ( status )  goto TERMINATE;
 
   /* Down node */
 
   varlu[0] = 'U';
   varbd[0] = xj_lo;
 
   status = CPXbranchcallbackbranchbds (env, cbdata, wherefrom,
                                        objval, 1, &bestj, varlu, varbd,
                                        NULL, &seqnum2);
   if ( status )  goto TERMINATE;
 
   /* Set useraction to indicate a user-specified branch */
 
   *useraction_p = CPX_CALLBACK_SET;
 
TERMINATE:
 
   free_and_null ((char **) &x);
   free_and_null ((char **) &obj);
   free_and_null ((char **) &feas); 
   return (status);
 
} /* END usersetbranch */


static int CPXPUBLIC 
userselectnode (CPXCENVptr env,
                void       *cbdata,
                int        wherefrom,
                void       *cbhandle,
                int        *nodenum_p,
                int        *useraction_p)
{
   int status = 0;

   int    thisnode;
   int    nodesleft;
   int    bestnode = 0;
   int    depth;
   int    maxdepth = -1;
   double siinf;
   double maxsiinf = 0.0;

   /* Initialize useraction to indicate no user node selection */

   *useraction_p = CPX_CALLBACK_DEFAULT;

   /* Choose the node with the largest sum of infeasibilities among
      those at the greatest depth */

   status = CPXgetcallbackinfo (env, cbdata, wherefrom,
                                CPX_CALLBACK_INFO_NODES_LEFT,
                                &nodesleft);
   if ( status )  goto TERMINATE;

   for (thisnode = 0; thisnode < nodesleft; thisnode++) {
      status = CPXgetcallbacknodeinfo (env, cbdata, wherefrom,
                                       thisnode,
                                       CPX_CALLBACK_INFO_NODE_DEPTH,
                                       &depth);
      if ( !status ) {
         status = CPXgetcallbacknodeinfo (env, cbdata, wherefrom,
                                        thisnode,
                                        CPX_CALLBACK_INFO_NODE_SIINF,
                                        &siinf);
      }
      if ( status )  break;

      if ( (depth >= maxdepth)                   &&
           (depth > maxdepth || siinf > maxsiinf)  ) {
         bestnode = thisnode;
         maxdepth = depth;
         maxsiinf = siinf;
      }
   }

   *nodenum_p = bestnode;
   *useraction_p = CPX_CALLBACK_SET;

TERMINATE:

   return (status);

} /* END userselectnode */
