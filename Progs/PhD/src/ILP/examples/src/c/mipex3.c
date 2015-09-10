/* --------------------------------------------------------------------------
 * File: mipex3.c
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

/* mipex3.c - Entering and optimizing a MIP problem with SOS sets
              and priority orders.  Is a modification of mipex1.c .
              Note that the problem solved is slightly different than
              mipex1.c so that the output is interesting.  */

/* Bring in the CPLEX function declarations and the C library 
   header file stdlib.h with the following single include. */

#include <ilcplex/cplex.h>

/* Bring in the declarations for the string functions */

#include <string.h>
#include <stdlib.h>

/* Include declaration for function at end of program */

static int
   setproblemdata (char **probname_p, int *numcols_p, int *numrows_p, 
                   int *objsen_p, double **obj_p, double **rhs_p, 
                   char **sense_p, int **matbeg_p, int **matcnt_p, 
                   int **matind_p, double **matval_p, 
                   double **lb_p, double **ub_p, char **ctype_p),
   setsosandorder (CPXENVptr env, CPXLPptr lp);

static void
   free_and_null (char **ptr);



/* The problem we are optimizing will have 3 rows, 4 columns 
   and 9 nonzeros.  */

#define NUMROWS    3
#define NUMCOLS    4
#define NUMNZ      9


int
main (void)
{
   /* Declare and allocate space for the variables and arrays that 
      will contain the data which define the LP problem */

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
   char     *ctype = NULL;

   /* Declare and allocate space for the variables and arrays where we
      will store the optimization results including the status, objective
      value, variable values, and row slacks. */

   int      solstat;
   double   objval;
   double   x[NUMCOLS];
   double   slack[NUMROWS];


   CPXENVptr     env = NULL;
   CPXLPptr      lp = NULL;
   int           status;
   int           i, j;
   int           cur_numrows, cur_numcols;

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
                            &rhs, &sense, &matbeg, &matcnt, &matind, &matval, 
                            &lb, &ub, &ctype);
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
      fprintf (stderr, "Failed to create LP.\n");
      goto TERMINATE;
   }

   /* Now copy the problem data into the lp */

   status = CPXcopylp (env, lp, numcols, numrows, objsen, obj, rhs, 
                       sense, matbeg, matcnt, matind, matval,
                       lb, ub, NULL);

   if ( status ) {
      fprintf (stderr, "Failed to copy problem data.\n");
      goto TERMINATE;
   }

   /* Now copy the ctype array */

   status = CPXcopyctype (env, lp, ctype);
   if ( status ) {
      fprintf (stderr, "Failed to copy ctype\n");
      goto TERMINATE;
   }

   /* Set up the SOS set and priority order */

   status = setsosandorder (env, lp);
   if ( status ) goto TERMINATE;

   /* Write a copy of the problem to a file. */

   status = CPXwriteprob (env, lp, "mipex3.mps", NULL);
   if ( status ) {
      fprintf (stderr, "Failed to write LP to disk.\n");
      goto TERMINATE;
   }

   /* Turn off CPLEX presolve, aggregate, and print every node.  This
      is just to make it interesting.  Turning off CPLEX presolve is
      NOT recommended practice !! */

   status = CPXsetintparam (env, CPX_PARAM_PREIND, CPX_OFF);
   if (!status) CPXsetintparam (env, CPX_PARAM_AGGIND, CPX_OFF);
   if (!status) CPXsetintparam (env, CPX_PARAM_MIPINTERVAL, 1);

   if ( status ) {
      fprintf (stderr, "Failed to set some CPLEX parameters.\n");
      goto TERMINATE;
   }


   /* Optimize the problem and obtain solution. */

   status = CPXmipopt (env, lp);
   if ( status ) {
      fprintf (stderr, "Failed to optimize MIP.\n");
      goto TERMINATE;
   }

   solstat = CPXgetstat (env, lp);

   /* Write the output to the screen. */

   printf ("\nSolution status = %d\n", solstat);
    
   status = CPXgetobjval (env, lp, &objval);
   if ( status ) {
      fprintf (stderr,"No MIP objective value available.  Exiting...\n");
      goto TERMINATE;
   }

   printf ("Solution value  = %f\n\n", objval);

   /* The size of the problem should be obtained by asking CPLEX what
      the actual size is, rather than using what was passed to CPXcopylp.
      cur_numrows and cur_numcols store the current number of rows and
      columns, respectively.  */

   cur_numrows = CPXgetnumrows (env, lp);
   cur_numcols = CPXgetnumcols (env, lp);

   status = CPXgetx (env, lp, x, 0, cur_numcols-1);
   if ( status ) {
      fprintf (stderr, "Failed to get optimal integer x.\n");
      goto TERMINATE;
   }

   status = CPXgetslack (env, lp, slack, 0, cur_numrows-1);
   if ( status ) {
      fprintf (stderr, "Failed to get optimal slack values.\n");
      goto TERMINATE;
   }

   for (i = 0; i < cur_numrows; i++) {
      printf ("Row %d:  Slack = %10f\n", i, slack[i]);
   }

   for (j = 0; j < cur_numcols; j++) {
      printf ("Column %d:  Value = %10f\n", j, x[j]);
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
   free_and_null ((char **) &ctype);
     
   return (status);

}  /* END main */


/* This function fills in the data structures for the mixed integer program:

      Maximize
       obj: x1 + 2 x2 + 3 x3 + x4
      Subject To
       c1: - x1 + x2 + x3 + 10x4  <= 20
       c2: x1 - 3 x2 + x3         <= 30
       c3:       x2       - 3.5x4  = 0
      Bounds
       0 <= x1 <= 40
       2 <= x4 <= 3
      Integers
        x2 x3 x4
      End
 */


static int
setproblemdata (char **probname_p, int *numcols_p, int *numrows_p, 
                int *objsen_p, double **obj_p, double **rhs_p, 
                char **sense_p, int **matbeg_p, int **matcnt_p, 
                int **matind_p, double **matval_p, 
                double **lb_p, double **ub_p, char **ctype_p)
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
   char     *zctype = NULL;
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
   zctype    = (char *) malloc (NUMCOLS * sizeof(char)); 
 
   if ( zprobname == NULL || zobj    == NULL ||
        zrhs      == NULL || zsense  == NULL ||
        zmatbeg   == NULL || zmatcnt == NULL ||
        zmatind   == NULL || zmatval == NULL ||
        zlb       == NULL || zub     == NULL ||
        zctype    == NULL                       )  {
      status = 1;
      goto TERMINATE;
   }

   strcpy (zprobname, "example");

   /* The code is formatted to make a visual correspondence 
      between the mathematical linear program and the specific data
      items.   */

     zobj[0] = 1.0;   zobj[1]   = 2.0;  zobj[2] = 3.0;    zobj[3] = 1.0;  

   zmatbeg[0] = 0;    zmatbeg[1] = 2;   zmatbeg[2] = 5;   zmatbeg[3] = 7;
   zmatcnt[0] = 2;    zmatcnt[1] = 3;   zmatcnt[2] = 2;   zmatcnt[3] = 2;
      
   zmatind[0] = 0;    zmatind[2] = 0;   zmatind[5] = 0;   zmatind[7] = 0;
   zmatval[0] = -1.0; zmatval[2] = 1.0; zmatval[5] = 1.0; zmatval[7] = 10.0;

   zmatind[1] = 1;    zmatind[3] = 1;    zmatind[6] = 1;     
   zmatval[1] = 1.0;  zmatval[3] = -3.0; zmatval[6] = 1.0;   

                      zmatind[4] = 2;                     zmatind[8] = 2;
                      zmatval[4] = 1.0;                   zmatval[8] = -3.5;

   zlb[0] = 0.0;    zlb[1] = 0.0;          zlb[2] = 0.0;          zlb[3] = 2.0;
   zub[0] = 40.0;   zub[1] = CPX_INFBOUND; zub[2] = CPX_INFBOUND; zub[3] = 3.0;

   zctype[0] = 'C';   zctype[1] = 'I';  zctype[2] = 'I';   zctype[3] = 'I';

  /* The right-hand-side values don't fit nicely on a line above.  So put
     them here.  */

   zsense[0] = 'L';
   zrhs[0]   = 20.0;

   zsense[1] = 'L';
   zrhs[1]   = 30.0;

   zsense[2] = 'E';
   zrhs[2]   = 0.0;

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
      free_and_null ((char **) &zctype);
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
      *ctype_p     = zctype;
   }
   return (status);

}  /* END setproblemdata */


static int
setsosandorder (CPXENVptr env, CPXLPptr lp)
{
   /* Priority order information */
   int  colindex[2];
   int  priority[2];
   int  direction[2];

   /* SOS set information */
   char   sostype[1];
   int    sosbeg[1];
   int    sosind[2];
   double sosref[2];

   int  status = 0;

   /* Note - for this example, the priority order and SOS information
      are just made up for illustrative purposes.  The priority order
      is not necessarily a good one for this particular problem.  */

   /* Set order info.  Variables 1 and 3 will be in the priority order,
      with respective priorities of 8 and 7, and with respective
      branching directions of up and down */

   colindex[0]  = 1;                 colindex[1]  = 3;
   priority[0]  = 8;                 priority[1]  = 7;
   direction[0] = CPX_BRANCH_UP;     direction[1] = CPX_BRANCH_DOWN;

   status = CPXcopyorder (env, lp, 2, colindex, priority, direction);
   if ( status ) {
      fprintf (stderr, "CPXcopyorder failed.\n");
      goto TERMINATE;
   }

   /* Set SOS set info.  Create one SOS type 1 set, with variables
      2 and 3 in it, and reference values 25 and 18  for the 2 variables,
      respectively.   */

   sostype[0] = CPX_TYPE_SOS1;
   sosbeg[0]  = 0;
   sosind[0]  = 2;    sosind[1] = 3;   
   sosref[0]  = 25;   sosref[1] = 18; 

   status = CPXcopysos (env, lp, 1, 2, sostype, sosbeg,
                        sosind, sosref, NULL);
   if ( status ) {
      fprintf (stderr, "CPXcopysos failed.\n");
      goto TERMINATE;
   }

   /* To assist in debugging, write the order to a file. */

   status = CPXordwrite (env, lp, "mipex3.ord");
   if ( status ) {
      fprintf (stderr, "CPXordwrite failed.\n");
      goto TERMINATE;
   }

TERMINATE:

   return (status);

}




/* This simple routine frees up the pointer *ptr, and sets *ptr to NULL */

static void
free_and_null (char **ptr)
{
   if ( *ptr != NULL ) {
      free (*ptr);
      *ptr = NULL;
   }
} /* END free_and_null */  
