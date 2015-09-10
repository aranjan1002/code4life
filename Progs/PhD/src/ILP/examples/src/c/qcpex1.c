/* --------------------------------------------------------------------------
 * File: qcpex1.c
 * Version 12.2
 * --------------------------------------------------------------------------
 * Licensed Materials - Property of IBM
 * 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
 * Copyright IBM Corporation 2003, 2010. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with
 * IBM Corp.
 * --------------------------------------------------------------------------
 */

/* qcpex1.c - Entering and optimizing a quadratically constrained problem */

/* Bring in the CPLEX function declarations and the C library
   header file stdio.h with include of cplex.h. */

#include <ilcplex/cplex.h>
#include <stdlib.h>

/* Bring in the declarations for the string functions */

#include <string.h>

/* Include declaration for function at end of program */

static int
   setproblemdata (char **probname_p, int *numcols_p, int *numrows_p,
                   int *objsen_p, double **obj_p, double **rhs_p,
                   char **sense_p, int **matbeg_p, int **matcnt_p,
                   int **matind_p, double **matval_p, double **lb_p,
                   double **ub_p, int **qmatbeg_p, int **qmatcnt_p,
                   int **qmatind_p, double **qmatval_p,
		   int **qconrow_p, int **qconcol_p, double **qconval_p);

static void
   free_and_null (char **ptr);


/* The problem we are optimizing will have 2 rows, 3 columns,
   6 nonzeros in the linear constraint matrix, 7 nonzeros in the
   quadratic objective, and 3 nonzeros in the quadratic constraint. */

#define NUMROWS   2
#define NUMCOLS   3
#define NUMNZ     6
#define NUMQOBJNZ 7
#define NUMQCONNZ 3


int
main (void)
{
   /* Declare pointers for the variables and arrays that will contain
      the data which define the LP problem.  The setproblemdata() routine
      allocates space for the problem data.  */

   char     *probname = NULL;
   int      numcols;
   int      numrows;
   int      objsen;
   double   *obj = NULL;
   double   *rhs = NULL;
   char     *sense = NULL;
   int      *matbeg = NULL;
   int      *matcnt = NULL;
   int      *matind = NULL;
   double   *matval = NULL;
   double   *lb = NULL;
   double   *ub = NULL;
   int      *qmatbeg = NULL;
   int      *qmatcnt = NULL;
   int      *qmatind = NULL;
   double   *qmatval = NULL;
   int      *qconrow = NULL;
   int      *qconcol = NULL;
   double   *qconval = NULL;

   /* Declare and allocate space for the variables and arrays where we
      will store the optimization results including the status, objective
      value, variable values, dual values, row slacks and variable
      reduced costs. */

   int      solstat;
   double   objval;
   double   x[NUMCOLS];
   double   slack[NUMROWS];


   CPXENVptr     env = NULL;
   CPXLPptr      lp = NULL;
   int           status;
   int           j;
   int           cur_numcols;

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

   /* Fill in the data for the problem.  */

   status = setproblemdata (&probname, &numcols, &numrows, &objsen, &obj,
                            &rhs, &sense, &matbeg, &matcnt, &matind,
                            &matval, &lb, &ub, &qmatbeg, &qmatcnt,
                            &qmatind, &qmatval, &qconrow, &qconcol, &qconval);
   if ( status ) {
      fprintf (stderr, "Failed to build problem data arrays.\n");
      goto TERMINATE;
   }

   /* Create the problem. */

   lp = CPXcreateprob (env, &status, probname);

   /* A returned pointer of NULL may mean that not enough memory
      was available or there was some other problem.  In the case of
      failure, an error message will have been written to the error
      channel from inside CPLEX.  In this example, the setting of
      the parameter CPX_PARAM_SCRIND causes the error message to
      appear on stdout.  */

   if ( lp == NULL ) {
      fprintf (stderr, "Failed to create problem.\n");
      goto TERMINATE;
   }

   /* Now copy the LP part of the problem data into the lp. */

   status = CPXcopylp (env, lp, numcols, numrows, objsen, obj, rhs,
                       sense, matbeg, matcnt, matind, matval,
                       lb, ub, NULL);

   if ( status ) {
      fprintf (stderr, "Failed to copy problem data.\n");
      goto TERMINATE;
   }

   /* Now copy the quadratic objective. */

   status = CPXcopyquad (env, lp, qmatbeg, qmatcnt, qmatind, qmatval);
   if ( status ) {
      fprintf (stderr, "Failed to copy quadratic matrix.\n");
      goto TERMINATE;
   }

   /* Now add the quadratic constraint. */

   status = CPXaddqconstr (env, lp, 0, NUMQCONNZ, 1.0, 'L',
                           NULL, NULL, qconrow, qconcol, qconval, NULL);
   if ( status ) {
      fprintf (stderr, "Failed to copy quadratic constraint.\n");
      goto TERMINATE;
   }


   /* Optimize the problem and obtain solution. */

   status = CPXbaropt (env, lp);
   if ( status ) {
      fprintf (stderr, "Failed to optimize QCP.\n");
      goto TERMINATE;
   }

   status = CPXsolution (env, lp, &solstat, &objval, x, NULL, slack, NULL);
   if ( status ) {
      fprintf (stderr, "Failed to obtain solution.\n");
      goto TERMINATE;
   }


   /* Write the output to the screen. */

   printf ("\nSolution status = %d\n", solstat);
   printf ("Solution value  = %f\n\n", objval);

   /* The size of the problem should be obtained by asking CPLEX what
      the actual size is, rather than using what was passed to CPXcopylp.
      cur_numcols stores the current number of columns.  */

   cur_numcols = CPXgetnumcols (env, lp);

   for (j = 0; j < cur_numcols; j++) {
      printf ("Column %d:  Value = %10f\n", j, x[j]);
   }

   /* Finally, write a copy of the problem to a file. */

   status = CPXwriteprob (env, lp, "qcpex1.lp", NULL);
   if ( status ) {
      fprintf (stderr, "Failed to write LP to disk.\n");
      goto TERMINATE;
   }


TERMINATE:

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

   /* Free up the problem data arrays, if necessary. */

   free_and_null ((char **) &probname);
   free_and_null ((char **) &obj);
   free_and_null ((char **) &rhs);
   free_and_null ((char **) &sense);
   free_and_null ((char **) &matbeg);
   free_and_null ((char **) &matcnt);
   free_and_null ((char **) &matind);
   free_and_null ((char **) &matval);
   free_and_null ((char **) &lb);
   free_and_null ((char **) &ub);
   free_and_null ((char **) &qmatbeg);
   free_and_null ((char **) &qmatcnt);
   free_and_null ((char **) &qmatind);
   free_and_null ((char **) &qmatval);
   free_and_null ((char **) &qconrow);
   free_and_null ((char **) &qconcol);
   free_and_null ((char **) &qconval);

   return (status);

}  /* END main */


/* This function fills in the data structures for the quadratically
   constrained program:

      Maximize
       obj: x1 + 2 x2 + 3 x3
              - 0.5 ( 33x1*x1 + 22*x2*x2 + 11*x3*x3
                   -  12*x1*x2 - 23*x2*x3 )
      Subject To
       c1: - x1 + x2 + x3 <= 20
       c2: x1 - 3 x2 + x3 <= 30
       q1: [ x1^2 + x2^2 + x3^2 ] <= 1.0
      Bounds
       0 <= x1 <= 40
      End
 */


static int
setproblemdata (char **probname_p, int *numcols_p, int *numrows_p,
                int *objsen_p, double **obj_p, double **rhs_p,
                char **sense_p, int **matbeg_p, int **matcnt_p,
                int **matind_p, double **matval_p, double **lb_p,
                double **ub_p, int **qmatbeg_p, int **qmatcnt_p,
                int **qmatind_p, double **qmatval_p,
		int **qconrow_p, int **qconcol_p, double **qconval_p)
{
   char     *zprobname = NULL;     /* Problem name <= 16 characters */
   double   *zobj = NULL;
   double   *zrhs = NULL;
   char     *zsense = NULL;
   int      *zmatbeg = NULL;
   int      *zmatcnt = NULL;
   int      *zmatind = NULL;
   double   *zmatval = NULL;
   double   *zlb = NULL;
   double   *zub = NULL;
   int      *zqmatbeg = NULL;
   int      *zqmatcnt = NULL;
   int      *zqmatind = NULL;
   double   *zqmatval = NULL;
   int      *zqconrow = NULL;
   int      *zqconcol = NULL;
   double   *zqconval = NULL;
   int      status = 0;

   zprobname = (char *) malloc (16 * sizeof(char));
   zobj      = (double *) malloc (NUMCOLS * sizeof(double));
   zrhs      = (double *) malloc (NUMROWS * sizeof(double));
   zsense    = (char *) malloc (NUMROWS * sizeof(char));
   zmatbeg   = (int *) malloc (NUMCOLS * sizeof(int));
   zmatcnt   = (int *) malloc (NUMCOLS * sizeof(int));
   zmatind   = (int *) malloc (NUMNZ * sizeof(int));
   zmatval   = (double *) malloc (NUMNZ * sizeof(double));
   zlb       = (double *) malloc (NUMCOLS * sizeof(double));
   zub       = (double *) malloc (NUMCOLS * sizeof(double));
   zqmatbeg  = (int *) malloc (NUMCOLS * sizeof(int));
   zqmatcnt  = (int *) malloc (NUMCOLS * sizeof(int));
   zqmatind  = (int *) malloc (NUMQOBJNZ * sizeof(int));
   zqmatval  = (double *) malloc (NUMQOBJNZ * sizeof(double));
   zqconrow  = (int *) malloc (NUMQCONNZ * sizeof(int));
   zqconcol  = (int *) malloc (NUMQCONNZ * sizeof(int));
   zqconval  = (double *) malloc (NUMQCONNZ * sizeof(double));

   if ( zprobname == NULL || zobj     == NULL ||
        zrhs      == NULL || zsense   == NULL ||
        zmatbeg   == NULL || zmatcnt  == NULL ||
        zmatind   == NULL || zmatval  == NULL ||
        zlb       == NULL || zub      == NULL ||
        zqmatbeg  == NULL || zqmatcnt == NULL ||
        zqmatind  == NULL || zqmatval == NULL ||
        zqconrow  == NULL || zqconcol == NULL ||
        zqconval  == NULL                       )  {
      status = 1;
      goto TERMINATE;
   }

   strcpy (zprobname, "example");

   /* The code is formatted to make a visual correspondence
      between the mathematical linear program and the specific data
      items.   */

     zobj[0]  = 1.0;   zobj[1]   = 2.0;   zobj[2] = 3.0;

   zmatbeg[0] = 0;     zmatbeg[1] = 2;    zmatbeg[2] = 4;
   zmatcnt[0] = 2;     zmatcnt[1] = 2;    zmatcnt[2] = 2;

   zmatind[0] = 0;     zmatind[2] = 0;    zmatind[4] = 0;     zsense[0] = 'L';
   zmatval[0] = -1.0;  zmatval[2] = 1.0;  zmatval[4] = 1.0;   zrhs[0]   = 20.0;

   zmatind[1] = 1;     zmatind[3] = 1;    zmatind[5] = 1;     zsense[1] = 'L';
   zmatval[1] = 1.0;   zmatval[3] = -3.0; zmatval[5] = 1.0;   zrhs[1]   = 30.0;

       zlb[0] = 0.0;       zlb[1] = 0.0;          zlb[2] = 0.0;
       zub[0] = 40.0;      zub[1] = CPX_INFBOUND; zub[2] = CPX_INFBOUND;

   /* Now set up the Q matrix.  Note that we set the values knowing that
      we're doing a maximization problem, so negative values go on
      the diagonal.  Also, the off diagonal terms are each repeated,
      by taking the algebraic term and dividing by 2 */

   zqmatbeg[0] = 0;     zqmatbeg[1] = 2;     zqmatbeg[2] = 5;
   zqmatcnt[0] = 2;     zqmatcnt[1] = 3;     zqmatcnt[2] = 2;

   /* Matrix is set up visually.  Note that the x1*x3 term is 0, and is
      left out of the matrix.  */

   zqmatind[0] = 0;     zqmatind[2] = 0;
   zqmatval[0] = -33.0; zqmatval[2] = 6.0;

   zqmatind[1] = 1;     zqmatind[3] = 1;     zqmatind[5] = 1;
   zqmatval[1] = 6.0;   zqmatval[3] = -22.0; zqmatval[5] = 11.5;

                        zqmatind[4] = 2;     zqmatind[6] = 2;
                        zqmatval[4] = 11.5;  zqmatval[6] = -11.0;

   /* Specify the quadratic constraint coefficients */

   zqconrow[0] = 0;     zqconcol[0] = 0;     zqconval[0] = 1.0;
   zqconrow[1] = 1;     zqconcol[1] = 1;     zqconval[1] = 1.0;
   zqconrow[2] = 2;     zqconcol[2] = 2;     zqconval[2] = 1.0;

TERMINATE:

   if ( status ) {
      free_and_null ((char **) &zprobname);
      free_and_null ((char **) &zobj);
      free_and_null ((char **) &zrhs);
      free_and_null ((char **) &zsense);
      free_and_null ((char **) &zmatbeg);
      free_and_null ((char **) &zmatcnt);
      free_and_null ((char **) &zmatind);
      free_and_null ((char **) &zmatval);
      free_and_null ((char **) &zlb);
      free_and_null ((char **) &zub);
      free_and_null ((char **) &zqmatbeg);
      free_and_null ((char **) &zqmatcnt);
      free_and_null ((char **) &zqmatind);
      free_and_null ((char **) &zqmatval);
      free_and_null ((char **) &zqconrow);
      free_and_null ((char **) &zqconcol);
      free_and_null ((char **) &zqconval);
   }
   else {
      *numcols_p   = NUMCOLS;
      *numrows_p   = NUMROWS;
      *objsen_p    = CPX_MAX;   /* The problem is maximization */

      *probname_p  = zprobname;
      *obj_p       = zobj;
      *rhs_p       = zrhs;
      *sense_p     = zsense;
      *matbeg_p    = zmatbeg;
      *matcnt_p    = zmatcnt;
      *matind_p    = zmatind;
      *matval_p    = zmatval;
      *lb_p        = zlb;
      *ub_p        = zub;
      *qmatbeg_p   = zqmatbeg;
      *qmatcnt_p   = zqmatcnt;
      *qmatind_p   = zqmatind;
      *qmatval_p   = zqmatval;
      *qconrow_p   = zqconrow;
      *qconcol_p   = zqconcol;
      *qconval_p   = zqconval;
   }
   return (status);

}  /* END setproblemdata */



/* This simple routine frees up the pointer *ptr, and sets *ptr to NULL */

static void
free_and_null (char **ptr)
{
   if ( *ptr != NULL ) {
      free (*ptr);
      *ptr = NULL;
   }
} /* END free_and_null */

