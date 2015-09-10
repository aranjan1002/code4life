/* --------------------------------------------------------------------------
 * File: admipex6.c
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

/* admipex6.c - Start the solution of the root relaxation in a 
                MIP model from an existing primal solution */

/* To run this example, command line arguments are required:
       admipex6  filename
   where 
       filename  Name of the file, with .mps, .lp, or .sav
                 extension, and a possible additional .gz 
                 extension.
   Example:
       admipex6  mexample.mps.gz */

/* Bring in the CPLEX function declarations and the C library 
   header file stdio.h with the following single include */

#include <ilcplex/cplex.h>

/* Bring in the declarations for the string and character functions, 
   malloc, and fabs */

#include <ctype.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

/* Structure for passing information into the solve callback 
   function */

struct MYCBstr {
   int      count; /* Number of solves performed so far */
   CPXLPptr mip;   /* The MIP model */
   double   *relx; /* The solution to the continuous relaxation */
};
typedef struct MYCBstr MYCB, *MYCBptr;

/* Declarations for functions in this program */

static void
   free_and_null (char **ptr),
   usage         (char *progname);

static int CPXPUBLIC
   solvecallback (CPXCENVptr env, void *cbdata, int wherefrom,
                  void *userinfo, int *useraction_p);



int
main (int  argc,
      char *argv[])
{
   int status = 0;

   /* Declare and allocate space for the variables and arrays where
      we will store the optimization results, including the status, 
      objective value, and variable values */
   
   int    solstat;
   double objval, relobj;
   double *x = NULL;
	 
   MYCB info;

   CPXENVptr env = NULL;
   CPXLPptr  lp  = NULL;
   CPXLPptr  lpclone = NULL;

   int j;
   int cur_numcols;

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


   /* Turn on traditional search for use with control callbacks */

   status = CPXsetintparam (env, CPX_PARAM_MIPSEARCH, CPX_MIPSEARCH_TRADITIONAL);
   if ( status )  goto TERMINATE;

   /* Create the problem, using the filename as the problem name */

   lp = CPXcreateprob (env, &status, argv[1]);

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

   status = CPXreadcopyprob (env, lp, argv[1], NULL);
   if ( status ) {
      fprintf (stderr,
               "Failed to read and copy the problem data.\n");
      goto TERMINATE;
   }

   /* We transfer a problem with semi-continuous or semi-integer
      variables to a MIP problem by adding variables and  
      constraints. So in MIP callbacks, the size of the problem
      is changed and this example won't work for such problems */

   if ( CPXgetnumsemicont (env, lp) + CPXgetnumsemiint (env, lp) ) {
      fprintf (stderr, 
         "Not for problems with semi-continuous or semi-integer variables.\n");
      goto TERMINATE;
   }

   /* The size of the problem should be obtained by asking CPLEX what
      the actual size is. cur_numcols store the current number 
      of columns */

   cur_numcols = CPXgetnumcols (env, lp);

   x = (double *) malloc (cur_numcols * sizeof (double));
   if ( x == NULL ) {
      fprintf (stderr, "Memory allocation failed.\n");
      goto TERMINATE;
   }

   /* Solve relaxation of MIP */

   /* Clone original model */

   lpclone = CPXcloneprob (env, lp, &status);
   if ( status ) {
      fprintf (stderr, "Failed to clone problem.\n");
      goto TERMINATE;
   }

   /* Relax */

   status = CPXchgprobtype (env, lpclone, CPXPROB_LP);
   if ( status ) {
      fprintf (stderr, "Failed to relax problem.\n");
      goto TERMINATE;
   }

   /* Solve LP relaxation of original model using "default"
      LP solver */

   status = CPXlpopt (env, lpclone);
   if ( status ) {
      fprintf (stderr, "Failed to solve relaxation.\n");
      goto TERMINATE;
   }

   status = CPXsolution (env, lpclone, NULL, &relobj, x, NULL,
                         NULL, NULL);
   if ( status ) {
      fprintf (stderr, "Failed to extract solution.\n");
      goto TERMINATE;
   }

   printf ("\nLP relaxation objective: %.4e\n\n", relobj);

   /* Set up solve callback */
   
   info.count = 0;
   info.mip   = lp;
   info.relx  = x;

   status = CPXsetsolvecallbackfunc (env, &solvecallback,
                                     (void *) &info);
   if ( status ) {
      fprintf (stderr, "Failed to set solve callback.\n");
      goto TERMINATE;
   }

   /* Optimize the problem and obtain solution */

   status = CPXmipopt (env, lp);
   if ( status ) {
      fprintf (stderr, "Failed to optimize MIP.\n");
      goto TERMINATE;
   }

   solstat = CPXgetstat (env, lp);
   printf ("Solution status %d.\n", solstat);

   status = CPXgetobjval (env, lp, &objval);
   if ( status ) {
      fprintf (stderr,"Failed to obtain objective value.\n");
      goto TERMINATE;
   }

   printf ("Objective value %.10g\n", objval);

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

   /* Free the cloned lp as allocated by CPXcloneprob,
      if necessary */

   if ( lpclone != NULL ) {
      status = CPXfreeprob (env, &lpclone);
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


/* This simple routine frees up the pointer *ptr, and sets *ptr 
   to NULL */

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
    "Usage: %s filename\n", progname);
   fprintf (stderr,
    "  filename   Name of a file, with .mps, .lp, or .sav\n");
   fprintf (stderr,
    "             extension, and a possible, additional .gz\n"); 
   fprintf (stderr,
    "             extension\n");
} /* END usage */


static int CPXPUBLIC
solvecallback (CPXCENVptr env,
               void       *cbdata,
               int        wherefrom,
               void       *userinfo,
               int        *useraction_p)
{
   int status = 0;

   int      lpstatus = 0;
   CPXLPptr nodelp   = NULL;
   double   *prex    = NULL;
   MYCBptr  mycbinfo = (MYCBptr) userinfo;
   CPXLPptr mip      = mycbinfo->mip;
   double   *relx    = mycbinfo->relx;
   int      cols;
   int      prestat;

   *useraction_p = CPX_CALLBACK_DEFAULT;

   /* Only use callback for solving the root relaxation (node 0) */

   if ( mycbinfo->count > 0 ) {
      goto TERMINATE;
   }
   mycbinfo->count++;

   /* Extract the LP to be solved */

   status = CPXgetcallbacknodelp (env, cbdata, wherefrom, &nodelp);
   if ( status ) goto TERMINATE;

   cols = CPXgetnumcols (env, nodelp);

   prex = (double *) malloc (cols * sizeof (double));
   if ( prex == NULL ) {
      status = CPXERR_NO_MEMORY;
      goto TERMINATE;
   }

   /* Use MIP presolve to crush the original solution.  Note that
      MIP presolve can only crush primal solutions */

   status = CPXgetprestat (env, mip, &prestat, NULL, NULL, NULL,
                           NULL);
   if ( status ) goto TERMINATE;

   /* If a presolved model exists, then relx is crushed down
      to prex, the corresponding solution for the presolved
      model; otherwise, prex is just a copy of relx */
   
   if ( prestat ) {
      status = CPXcrushx (env, mip, relx, prex);
      if ( status ) goto TERMINATE;
   }
   else {
      memcpy (prex, relx, cols * sizeof (double));
   }

   /* Feed the crushed solution into 'nodelp' */

   status = CPXcopystart (env, nodelp, NULL, NULL, prex, NULL,
                          NULL, NULL);

   /* Use primal to reoptimize, since we only have a primal
      solution */

   CPXsetintparam ((CPXENVptr)env, CPX_PARAM_SIMDISPLAY, 1);

   status = CPXprimopt (env, nodelp);
   if ( status )  goto TERMINATE;

   lpstatus = CPXgetstat (env, nodelp);
   if ( lpstatus == CPX_STAT_OPTIMAL        ||
        lpstatus == CPX_STAT_OPTIMAL_INFEAS ||
        lpstatus == CPX_STAT_INFEASIBLE       ) {
      *useraction_p = CPX_CALLBACK_SET;
   }
   
TERMINATE:

   free_and_null ((char **) &prex);

   return (status);

} /* END solvecallback */
