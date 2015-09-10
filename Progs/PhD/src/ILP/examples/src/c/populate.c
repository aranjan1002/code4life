/* --------------------------------------------------------------------------
 * File: populate.c
 * Version 12.2
 * --------------------------------------------------------------------------
 * Licensed Materials - Property of IBM
 * 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
 * Copyright IBM Corporation 2007, 2010. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with
 * IBM Corp.
 * --------------------------------------------------------------------------
 */

/* populate.c - Reading in and generating multiple solutions to a
                MIP problem */

/* To run this example, command line arguments are required.
   i.e.,   populate   filename
   where 
       filename is the name of the file, with .mps, .lp, or .sav extension
   Example:
       populate  location.lp
 */

/* Bring in the CPLEX function declarations and the C library 
   header file stdio.h with the following single include. */

#include <ilcplex/cplex.h>

/* Bring in the declarations for the string and character functions 
   and malloc */

#include <ctype.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define EPSZERO        1.0E-10
#define BUFSIZE 16

/* Include declarations for functions in this program */

static void
   free_and_null (char **ptr),
   usage         (char *progname);


int
main (int argc, char *argv[])
{
   /* Declare and allocate space for the variables and arrays where we will
      store the optimization results including the status, objective value,
      and variable values. */


   int      solstat;
   double   objval;
   double   incobjval;
   double   meanobjval;
   double   *x     = NULL;
   double   *incx  = NULL;
   int      numsol;
   int      numsolreplaced;
   int      numdiff;

   CPXENVptr     env = NULL;
   CPXLPptr      lp = NULL;
   int           status;
   int           i, j;
   int           cur_numcols;

   /* Check the command line arguments */

   if ( argc != 2 ) {
      usage (argv[0]);
      goto TERMINATE;
   }

   /* Initialize the CPLEX environment */

   env = CPXopenCPLEX (&status);

   /* If an error occurs, the status value indicates the reason for
      failure.  A call to CPXgeterrorstring will produce the text of
      the error message.  Note that CPXopenCPLEX produces no output,
      so the only way to see the cause of the error is to use
      CPXgeterrorstring.  For other CPLEX routines, the errors will
      be seen if the CPX_PARAM_SCRIND indicator is set to CPX_ON.  */

   if ( env == NULL ) {
      char  errmsg[1024];
      fprintf (stderr, "Could not open CPLEX environment.\n");
      CPXgeterrorstring (env, status, errmsg);
      fprintf (stderr, "%s", errmsg);
      goto TERMINATE;
   }

   /* Turn on output to the screen */

   status = CPXsetintparam (env, CPX_PARAM_SCRIND, CPX_ON);
   if ( status ) {
      fprintf (stderr, 
               "Failure to turn on screen indicator, error %d.\n", status);
      goto TERMINATE;
   }

   /* Create the problem, using the filename as the problem name */

   lp = CPXcreateprob (env, &status, argv[1]);

   /* A returned pointer of NULL may mean that not enough memory
      was available or there was some other problem.  In the case of 
      failure, an error message will have been written to the error 
      channel from inside CPLEX.  In this example, the setting of
      the parameter CPX_PARAM_SCRIND causes the error message to
      appear on stdout.  Note that most CPLEX routines return
      an error code to indicate the reason for failure.   */

   if ( lp == NULL ) {
      fprintf (stderr, "Failed to create LP.\n");
      goto TERMINATE;
   }

   /* Now read the file, and copy the data into the created lp */

   status = CPXreadcopyprob (env, lp, argv[1], NULL);
   if ( status ) {
      fprintf (stderr, "Failed to read and copy the problem data.\n");
      goto TERMINATE;
   }

   /* Set the solution pool relative gap parameter to obtain solutions
      of objective value within 10% of the optimal */

   status = CPXsetdblparam (env, CPX_PARAM_SOLNPOOLGAP, 0.1);
   if ( status ) {
      fprintf (stderr, 
               "Failed to set the solution pool relative gap, error %d.\n", 
               status);
      goto TERMINATE;
   }


   /* Optimize the problem and obtain multiple solutions. */

   status = CPXpopulate (env, lp);

   if ( status ) {
      fprintf (stderr, "Failed to populate MIP.\n");
      goto TERMINATE;
   }

   solstat = CPXgetstat (env, lp);
   printf ("Solution status: %d.\n", solstat);

   status  = CPXgetobjval (env, lp, &incobjval);

   if ( status ) {
      fprintf (stderr,
               "Failed to obtain objective value for the incumbent.\n");
      goto TERMINATE;
   }

   printf ("Objective value of the incumbent: %.10g\n", incobjval);

   /* The size of the problem should be obtained by asking CPLEX what
      the actual size is. cur_numcols stores the current number 
      of columns. */

   cur_numcols = CPXgetnumcols (env, lp);

   /* Allocate space for solution */

   incx = (double *) malloc (cur_numcols*sizeof(double));

   if ( incx == NULL ) {
      fprintf (stderr, "No memory for solution values for the incumbent.\n");
      goto TERMINATE;
   }

   status = CPXgetx (env, lp, incx, 0, cur_numcols-1);
   if ( status ) {
      fprintf (stderr, "Failed to obtain the incumbent.\n");
      goto TERMINATE;
   }

   /* Write out the incumbent */

   for (j = 0; j < cur_numcols; j++) {
      printf ("Incumbent: Column %d:  Value = %17.10g\n", j, incx[j]);
   }
   printf ("\n");

   /* Get the number of solutions in the solution pool */

   numsol = CPXgetsolnpoolnumsolns (env, lp);   
   printf ("The solution pool contains %d solutions.\n", numsol);

   /* Some solutions are deleted from the pool because of the solution
      pool relative gap parameter */

   numsolreplaced = CPXgetsolnpoolnumreplaced (env, lp);
   printf (
"%d solutions were removed due to the solution pool relative gap parameter.\n",
          numsolreplaced);

   printf ("In total, %d solutions were generated.\n",
           numsol + numsolreplaced);
   
   /* Get the average objective value of solutions in the solution
      pool */

   status = CPXgetsolnpoolmeanobjval (env, lp, &meanobjval);
   printf ("The average objective value of the solutions is %.10g.\n\n",
          meanobjval);

   /* Write out the objective value of each solution and its
      difference to the incumbent */

   x = (double *) malloc (cur_numcols*sizeof(double));
   if ( x == NULL ) {
      fprintf (stderr, "No memory for solution values.\n");
      goto TERMINATE;
   }

   printf ("Solution        Objective   Number of variables\n");
   printf ("                value       that differ compared to\n");
   printf ("                            the incumbent\n");
  

   for (i = 0; i < numsol; i++) {
      char namei[BUFSIZE];
      int  surplus;

      /* Write out objective value */

      CPXgetsolnpoolsolnname (env, lp, namei, BUFSIZE, &surplus, i);
      printf ("%-15s ", namei); 


      status = CPXgetsolnpoolobjval (env, lp, i, &objval);
      if ( status ) {
         fprintf (stderr,
                  "Failed to obtain objective value for solution %d.\n", i);
         goto TERMINATE;
      }
      printf ("%.10g         ", objval);

      status = CPXgetsolnpoolx (env, lp, i, x, 0, cur_numcols-1);
      if ( status ) {
         fprintf (stderr, "Failed to obtain solution %d.\n", i);
         goto TERMINATE;
      }
      
      /* Compute the number of variables that differ in the solution
         and in the incumbent */

      numdiff = 0;
      for (j = 0; j < cur_numcols; j++) {
         if ( fabs (x[j] - incx[j]) > EPSZERO )
            numdiff++;
      }      
      printf ("%d / %d\n", numdiff, cur_numcols);
   }

   
TERMINATE:

   /* Free up the solution */

   free_and_null ((char **) &incx);
   free_and_null ((char **) &x);

   /* Free up the problem as allocated by CPXcreateprob, if necessary */

   if ( lp != NULL ) {
      status = CPXfreeprob (env, &lp);
      if ( status ) {
         fprintf (stderr, "CPXfreeprob failed, error code %d.\n", status);
      }
   }

   /* Free up the CPLEX environment, if necessary */

   if ( env != NULL ) {
      status = CPXcloseCPLEX (&env);

      /* Note that CPXcloseCPLEX produces no output,
         so the only way to see the cause of the error is to use
         CPXgeterrorstring.  For other CPLEX routines, the errors will
         be seen if the CPX_PARAM_SCRIND indicator is set to CPX_ON. */

      if ( status ) {
         char  errmsg[1024];
         fprintf (stderr, "Could not close CPLEX environment.\n");
         CPXgeterrorstring (env, status, errmsg);
         fprintf (stderr, "%s", errmsg);
      }
   }
     
   return (status);

}  /* END main */


/* This simple routine frees up the pointer *ptr, and sets *ptr to NULL */

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
   fprintf (stderr,"Usage: %s filename\n", progname);
   fprintf (stderr,"   where filename is a file with extension \n");
   fprintf (stderr,"      MPS, SAV, or LP (lower case is allowed)\n");
   fprintf (stderr,"  This program uses the CPLEX MIP optimizer.\n");
   fprintf (stderr," Exiting...\n");
} /* END usage */
