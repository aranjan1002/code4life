/* --------------------------------------------------------------------------
 * File: admipex7.c
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

/* admipex7.c - Use the solve callback to do a combined dual-
                barrier solve.  This example shows how to obtain
                the functionality of CPX_NODEALG_DUAL_HYBBAROPT,
                which has been removed. */

/* To run this example, command line arguments are required:
       admipex7 filename
   where 
       filename  Name of the file, with .mps, .lp, or .sav
                 extension, and a possible additional .gz 
                 extension.
   Example:
       admipex7  mexample.mps */

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

static void
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
   
   CPXENVptr env = NULL;
   CPXLPptr  lp = NULL;

   int nameind = 1;

   /* Check the command line arguments */

   if ( argc != 2 ) {
      usage (argv[0]);
      goto TERMINATE;
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


   /* Turn on traditional search for use with control callbacks */

   status = CPXsetintparam (env, CPX_PARAM_MIPSEARCH, CPX_MIPSEARCH_TRADITIONAL);
   if ( status )  goto TERMINATE;

   /* Set up to use MIP callbacks */

   status = CPXsetsolvecallbackfunc (env, usersolve, NULL);
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



TERMINATE:

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

   /* Solve initial node with barrier with crossover */

   if ( nodecount < 1 ) {
       status = CPXhybbaropt (env, nodelp, CPX_ALG_AUTOMATIC);
   }
   else {
      int itlimit;
      int solstat;

      /* Limit the number of iterations for dual to 5
         The 'const' on the environment must be cast away
         to change a parameter.  Generally, parameter changes
         in a callback should not be made since they will
         have unpredictable effects.  This one, because it
         only controls a limit on an LP optimizer, is OK. */ 

      status = CPXgetintparam (env, CPX_PARAM_ITLIM, &itlimit);
      if ( status )  goto TERMINATE;

      status = CPXsetintparam ((CPXENVptr) env, CPX_PARAM_ITLIM, 200);
      if ( status )  goto TERMINATE;

      status = CPXdualopt (env, nodelp);

      CPXsetintparam ((CPXENVptr) env, CPX_PARAM_ITLIM, itlimit);

      if ( !status ) {
         solstat = CPXgetstat (env, nodelp);
         if ( solstat == CPX_STAT_ABORT_IT_LIM ) {
            int preind;
            int aggind;

            /* Set parameters to get a full presolve on the node lp */

            status = CPXgetintparam (env, CPX_PARAM_PREIND, &preind);
            if ( status )  goto TERMINATE;
            status = CPXgetintparam (env, CPX_PARAM_AGGIND, &aggind);
            if ( status )  goto TERMINATE;

            status = CPXsetintparam ((CPXENVptr) env, CPX_PARAM_PREIND, CPX_ON);
            if ( status )  goto TERMINATE;
            status = CPXsetintparam ((CPXENVptr) env, CPX_PARAM_AGGIND, -1);
            if ( status )  goto TERMINATE;

            status = CPXhybbaropt (env, nodelp, CPX_ALG_AUTOMATIC);

            CPXsetintparam ((CPXENVptr) env, CPX_PARAM_PREIND, preind);
            CPXsetintparam ((CPXENVptr) env, CPX_PARAM_AGGIND, aggind);
         }
      }
   }
 
   /* If the solve was OK, set return to say optimization has
      been done in callback, otherwise return the CPLEX error
      code */

   if ( !status )  *useraction_p = CPX_CALLBACK_SET;

TERMINATE:

   return (status);

} /* END usersolve */


