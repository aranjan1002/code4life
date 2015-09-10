/* --------------------------------------------------------------------------
 * File: tuneset.c
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

/* tuneset.c - Tune parameters for a set of problems */

/* To run this example, command line arguments are required.
   i.e.,   tuneset [options] file1 file2 ... filen
   where 
       each filei is the name of a file, with .mps, .lp, or 
          .sav extension
       options are described in usage().

   Example:
       tuneset  mexample.mps
 */

/* Bring in the CPLEX function declarations and the C library 
   header file stdio.h with the following single include. */

#include <ilcplex/cplex.h>

/* Bring in the declarations for the string and character functions 
   and malloc */

#include <ctype.h>
#include <stdlib.h>
#include <string.h>

/* Include declarations for functions in this program */

static void
   free_and_null (char **ptr),
   usage         (char *progname);

#define BUFSZ 128


int
main (int argc, char *argv[])
{
   CPXENVptr     env = NULL;
   int           status;
   
   int    filecnt;
   char   **filenames;
   char   **filetypes = NULL;

   char   fixedfile[BUFSZ];
   char   tunedfile[BUFSZ];
  
   int    i;
   int    tunestat = 0;
   int    tunemeasure;
   int    mset = 0;

   int    fcnt = 0;
   int    *fnum = NULL;

   int    icnt = 0;
   int    *inum = NULL;
   int    *ival = NULL;
 
   int    dcnt = 0;
   int    *dnum = NULL;
   double *dval = NULL;

   /* Process the command line arguments */

   if ( argc < 2 ) {
      usage (argv[0]);
      goto TERMINATE;
   }

   *fixedfile = '\0';
   *tunedfile = '\0';
   for (i = 1; i < argc; i++) {
      if ( argv[i][0] != '-' )  break;
      switch ( argv[i][1] ) {
      case 'a':
         tunemeasure = CPX_TUNE_AVERAGE;
         mset = 1;
         break;
      case 'm':
         tunemeasure = CPX_TUNE_MINMAX;
         mset = 1;
         break;
      case 'f':
         i++;
         strncpy (fixedfile, argv[i], BUFSZ);
         break;
      case 'o':
         i++;
         strncpy (tunedfile, argv[i], BUFSZ);
         break;
      }
   }
   filecnt = argc - i;
   filenames = argv + i;

   /* Initialize the CPLEX environment */

   env = CPXopenCPLEX (&status);

   if ( env == NULL ) {
      char  errmsg[1024];
      fprintf (stderr, "Could not open CPLEX environment.\n");
      CPXgeterrorstring (env, status, errmsg);
      fprintf (stderr, "%s", errmsg);
      goto TERMINATE;
   }

   printf ("Problem set:\n");
   for (i = 0; i < filecnt; i++) {
      printf ("  %s\n", filenames[i]);
   }

   /* Turn on output to the screen */

   status = CPXsetintparam (env, CPX_PARAM_SCRIND, CPX_ON);
   if ( status ) {
      fprintf (stderr, 
               "Failure to turn on screen indicator, error %d.\n", status);
      goto TERMINATE;
   }

   if ( mset ) {
      status = CPXsetintparam (env, CPX_PARAM_TUNINGMEASURE, tunemeasure);
      if ( status ) {
         fprintf (stderr, 
                 "Failure to set tuning measure, error %d.\n", status);
         goto TERMINATE;
      }
   }

   /* Read the fixed parameter file and collect the settings
      to pass to the tuning step. */

   if ( *fixedfile ) {
      int surplus;

      status = CPXreadcopyparam (env, fixedfile);
      if ( status ) {
         fprintf (stderr,
                  "Failure to read fixed parameter file\n");
         goto TERMINATE;
      }

      CPXgetchgparam (env, &fcnt, NULL, 0, &surplus);
      fcnt = -surplus;

      fnum = (int *)    malloc (fcnt*sizeof(int));
      inum = (int *)    malloc (fcnt*sizeof(int));
      ival = (int *)    malloc (fcnt*sizeof(int));
      dnum = (int *)    malloc (fcnt*sizeof(int));
      dval = (double *) malloc (fcnt*sizeof(double));
      if ( fnum == NULL ||
           inum == NULL ||
           ival == NULL ||
           dnum == NULL ||
           dval == NULL   ) {
         fprintf (stderr,
                  "Failure to allocate memory for fixed parameters.\n");
         status = CPXERR_NO_MEMORY;
         goto TERMINATE;
      }

      status = CPXgetchgparam (env, &fcnt, fnum, fcnt, &surplus);
      if ( status )  goto TERMINATE;

      for (i = 0; i < fcnt; i++) {
         int itype;
         CPXgetparamtype (env, fnum[i], &itype);
         if ( itype == CPX_PARAMTYPE_INT ) {
            inum[icnt] = fnum[i];
            CPXgetintparam (env, fnum[i], ival+icnt); 
            icnt++;
         }
         else if ( itype == CPX_PARAMTYPE_DOUBLE ) {
            dnum[dcnt] = fnum[i];
            CPXgetdblparam (env, fnum[i], dval+dcnt); 
            dcnt++;
         }
      }
      /* Clear nondefault settings */
      CPXsetdefaults (env);
      CPXsetintparam (env, CPX_PARAM_SCRIND, CPX_ON);
   }

   /* Tune */

   status = CPXtuneparamprobset (env, filecnt, filenames, filetypes,
                                 icnt, inum, ival, dcnt, dnum, dval,
                                 0, NULL, NULL, &tunestat);

   if ( status ) {
      fprintf (stderr, "Failed to tune, status = %d.\n", status);
      goto TERMINATE;
   }

   if ( tunestat ) {
      fprintf (stderr, "Tuning incomplete, status = %d.\n", tunestat);
      goto TERMINATE;
   }
   else {
      printf ("Tuning complete.\n");
   }

   if ( *tunedfile != '\0') {
      status = CPXwriteparam (env, tunedfile);
      if ( status ) {
         fprintf (stderr, "Failed to write tuned parameter file.\n");
         goto TERMINATE;
      }
      printf ("Tuned parameters written to file '%s'.\n",
              tunedfile);
   }
   
TERMINATE:

   /* Free the allocated vectors */

   free_and_null ((char **) &fnum);
   free_and_null ((char **) &inum);
   free_and_null ((char **) &ival);
   free_and_null ((char **) &dnum);
   free_and_null ((char **) &dval);

   /* Free up the CPLEX environment, if necessary */

   if ( env != NULL ) {
      int xstatus = CPXcloseCPLEX (&env);

      /* Note that CPXcloseCPLEX produces no output,
         so the only way to see the cause of the error is to use
         CPXgeterrorstring.  For other CPLEX routines, the errors will
         be seen if the CPX_PARAM_SCRIND indicator is set to CPX_ON. */

      if ( xstatus ) {
         char  errmsg[1024];
         fprintf (stderr, "Could not close CPLEX environment.\n");
         CPXgeterrorstring (env, status, errmsg);
         fprintf (stderr, "%s", errmsg);
         status = xstatus;
      }
   }
   return (status);

}  /* END main */


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
   fprintf (stderr,"Usage: %s [options] file1 file2 ... filen\n", progname);
   fprintf (stderr,"   where\n");
   fprintf (stderr,"      filei is a file with extension MPS, SAV, or LP\n");
   fprintf (stderr,"      and options are:\n");
   fprintf (stderr,"         -a for average measure\n"); 
   fprintf (stderr,"         -m for minmax measure\n ");
   fprintf (stderr,"         -f <file> where file is a fixed parameter file\n");
   fprintf (stderr,"         -o <file> where file is the tuned parameter file\n");
   fprintf (stderr," Exiting...\n");
} /* END usage */
