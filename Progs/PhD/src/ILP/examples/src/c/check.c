/* --------------------------------------------------------------------------
 * File: check.c
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


#include <ilcplex/cplex.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>


#define XABS(x)        fabs((x))


#ifndef SYSVAX
#define BIGREAL        1.0E+75
#endif
#ifdef SYSVAX
#define BIGREAL        1.0E+30
#endif

#define BIGINT         2100000000

#define EPSZERO        1.0E-10

#define TRUE    1
#define FALSE   0

#define SUCCEED 0
#define FAIL    1

#define MAXERRCNT  20
#define MAXWARNCNT  5
#define MAXNAMELEN 255




static int
   checkinit    (CPXCENVptr env, CPXCLPptr lp, const char *routinename),
   checkdata    (CPXCENVptr env, int numcols, int numrows, int objsen,
                 const double *obj, const double *rhs, const char *sense,
                 const int *matbeg, const int *matcnt, const int *matind,
                 const double *matval, const double *lb, const double *ub,
                 const double *rngval, char **colname, char **rowname),
   checkvaldata (CPXCENVptr env, CPXCLPptr lp, int cnt, int rows, int cols, 
                 const int *rowind, const int *colind, const double *values,
                 const char *rowindname, const char *colindname,
                 const char *valuesname),
   checknan     (CPXCENVptr env, const double *dx, int len,
                 const char *arrayname, CPXCHANNELptr errorchan,
                 CPXCHANNELptr reschan),
   checkmatval  (CPXCENVptr env, const int *matbeg, const int *matcnt,
                 const double *matval, int numcols, const char *arrayname,
                 CPXCHANNELptr errorchan, CPXCHANNELptr reschan),
   checkmalloc  (void),
   checksizes   (CPXCENVptr env, int numcols, int numrows, 
                 char **colname, char **rowname,
                 CPXCHANNELptr errorchan, CPXCHANNELptr warnchan,
                 CPXCHANNELptr reschan),
   checkrows    (CPXCENVptr env, int numcols, int numrows,
                 const double *rhs, const char *sense, const int *matbeg,
                 const int *matcnt, const double *rngval,
                 CPXCHANNELptr errorchan, CPXCHANNELptr warnchan,
                 CPXCHANNELptr reschan),
   checkcols    (CPXCENVptr env, int numcols, int numrows,
                 const double *obj, const int *matbeg, const int *matcnt,
                 const int *matind, const double *matval, const double *lb,
                 const double *ub, int *zeroind_p, double *maxval_p,
                 double *minval_p, CPXCHANNELptr errorchan, 
                 CPXCHANNELptr warnchan, CPXCHANNELptr reschan),
   checknames   (CPXCENVptr env, int numcols, int numrows,
                 char **colname, char **rowname,
                 CPXCHANNELptr errorchan, CPXCHANNELptr warnchan,
                 CPXCHANNELptr reschan);


#ifdef CHECK_NAME_CONFLICT
static int
   checknameconflicts (CPXCHANNELptr errorchan, CPXCHANNELptr warnchan,
                       CPXCHANNELptr reschan);
#endif



int CPXPUBLIC
CPXcheckcopylpwnames (CPXCENVptr   env,
                      CPXCLPptr    lp,
                      int          numcols,
                      int          numrows,
                      int          objsen,
                      const double *obj,
                      const double *rhs,
                      const char   *sense, 
                      const int    *matbeg,
                      const int    *matcnt,
                      const int    *matind,
                      const double *matval, 
                      const double *lb,
                      const double *ub,
                      const double *rngval, 
                      char         **colname,
                      char         **rowname)
{
   int status;

   status = checkinit (env, lp, "CPXcheckcopylpwnames");
   if ( !status ) {
      status = checkdata (env, numcols, numrows, objsen, obj, 
                          rhs, sense, matbeg, matcnt, matind, matval,
                          lb, ub, rngval, colname, rowname);
   }
   return (status);

} /* END cpxcheckcopylpwnames */


int CPXPUBLIC
CPXcheckcopylp (CPXCENVptr   env,
                CPXCLPptr    lp,
                int          numcols,
                int          numrows,
                int          objsen,
                const double *obj,
                const double *rhs,
                const char   *sense, 
                const int    *matbeg,
                const int    *matcnt,
                const int    *matind,
                const double *matval, 
                const double *lb,
                const double *ub,
                const double *rngval)
{
   int status;

   status = checkinit (env, lp, "CPXcheckcopylp");
   if ( !status ) {
      status = checkdata (env, numcols, numrows, objsen, obj, 
                          rhs, sense, matbeg, matcnt, matind, matval,
                          lb, ub, rngval, NULL, NULL);
   }
   return (status);

} /* END cpxcheckcopylp */


int CPXPUBLIC
CPXcheckcopyctype (CPXCENVptr env,
                   CPXCLPptr  lp,
                   const char *ctype)
{
   int           status = SUCCEED;
   int           j, numcols;
   CPXCHANNELptr errorchan = NULL;

   status = checkinit (env, lp, "CPXcheckcopyctype");
   if ( status )  goto TERMINATE;

   /* Get the CPLEX standard channels; all output is through
      these channels  */

   CPXgetchannels (env, NULL, NULL, &errorchan, NULL);

   numcols = CPXgetnumcols (env, lp);

   if ( ctype != NULL ) {
      for (j = 0; j < numcols; j++) {
         if ( ctype[j] != CPX_CONTINUOUS &&
              ctype[j] != CPX_BINARY     &&
              ctype[j] != CPX_INTEGER    &&
              ctype[j] != CPX_SEMICONT   &&
              ctype[j] != CPX_SEMIINT      ) {
            CPXmsg (errorchan,
                    "ctype must be '%c', '%c', '%c', '%c', or '%c' ",
                    CPX_CONTINUOUS, CPX_BINARY, CPX_INTEGER,
                    CPX_SEMICONT, CPX_SEMIINT);
            CPXmsg (errorchan,
                    "entry %d is '%c'.\n", j, ctype[j]);
            status = FAIL;
            goto TERMINATE;
         }
      }
   }

TERMINATE:

   return (status);

} /* END cpxcheckcopyctype */


int CPXPUBLIC
CPXcheckcopysos (CPXCENVptr   env,
                 CPXCLPptr    lp,
                 int          numsos,
                 int          numsosnz,
                 const char   *sostype,
                 const int    *sosbeg,
                 const int    *sosind,
                 const double *soswt,
                 char          **sosname)
{
   int           i, j, k, count, minsoslen, maxsoslen, numcols;
   int           warncnt, status = SUCCEED;
   CPXCHANNELptr errorchan = NULL;
   CPXCHANNELptr warnchan  = NULL; 
   CPXCHANNELptr reschan   = NULL; 
   char          sosindstr[32], soswtstr[32];

   status = checkinit (env, lp, "CPXcheckcopysos");
   if ( status )  goto TERMINATE;

   /* Get the CPLEX standard channels; all output is through
      these channels  */

   CPXgetchannels (env, &reschan, &warnchan, &errorchan, NULL);

   /* Check for NULL pointers */

   if ( sostype == NULL ||
        sosbeg  == NULL ||
        sosind  == NULL ||
        soswt   == NULL  ) {
      CPXmsg (errorchan, "The following arrays are NULL:\n");
      CPXmsg (errorchan, "%s%s%s%s",
              (sostype == NULL ? "sostype\n": ""),
              (sosbeg  == NULL ? "sosbeg\n": ""),
              (sosind  == NULL ? "sosind\n": ""),
              (soswt   == NULL ? "soswt\n": ""));
      status = FAIL;
      goto TERMINATE;
   }

   numcols = CPXgetnumcols (env, lp);

   /* Check numsos, numsosnz for valid values. */

   if ( numsos > numcols || numsos < 0 ) {
      CPXmsg (errorchan, "Invalid value for numsos: %d.\n", numsos);
      if ( numsos > numcols ) 
         CPXmsg (errorchan, 
                 "This value can be at most the number of variables (%d).\n",
                 numcols);
      else
         CPXmsg (errorchan, "This value must be nonnegative.\n");
                 
      status = FAIL;
      goto TERMINATE;
   }

   if ( numsosnz > numsos*numcols || numsosnz < 0 ) {

      /* Only issue warning since if numsosnz too big, duplicate
         entries exist; terminate after user sees duplicates output. */

      CPXmsg (warnchan, "Invalid value for numsosnz: %d.\n", numsosnz);
      if ( numsosnz < 0 )
         CPXmsg (errorchan, "This value must be nonnegative.\n");
      else
         CPXmsg (errorchan, 
                 "Each of the %d SOSs can contain at most (%d) variables.\n", 
                 numsos, numcols);
   }

   /* Check that sosbeg is in ascending order, has correct values relative
      to numsosnz. */

   maxsoslen = 0;
   minsoslen = numcols;
   count     = 0;
   for (i = 0; i < numsos-1; i++) {
      k = sosbeg[i+1] - sosbeg[i];
      if ( k < 0 ) {
         CPXmsg (errorchan,
                 "Components of sosbeg must be ascending, ");
         CPXmsg (errorchan,
                 "but sosbeg[%d] (%d) > sosbeg[%d] (%d).\n",
                 i, sosbeg[i], i+1, sosbeg[i+1]);
         status = FAIL;
         goto TERMINATE;
      }
      maxsoslen = k > maxsoslen ? k : maxsoslen;
      minsoslen = k < minsoslen ? k : minsoslen;
      count += k;
   }
   CPXmsg (reschan, "Number of entries in largest SOS: %d.\n", maxsoslen);
   CPXmsg (reschan, "Number of entries in smallest SOS: %d.\n", minsoslen);
   count += (numsosnz - sosbeg[numsos-1]);
   if ( count != numsosnz ) {
      CPXmsg (warnchan, "Value of numsosnz (%d) does not match ", numsosnz);
      CPXmsg (warnchan, "number of entries in sosbeg array (%d).\n", count);
   }

   if ( numsos > 0 ) {
      if ( sosbeg[0] < 0 ) {
         CPXmsg (errorchan,
                 "Negative entry in sosbeg: sosbeg[0] = %d\n", sosbeg[0]);
         status = FAIL;
         goto TERMINATE;
      }
      if ( sosbeg[numsos-1] >= numsosnz ) {
         CPXmsg (errorchan,
                 "Invalid value in sosbeg: sosbeg[%d] = %d\n", numsos-1, 
                 sosbeg[numsos-1]);
         CPXmsg (errorchan, "This value must be < numsosnz, which is %d.\n",
                 numsosnz);
         status = FAIL;
         goto TERMINATE;
      }
   }
   
   /* Check sostype array; each item must contain '1' or '2'. */

   for (i = 0; i < numsos; i++) {
      if ( sostype[i] != CPX_TYPE_SOS1 && sostype[i] != CPX_TYPE_SOS2 ) {
         CPXmsg (errorchan, "Invalid value in sostype: sostype[%d] = %c.\n",
                 i, sostype[i]);
         status = FAIL;
         goto TERMINATE;
      }
   }

   /* Loop through each individual SOS set.  Check for duplicate entries
      in sosind, invalid entries in soswt. */

   count   = numsosnz;
   warncnt = 0;
   for (i = numsos-1; i >= 0; i--) {
      count -= sosbeg[i];
      if ( count <= 1 ) {
         warncnt++;
         if ( warncnt <= MAXWARNCNT ) 
            CPXmsg (warnchan, "SOS set %d contains <= 1 elements.\n", i);
         
      }
         
      sprintf (sosindstr, "(sosind + %d)", sosbeg[i]);
      sprintf (soswtstr, "(soswt + %d)", sosbeg[i]);

      status = checkvaldata (env, lp, count, 0, numcols, NULL, 
                             &sosind[sosbeg[i]], NULL, NULL, 
                             sosindstr, soswtstr);
      if ( status ) goto TERMINATE;

      /* Also test for non-unique reference values in each SOS. */

      for (j = 0; j < count; j++) {
         for (k = sosbeg[i] + j + 1; k < sosbeg[i] + count; k++) {
            if ( fabs(soswt[sosbeg[i] + j] - soswt[k]) < EPSZERO ) {
               CPXmsg (errorchan, "Duplicate entries in SOS %d; ", i);
               CPXmsg (errorchan, "sosval[%d] = %g, sosval[%d] = %g.\n",
                       sosbeg[i] + j, soswt[sosbeg[i] + j], k, soswt[k]);
               status = FAIL;
               goto TERMINATE;
            }             
	 }
      }     
      count = sosbeg[i];
   }
   if ( warncnt > MAXWARNCNT ) 
      CPXmsg (warnchan,
         "%d short SOS warnings not printed.\n", warncnt - MAXWARNCNT);
   
   /* Check the soswt array for unrepresentable values. */

   status = checknan (env, soswt, numsosnz, "soswt", errorchan, reschan);
   if ( status ) goto TERMINATE;

   /* Check names */

   warncnt = 0;
   if ( sosname ) {
      for (i = 0; i < numsos; i++) {
         if ( strlen(sosname[i]) > MAXNAMELEN ) {
            warncnt++;
            if ( warncnt <= MAXWARNCNT ) {
               CPXmsg (warnchan,
         "Warning:  Name for SOS %d (%s) exceeds %d characters.\n",
                       i, sosname[i], MAXNAMELEN);
               CPXmsg (warnchan,
                "CPLEX may not be able to write LP or MPS files.\n");
            }
         }
      }
      if ( warncnt > MAXWARNCNT ) {
         CPXmsg (warnchan,
                 "%d SOS name length warnings not printed.\n",
                 warncnt - MAXWARNCNT);
      }
      CPXflushstdchannels (env);
   }

   CPXflushstdchannels (env);
TERMINATE:

   return (status);

} /* END cpxcheckcopysos */


int CPXPUBLIC
CPXcheckaddcols (CPXCENVptr   env,
                 CPXCLPptr    lp,
                 int          ccnt,
                 int          nzcnt,
                 const double *obj,
                 const int    *cmatbeg,
                 const int    *cmatind,
                 const double *cmatval,
                 const double *lb,
                 const double *ub,
                 char         **colname)
{
   int           status = SUCCEED; 
   CPXCHANNELptr errorchan = NULL; 
   CPXCHANNELptr warnchan  = NULL; 
   CPXCHANNELptr reschan   = NULL; 
   int           i, k, collen, rows;
   char          buffer[64];
 
   status = checkinit (env, lp, "CPXcheckaddcols");
   if ( status )  goto TERMINATE;

   /* Here, we get the CPLEX standard channels.  Alternatively, you
      you create your own, or set reschan, warnchan, errorchan, to be
      a channel you want to use. */

   CPXgetchannels (env, &reschan, &warnchan, &errorchan, NULL);

   /* Check for NULL pointers */

   if ( cmatbeg == NULL ||
        cmatind == NULL ||
        cmatval == NULL   ) {
      CPXmsg (errorchan, "The following arrays are NULL:\n");
      CPXmsg (errorchan, "%s%s%s",
              (cmatbeg == NULL ? "cmatbeg\n": ""),
              (cmatind == NULL ? "cmatind\n": ""),
              (cmatval == NULL ? "cmatval\n": ""));
      status = FAIL;
      goto TERMINATE;
   }

   status = checknan (env, obj, ccnt, "obj", errorchan, reschan);
   if ( status )  goto TERMINATE;
   if ( obj == NULL ) {
      CPXmsg (warnchan, "Warning: obj array is NULL.\n");
      CPXmsg (warnchan, 
              "All added columns will have objective coefficient of 0.\n");
   }
   status = checknan (env, lb, ccnt, "lb", errorchan, reschan);
   if ( status )  goto TERMINATE;
   if ( lb == NULL ) {
      CPXmsg (warnchan, "Warning: lower bound array is NULL.\n");
      CPXmsg (warnchan, 
              "All added columns will have lower bounds of 0.\n");
   }
   status = checknan (env, ub, ccnt, "ub", errorchan, reschan);
   if ( status )  goto TERMINATE;
   if ( ub == NULL ) {
      CPXmsg (warnchan, "Warning: upper bound array is NULL.\n");
      CPXmsg (warnchan, 
              "All added columns will have infinite upper bounds.\n");
   }

   /* Verify that components of cmatbeg are in ascending order */

   for (i = 0; i < ccnt - 1; i++) {
      if ( cmatbeg[i] > cmatbeg[i+1] ) {
         CPXmsg (errorchan,
                 "Components of cmatbeg must be ascending, ");
         CPXmsg (errorchan,
                 "but cmatbeg[%d] (%d) > cmatbeg[%d] (%d).\n",
                 i, cmatbeg[i], i+1, cmatbeg[i+1]);
         status = FAIL;
         goto TERMINATE;
      }
   }
   if ( ccnt > 0 ) {
      if ( cmatbeg[0] < 0 ) {
         CPXmsg (errorchan,
                 "Negative entry in cmatbeg: cmatbeg[0] = %d\n", cmatbeg[0]);
         status = FAIL;
         goto TERMINATE;
      }
      if ( cmatbeg[ccnt-1] > nzcnt ) {
         CPXmsg (errorchan,
                 "Invalid value in cmatbeg: cmatbeg[%d] = %d\n", ccnt-1, 
                 cmatbeg[ccnt-1]);
         CPXmsg (errorchan, "This value must be < nzcnt, which is %d.\n",
                 nzcnt);
         status = FAIL;
         goto TERMINATE;
      }
   }
   else if ( ccnt < 0 ) {
      CPXmsg (errorchan, "Negative value for ccnt (%d).\n",
              ccnt);
      status = FAIL;
      goto TERMINATE;
   }

   /* Test the column oriented data structures for errors. */

   rows = CPXgetnumrows (env, lp);
   for (i = ccnt - 1, k = nzcnt; i >= 0; i--) {
      collen = k - cmatbeg[i];
      k      = cmatbeg[i];
      sprintf (buffer, "cmatval for column %d", i);
      status = checkvaldata (env, lp, collen, rows, 0, 
                             &cmatind[cmatbeg[i]], NULL, 
                             &cmatval[cmatbeg[i]], "cmatind", NULL, buffer);
      if ( status ) {
         CPXmsg (errorchan, "Error detected in column %d.\n", i);
         goto TERMINATE;

      }
   }

TERMINATE:

   return (status);

} /* END cpxcheckaddcols */


int CPXPUBLIC
CPXcheckaddrows (CPXCENVptr env,
                 CPXCLPptr lp,
                 int ccnt,
                 int rcnt,
                 int nzcnt,
                 const double *rhs,
                 const char *sense, 
                 const int *rmatbeg,
                 const int *rmatind,
                 const double *rmatval, 
                 char **colname,
                 char **rowname)
{
   int           status = SUCCEED; 
   CPXCHANNELptr errorchan = NULL; 
   CPXCHANNELptr warnchan  = NULL; 
   CPXCHANNELptr reschan   = NULL; 
   CPXFILEptr    fp        = NULL;
   int           i, k, rowlen, colstot, has_ranges;
   double        dx;
   char          buffer[64];

   status = checkinit (env, lp, "CPXcheckaddrows");
   if ( status )  goto TERMINATE;

   /* Here, we get the CPLEX standard channels.  Alternatively, you
      you create your own, or set reschan, warnchan, errorchan, to be
      a channel you want to use. */

   CPXgetchannels (env, &reschan, &warnchan, &errorchan, NULL);

   /* Check for NULL pointers */

   if ( rmatbeg == NULL ||
        rmatind == NULL ||
        rmatval == NULL   ) {
      CPXmsg (errorchan, "The following arrays are NULL:\n");
      CPXmsg (errorchan, "%s%s%s",
              (rmatbeg == NULL ? "rmatbeg\n": ""),
              (rmatind == NULL ? "rmatind\n": ""),
              (rmatval == NULL ? "rmatval\n": ""));
      status = FAIL;
      goto TERMINATE;
   }

   status = checknan (env, rhs, rcnt, "rhs", errorchan, reschan);
   if ( status )  goto TERMINATE;
   if ( rhs == NULL ) {
      CPXmsg (warnchan, "Warning: rhs array is NULL; ");
      CPXmsg (warnchan, "all added rows will have right hand sides of 0.\n");
   }

   /* Check for invalid values in the sense array. 
      Suppress output from CPXgetrngval if no ranged rows exist. */

   status = CPXgetlogfile (env, &fp);
   if ( fp != NULL )  status = CPXsetlogfile ((CPXENVptr) env, NULL);
   CPXgetintparam (env, CPX_PARAM_SCRIND, &k);
   CPXsetintparam ((CPXENVptr) env, CPX_PARAM_SCRIND, CPX_OFF);
   has_ranges = (CPXgetrngval (env, lp, &dx, 0, 0) != CPXERR_NO_RNGVAL);
   CPXsetintparam ((CPXENVptr) env, CPX_PARAM_SCRIND, k);
   if ( fp != NULL )  status = CPXsetlogfile ((CPXENVptr) env, fp);
   if ( sense != NULL ) {
      for (i = 0; i < rcnt; i++) {
         if ( sense[i] != 'L' &&
              sense[i] != 'G' &&
              sense[i] != 'E' &&
              sense[i] != 'R'   ) {
            CPXmsg (errorchan,
                    "Rows sense must be 'L', 'G', 'E', or 'R':  ");
            CPXmsg (errorchan,
                    "Entry %d is '%c'.\n", i, sense[i]);
            status = FAIL;
            goto TERMINATE;
         }
         else if ( (sense[i] == 'R')  &&  !has_ranges ) {
            CPXmsg (errorchan,
              "Row %d has sense 'R' but range-value vector is NULL.\n",
              i);
            status = FAIL;
            goto TERMINATE;
         }
      }
   }
   else {
      CPXmsg (warnchan, "Warning: sense array is NULL; ");
      CPXmsg (warnchan, "all added rows will be considered equalities.\n");
   }


   /* Verify that components of rmatbeg are in ascending order */

   for (i = 0; i < rcnt - 1; i++) {
      if ( rmatbeg[i] > rmatbeg[i+1] ) {
         CPXmsg (errorchan,
                 "Components of rmatbeg must be ascending, ");
         CPXmsg (errorchan,
                 "but rmatbeg[%d] (%d) > rmatbeg[%d] (%d).\n",
                 i, rmatbeg[i], i+1, rmatbeg[i+1]);
         status = FAIL;
         goto TERMINATE;
      }
   }

   /* Make sure rmatbeg has no negative entries, and that nzcnt is
      reasonable. Since we know that rmatbeg is in ascending order 
      when we get here, just look at rmatbeg[0] and rmatbeg[rcnt-1]. */

   if ( rcnt > 0 ) {
      if ( rmatbeg[0] < 0 ) {
         CPXmsg (errorchan,
                 "Negative entry in rmatbeg: rmatbeg[0] = %d\n", rmatbeg[0]);
         status = FAIL;
         goto TERMINATE;
      }
      if ( rmatbeg[rcnt-1] > nzcnt ) {
         CPXmsg (errorchan,
                 "Invalid value in rmatbeg: rmatbeg[%d] = %d\n", rcnt-1, 
                 rmatbeg[rcnt-1]);
         CPXmsg (errorchan, "This value must be < nzcnt, which is %d.\n",
                 nzcnt);
         status = FAIL;
         goto TERMINATE;
      }
   }
   else if ( rcnt < 0 ) {
      CPXmsg (errorchan, "Negative value for rcnt (%d).\n",
              rcnt);
      status = FAIL;
      goto TERMINATE;
   }

   /* Test the row oriented data structures for errors. */

   colstot = CPXgetnumcols (env, lp) + ccnt;
   for (i = rcnt - 1, k = nzcnt; i >= 0; i--) {
      rowlen = k - rmatbeg[i];
      k      = rmatbeg[i];
      sprintf (buffer, "rmatval for row %d", i);
      status = checkvaldata (env, lp, rowlen, 0, colstot, NULL, 
                             &rmatind[rmatbeg[i]], &rmatval[rmatbeg[i]],
                             NULL, "rmatind", buffer);
      if ( status ) {
         CPXmsg (errorchan, "Error detected in row %d.\n", i);
         goto TERMINATE;

      }
   }

TERMINATE:

   return (status);
   
} /* END cpxcheckaddcols */


int CPXPUBLIC
CPXcheckchgcoeflist (CPXCENVptr   env,
                     CPXCLPptr    lp,
                     int          numcoefs,
                     const int    *rowlist, 
                     const int    *collist,
                     const double *vallist)
{
   int           status = SUCCEED;
   CPXCHANNELptr errorchan = NULL;
   CPXCHANNELptr warnchan  = NULL;
   CPXCHANNELptr reschan   = NULL;
   int           i, j, k, rows, cols;
   int           *rowchgcnt = NULL;
   int           *rowchgbeg = NULL; 
   int           *colchgind = NULL;
   int           *colchgprm = NULL;
   int           *colchgcnt = NULL;

   status = checkinit (env, lp, "CPXcheckchgcoeflist");
   if ( status )  goto TERMINATE;

   /* Here, we get the CPLEX standard channels.  Alternatively, you
      you create your own, or set reschan, warnchan, errorchan, to be
      a channel you want to use. */

   CPXgetchannels (env, &reschan, &warnchan, &errorchan, NULL);

   /* Check for NULL pointers */

   if ( rowlist == NULL ||
        collist == NULL ||
        vallist == NULL   ) {
      CPXmsg (errorchan, "The following arrays are NULL:\n");
      CPXmsg (errorchan, "%s%s%s",
              (rowlist == NULL ? "rowlist\n": ""),
              (collist == NULL ? "collist\n": ""),
              (vallist == NULL ? "vallist\n": ""));
      status = FAIL;
      goto TERMINATE;
   }

   /* Check for invalid values in the indices arrays. */

   rows = CPXgetnumrows (env, lp);
   cols = CPXgetnumcols (env, lp);

   rowchgcnt = (int *) calloc((size_t) rows, sizeof(int));
   rowchgbeg = (int *) calloc((size_t) rows, sizeof(int));
   colchgind = (int *) calloc((size_t) numcoefs, sizeof(int));
   colchgprm = (int *) calloc((size_t) numcoefs, sizeof(int));
   colchgcnt = (int *) calloc((size_t) cols, sizeof(int));
   if ( rowchgcnt == NULL || rowchgbeg == NULL || 
        colchgind == NULL || colchgprm == NULL || colchgcnt == NULL ) {
      CPXmsg (errorchan, 
	      "Work vector mallocs failed in CPXcheckchgcoeflist.\n");
      status = FAIL;
      goto TERMINATE;
   }

   for (i = 0; i < numcoefs; i++) {
      k = rowlist[i];
      rowchgcnt[k]++;
      if ( k < 0 ) {
         CPXmsg (errorchan,
                 "Error: entry rowlist[%d] is negative (%d).\n",
                 i, k);
         status = FAIL;
         goto TERMINATE;
      }
      if ( k >= rows ) {
         CPXmsg (errorchan,
       "Error: entry rowlist[%d] (%d) invalid for number of rows (%d).\n",
                 i, k, rows);
         status = FAIL;
         goto TERMINATE;
      }

      k = collist[i];
      if ( k < 0 ) {
         CPXmsg (errorchan,
                 "Error: entry collist[%d] is negative (%d).\n",
                 i, k);
         status = FAIL;
         goto TERMINATE;
      }
      if ( k >= cols ) {
         CPXmsg (errorchan,
       "Error: entry collist[%d] (%d) invalid for number of columns (%d).\n",
                 i, k, cols);
         status = FAIL;
         goto TERMINATE;
      }
   }

   /* Check for duplicates in the indices arrays.  From the last loop,
      the rowchgcnt array contains counts for the # of coefficients 
      changed in each row of the matrix.  Use this to construct a list
      of column indices for the changes in each row (i.e. the colchgind
      array below is the collist array sorted by rows).  Then test for 
      duplicate column indices among each row that had more than one
      coefficient changed.  */

   for (i = 1; i < rows; i++)
      rowchgbeg[i] = rowchgbeg[i-1] + rowchgcnt[i-1];

   for (i = 0; i < numcoefs; i++) {
      j = collist[i];
      k = rowlist[i];
      colchgind[rowchgbeg[k]] = j;
      colchgprm[rowchgbeg[k]] = i;
      rowchgbeg[k]++;
   }

   rowchgbeg[0] = 0;
   for (i = 1; i < rows; i++)
      rowchgbeg[i] = rowchgbeg[i-1] + rowchgcnt[i-1];
   

   for (i = 0; i < numcoefs; i++) {
      k = rowlist[i];
      if ( rowchgcnt[k] > 1 ) { /* Need to check for a duplicate. */

         /* For each row that has more than one change, count the
            number of occurrences for the column indices associated
            with the coefficient changes in that row.  Any column
            index that appears more than once means a particular
            matrix element was changed more than once.  The code
            flags this as a warning, since in some cases it may be
            convenient to generate repeated changes.  Once we have
            tested a row, set it's # of changes to 0 so we don't
            reexamine it later. */

         for (j = rowchgbeg[k]; j < rowchgbeg[k] + rowchgcnt[k]; j++)
            colchgcnt[colchgind[j]]++;
         for (j = rowchgbeg[k] + rowchgcnt[k] - 1; j >= rowchgbeg[k]; 
              j--) {
            if ( colchgcnt[colchgind[j]] > 1 ) {
               CPXmsg (warnchan, "Warning: the same matrix entry was ");
               CPXmsg (warnchan, "changed more than once.\n");
               CPXmsg (warnchan, "Rowlist[%d] = %d, collist[%d] = %d.\n",
                       colchgprm[j], rowlist[i], colchgprm[j], collist[i]);
	    }
	 }
         for (j = rowchgbeg[k]; j < rowchgbeg[k] + rowchgcnt[k]; j++)
            colchgcnt[colchgind[j]] = 0;

         rowchgcnt[k] = 0;
      }
   }


   /* Check for NANs in the values array. */

   status = checknan (env, vallist, numcoefs, "vallist", 
                      errorchan, reschan);

TERMINATE:

   if ( rowchgcnt )  free ((char *) rowchgcnt); 
   if ( rowchgbeg )  free ((char *) rowchgbeg); 
   if ( colchgind )  free ((char *) colchgind); 
   if ( colchgprm )  free ((char *) colchgprm); 
   if ( colchgcnt )  free ((char *) colchgcnt); 

   return (status);

} /* END cpxcheckchgcoeflist */


int CPXPUBLIC
CPXcheckvals (CPXCENVptr   env,
              CPXCLPptr    lp,
              int          cnt,
              const int    *rowind, 
              const int    *colind,
              const double *values)
{
   int status = SUCCEED;
   int rows, cols;
   
   status = checkinit (env, lp, "CPXcheckvals");
   if ( status )  goto TERMINATE;

   rows = CPXgetnumrows (env, lp);
   cols = CPXgetnumcols (env, lp);
   status = checkvaldata (env, lp, cnt, rows, cols, rowind, colind, values,
                          "rowind", "colind", "values");

TERMINATE:

   return (status);

} /* END cpxcheckvals */


int CPXPUBLIC
CPXcheckcopyqpsep (CPXCENVptr   env,
                   CPXCLPptr    lp,
                   const double *qsepvec)
{
   int           i, status = SUCCEED;
   int           numcols = 0, objsen = 0;
   double        qminval, qmaxval, t, dsen;
   char          *senstr, *signstr;
   CPXCHANNELptr errorchan = NULL;
   CPXCHANNELptr warnchan = NULL;
   CPXCHANNELptr reschan = NULL;

   status = checkinit (env, lp, "CPXcheckcopyqpsep");
   if ( status )  goto TERMINATE;

   /* Get the CPLEX standard channels; all output is through
      these channels  */

   CPXgetchannels (env, &reschan, &warnchan, &errorchan, NULL);

   /* Check for NULL pointers */

   if ( qsepvec == NULL ) {
      CPXmsg (errorchan, "The qsepvec array is NULL.\n");
      status = FAIL;
      goto TERMINATE;
   }
   objsen = CPXgetobjsen (env, lp);
   numcols = CPXgetnumcols (env, lp);

   CPXmsg (reschan, "Checking QP (separable) problem data:\n");

   qminval = BIGREAL;
   qmaxval = 0.0;

   senstr  = objsen == 1 ? "minimizing" : "maximizing";
   signstr = objsen == 1 ? "Negative"   : "Positive";
   dsen    = (double) objsen;
   for (i = 0; i < numcols; i++) {
      t = dsen * qsepvec[i];
      if ( t < 0 ) {
         CPXmsg (errorchan, "%s entry in qsepvec when %s: ",
                 signstr, senstr);
         CPXmsg (errorchan, "qsepvec[%d] = %18.10e\n",
                 i, qsepvec[i]);
         status = FAIL;
         goto TERMINATE;
      }
      else {
         if ( t < qminval )  qminval = t;
         if ( t > qmaxval )  qmaxval = t;
      }
   }

   CPXmsg (reschan, "Columns                %6d\n", numcols);

   CPXmsg (reschan,
           "Minimum absolute qsepvec value %18.10e.\n",
           qminval);
   CPXmsg (reschan,
           "Maximum absolute qsepvec value %18.10e.\n",
           qmaxval);

   CPXflushstdchannels (env);

   status = checknan (env, qsepvec, numcols, "qsepvec", errorchan, reschan);
   if ( status )  goto TERMINATE;

TERMINATE:

   CPXflushstdchannels (env);

   return (status);

} /* END cpxcheckcopyqpsep */


int CPXPUBLIC
CPXcheckcopyquad (CPXCENVptr   env,
                  CPXCLPptr    lp,
                  const int    *qmatbeg,
                  const int    *qmatcnt,
                  const int    *qmatind,
                  const double *qmatval)
{
   int           status = SUCCEED;
   int           j, k, errcnt = 0;
   int           numcols = 0;
   int           *iwork = NULL;
   int           zeroind = FALSE;
   int           nzcnt = 0;
   double        qminval, qmaxval;
   CPXCHANNELptr errorchan = NULL;
   CPXCHANNELptr warnchan = NULL;
   CPXCHANNELptr reschan = NULL;

   status = checkinit (env, lp, "CPXcheckcopyquad");
   if ( status == FAIL )  goto TERMINATE;

   /* Get the CPLEX standard channels; all output is through
      these channels  */

   CPXgetchannels (env, &reschan, &warnchan, &errorchan, NULL);

   numcols = CPXgetnumcols (env, lp);

   /* Check for NULL pointers */

   if ( qmatbeg == NULL ||
        qmatcnt == NULL ||
        qmatind == NULL ||
        qmatval == NULL   ) {
      CPXmsg (errorchan, "The following arrays are NULL:\n");
      CPXmsg (errorchan, "%s%s%s%s",
              (qmatbeg == NULL ? "qmatbeg\n": ""),
              (qmatcnt == NULL ? "qmatcnt\n": ""),
              (qmatind == NULL ? "qmatind\n": ""),
              (qmatval == NULL ? "qmatval\n": ""));
      status = FAIL;
      goto TERMINATE;
   }

   CPXmsg (reschan, "Checking Q matrix (non-separable):\n");

   /* Verify that components of qmatbeg are in ascending order */

   for (j = 0; j < numcols - 1; j++) {
      if ( qmatbeg[j] > qmatbeg[j+1] ) {
         CPXmsg (errorchan,
                 "Components of qmatbeg must be ascending, ");
         CPXmsg (errorchan,
                 "but qmatbeg[%d] (%d) > qmatbeg[%d] (%d).\n",
                 j, qmatbeg[j], j+1, qmatbeg[j+1]);
         status = FAIL;
         goto TERMINATE;
      }
   }

   /* Make sure qmatbeg has no negative entries. Since we know that
      qmatbeg is in ascending order when we get here, just look at
      qmatbeg[0] */

   if (( numcols > 0 ) && ( qmatbeg[0] < 0 )) {
      CPXmsg (errorchan,
              "Negative entry in qmatbeg: qmatbeg[0] = %d\n",
              qmatbeg[0]);
      status = FAIL;
      goto TERMINATE;
   }

   /* Check for invalid column counts and overlapping columns */

   iwork = (int *) calloc((size_t) numcols, sizeof(int));
   if ( iwork == NULL ) {
      CPXmsg (errorchan, "Work vector malloc failed in checkquaddata.\n");
      status = FAIL;
      goto TERMINATE;
   }

   qmaxval = 0.0;
   qminval = BIGREAL;
   nzcnt   = 0;
   for (j = 0; j < numcols; j++) {
      if ( qmatcnt[j] < 0 ) {
         CPXmsg (errorchan,
                 "Count of entries in column %d is negative (%d).\n",
                 j, qmatcnt[j]);
         status = FAIL;
         goto TERMINATE;
      }
      if ( qmatcnt[j] > numcols ) {
         CPXmsg (errorchan,
              "Count of entries in column %d (%d) > columns (%d).\n",
              j, qmatcnt[j], numcols);
         status = FAIL;
         goto TERMINATE;
      }
      if ( (j < numcols-1)                         && 
           (qmatbeg[j] + qmatcnt[j] > qmatbeg[j+1])  ) {
         CPXmsg (errorchan,
                 "End of column %d overlaps start of column %d\n",
                 j, j+1);
         status = FAIL;
         goto TERMINATE;
      }

      /* Compute min and max absolute Q matrix elements. Also,
         check validity of entries in qmatind */

      for (k = qmatbeg[j]; k < qmatbeg[j] + qmatcnt[j]; k++) {
         if ( qmatind[k] < 0 ) {
            CPXmsg (errorchan,
                    "Entry qmatind[%d] is negative (%d).\n",
                    k, qmatind[k]);
            status = FAIL;
            goto TERMINATE;
         }
         if ( qmatind[k] >= numcols ) {
            CPXmsg (errorchan,
          "Entry qmatind[%d] (%d) invalid for number of columns (%d).\n",
          k, qmatind[k], numcols);
            status = FAIL;
            goto TERMINATE;
         }
         iwork[qmatind[k]]++;
         if ( !qmatval[k] )  zeroind = TRUE;
         else                nzcnt++;
         if ( qmatval[k]  &&  XABS(qmatval[k]) < qminval ) {
            qminval = XABS(qmatval[k]);
         }
         if ( XABS(qmatval[k]) > qmaxval ) {
            qmaxval = XABS(qmatval[k]);
         }
      }

      /* Check for duplicate row indices in a single column */

      for (k = qmatbeg[j]; k < qmatbeg[j] + qmatcnt[j]; k++) {
         if ( iwork[qmatind[k]] > 1) {
            CPXmsg (errorchan, "Duplicate row entry in qmatind: ");
            CPXmsg (errorchan, "column %d, row %d.\n",
                    j, qmatind[k]);
            status = FAIL;
            errcnt++;
            if ( errcnt >= MAXERRCNT ) {
               CPXmsg(errorchan,
                  "Quitting after %d duplicate row entries found.\n",
                  MAXERRCNT);
               status = FAIL;
               goto TERMINATE;
            }
         }
         iwork[qmatind[k]] = 0;
      }
   }
   if ( status )  goto TERMINATE;

   if ( numcols > 0 ) {
      status = checkmatval (env, qmatbeg, qmatcnt, qmatval, numcols, 
                            "qmatval", errorchan, reschan);
      if ( status )  goto TERMINATE;
   }

   CPXmsg (reschan, "Columns                  %6d\n", numcols);
   CPXmsg (reschan, "Q Matrix nonzeros        %6d\n", nzcnt);
   CPXmsg (reschan,
           "Minimum absolute nonzero Q matrix    value %18.10e.\n",
           qminval);
   CPXmsg (reschan,
           "Maximum absolute         Q matrix    value %18.10e.\n",
           qmaxval);
   if ( zeroind ) {
      CPXmsg (warnchan, "Warning:  Matrix contains zero entries.\n");
   }

TERMINATE:

   CPXflushstdchannels (env);

   if ( iwork )  free ((char *) iwork);

   return (status);

} /* END cpxcheckquad */


/*  This routine tests for conflicts in the arguments passed to 
    function CPXNETcopynetwork () */

int CPXPUBLIC
CPXNETcheckcopynet (CPXCENVptr   env,
                    CPXNETptr    net,
                    int          objsen,
                    int          nnodes,
                    const double *supply,
                    char         ** names,
                    int          narcs,
                    const int    *fromnode,
                    const int    *tonode,
                    const double *low,
                    const double *up,
                    const double *obj,
                    char         **aname)
{
   int            status    = 0;
   CPXCHANNELptr  errorchan = NULL;
   CPXCHANNELptr  warnchan  = NULL;
   CPXCHANNELptr  reschan   = NULL;

   int     i;
   double  minval, maxval;

   if ( env == NULL ) {
      printf("Environment pointer in CPXNETcheckcopynet is NULL,\
 cannot proceed.\n");
      status = FAIL;
      goto TERMINATE;
   }

   /* Here, we get the CPLEX standard channels.  Alternatively, you
      you create your own, or set reschan, warnchan, errorchan, to be
      a channel you want to use */

   CPXgetchannels (env, &reschan, &warnchan, &errorchan, NULL);

   if ( net == NULL ) {
      CPXmsg (errorchan, "NET pointer in CPXNETcheckcopynet is NULL.\n");
      status = FAIL;
   }
   else if ( checkmalloc () ) {
      CPXmsg (errorchan, "Check malloc at start of CPXNETcheckcopynet\
 failed.\n");
      status = FAIL;
   }
   if ( status == FAIL )  goto TERMINATE;

   if ( nnodes < 0 ) {
      CPXmsg (errorchan,
              "Number of nodes (%d) must be positive.\n",
              nnodes);
      status = FAIL;
      goto TERMINATE;
   }

   if ( narcs < 0 ) {
      CPXmsg (errorchan,
              "Number of arcs (%d) must be positive.\n",
              narcs);
      status = FAIL;
      goto TERMINATE;
   }

   if ( narcs > 0 && fromnode == NULL )  {
      CPXmsg (errorchan,
              "From-nodes for arcs must be specified.\n");
      status = FAIL;
      goto TERMINATE;
   }

   if ( narcs > 0 && tonode == NULL )  {
      CPXmsg (errorchan,
              "To-nodes for arcs must be specified.\n");
      status = FAIL;
      goto TERMINATE;
   }

   for (i = 0; i < narcs; i++) {
      if ( fromnode[i] < 0  ||  fromnode[i] >= nnodes ) {
         CPXmsg (errorchan,
                 "From-node %d for arc %d out of range.\n",
                 fromnode[i], i);
         status = FAIL;
         goto TERMINATE;
      }

      if ( tonode[i] < 0  ||  tonode[i] >= nnodes ) {
         CPXmsg (errorchan,
                 "To-node %d for arc %d out of range.\n",
                 tonode[i], i);
         status = FAIL;
         goto TERMINATE;
      }

      if ( fromnode[i] == tonode[i] ) {
         CPXmsg (errorchan,
                 "From-node %d equals to-node for arc %d.\n",
                 tonode[i], i);
         status = FAIL;
         goto TERMINATE;
      }
   }

   if ( low != NULL  &&  up != NULL ) {
      for (i = 0; i < narcs; i++) {
         if ( up[i] < low[i] ) {
            CPXmsg (errorchan,
            "Lower bound (%g) of arc %d exceeds upper bound (%g).\n",
            low[i], i, up[i]);
            status = FAIL;
            goto TERMINATE;
         }
      }
   }

   if ( supply ) {
      status = checknan (env, supply, nnodes, "supply", errorchan,
                         reschan);
      if ( status )  goto TERMINATE;

      minval =  BIGREAL;
      maxval = -BIGREAL;

      for (i = 0; i < nnodes; i++) {
         if ( supply[i] > maxval )  maxval = supply[i];
         if ( supply[i] > minval )  minval = supply[i];
      }

      CPXmsg (reschan, "Minimum supply value %18.10e\n", minval);
      CPXmsg (reschan, "Maximum supply value %18.10e\n", maxval);
   }

   if ( low != NULL ) {
      status = checknan (env, low, narcs, "low", errorchan, reschan);
      if ( status )  goto TERMINATE;

      minval =  BIGREAL;
      maxval = -BIGREAL;

      for ( i = 0; i < narcs; ++i ) {
         if ( low[i] > maxval )  maxval = low[i];
         if ( low[i] > minval )  minval = low[i];
      }

      CPXmsg (reschan, "Minimum lower bound value %18.10e\n", 
              minval);
      CPXmsg (reschan, "Maximum lower bound value %18.10e\n",
              maxval);
   }

   if ( up != NULL ) {
      status = checknan (env, up, narcs, "up", errorchan, reschan);
      if ( status )  goto TERMINATE;

      minval =  BIGREAL;
      maxval = -BIGREAL;

      for (i = 0; i < narcs; i++) {
         if ( up[i] > maxval )  maxval = up[i];
         if ( up[i] > minval )  minval = up[i];
      }

      CPXmsg (reschan, "Minimum upper bound value %18.10e\n", 
              minval);
      CPXmsg (reschan, "Maximum upper bound value %18.10e\n", 
              maxval);
   }

   if ( obj != NULL ) {
      status = checknan (env, obj, narcs, "obj", errorchan, reschan);
      if ( status )  goto TERMINATE;

      minval =  BIGREAL;
      maxval = -BIGREAL;

      for (i = 0; i < narcs; i++) {
         if ( obj[i] > maxval )  maxval = obj[i];
         if ( obj[i] > minval )  minval = obj[i];
      }

      CPXmsg (reschan, "Minimum objective value %18.10e\n", minval);
      CPXmsg (reschan, "Maximum objective value %18.10e\n", maxval);
   }

TERMINATE:

   return status;

} /* END cpxnetcopynet */


static int
checkinit (CPXCENVptr env,
           CPXCLPptr  lp,
           const char *routinename)
{
   int status = SUCCEED;

   CPXCHANNELptr errorchan = NULL;

   if ( env == NULL ) {
      printf("Environment pointer in %s is NULL, cannot proceed.\n",
             routinename);
      status = FAIL;
      goto TERMINATE;
   }
   else if ( lp == NULL ) {
      CPXgetchannels (env, NULL, NULL, &errorchan, NULL);
      CPXmsg (errorchan, "LP pointer in %s is NULL.\n",
              routinename);
      status = FAIL;
   }
   else if ( checkmalloc () ) {
      CPXgetchannels (env, NULL, NULL, &errorchan, NULL);
      CPXmsg (errorchan, "Check malloc at start of %s failed.\n",
              routinename);
      status = FAIL;
   }

TERMINATE:

   return (status);

} /* END checkinit */


static int
checkvaldata (CPXCENVptr   env,
              CPXCLPptr    lp,
              int          cnt,
              int          rows,
              int          cols, 
              const int    *rowind,
              const int    *colind,
              const double *values,
              const char   *rowindname,
              const char   *colindname,
              const char   *valuesname)
{
   int           status = SUCCEED;
   CPXCHANNELptr errorchan = NULL;
   CPXCHANNELptr warnchan  = NULL;
   CPXCHANNELptr reschan   = NULL;
   int           i, maxrows, maxcols, len, errcnt = 0;
   int           *iwork = NULL;

   /* Here, we get the CPLEX standard channels.  Alternatively, you
      you create your own, or set reschan, warnchan, errorchan, to be
      a channel you want to use. */

   CPXgetchannels (env, &reschan, &warnchan, &errorchan, NULL);

   /* Check for invalid values in the indices arrays. */

   maxrows = 0;
   if ( rowind != NULL ) {
      for (i = 0; i < cnt; i++) {
         if ( rowind[i] >= rows ) {
            CPXmsg (errorchan, 
            "Error: entry %s[%d] (%d) invalid for number of rows (%d).\n", 
                    rowindname, i, rowind[i], rows);
            status = FAIL;
            goto TERMINATE;
	 }
         else if ( rowind[i] < 0 ) {
            CPXmsg (errorchan, 
                    "Error: entry %s[%d] has negative value (%d).\n", 
                    rowindname, i, rowind[i]);
            status = FAIL;
            goto TERMINATE;
	 }
         else 
            maxrows = rowind[i] > maxrows ? rowind[i] : maxrows;
      }
   }


   maxcols = 0;
   if ( colind != NULL ) {
      for (i = 0; i < cnt; i++) {
         if ( colind[i] >= cols ) {
            CPXmsg (errorchan, 
            "Error: entry %s[%d] (%d) invalid for number of columns (%d).\n", 
                    colindname, i, colind[i], cols);
            status = FAIL;
            goto TERMINATE;
	 }
         else if ( colind[i] < 0 ) {
            CPXmsg (errorchan, 
                    "Error: entry %s[%d] has negative value (%d).\n", 
                    colindname, i, colind[i]);
            status = FAIL;
            goto TERMINATE;
	 }
         else 
            maxcols = colind[i] > maxcols ? colind[i] : maxcols;
      }
   }

   /* Check for duplicates in the indices arrays. */

   maxrows++;
   maxcols++;
   len   = maxrows > maxcols ? maxrows : maxcols;
   iwork = (int *) calloc((size_t) len, sizeof(int));
   if ( iwork == NULL ) {
      CPXmsg (errorchan, "Work vector malloc failed in checkvaldata.\n");
      status = FAIL;
      goto TERMINATE;
   }

   if ( rowind != NULL ) {
      for (i = 0; i < cnt; i++) {
         iwork[rowind[i]]++;
      }

      for (i = 0; i < cnt; i++){
         if ( iwork[rowind[i]] > 1) {
            CPXmsg (errorchan, "Error: duplicate row entry; %s[%d] = %d.\n",
                    rowindname, i, rowind[i]);
            status = FAIL;
            errcnt++;
            if ( errcnt >= MAXERRCNT ) {
               CPXmsg(errorchan,
                  "Quitting after %d duplicate row entries found.\n",
                  MAXERRCNT);
               goto TERMINATE;
            }
         }
      }
      if ( status )  goto TERMINATE;
   }

   for (i = 0; i < maxcols; i++) {
      iwork[i] = 0;
   }

   if ( colind != NULL ) {
      for (i = 0; i < cnt; i++) {
         iwork[colind[i]]++;
      }

      for (i = 0; i < cnt; i++){
         if ( iwork[colind[i]] > 1) {
            CPXmsg (errorchan, 
                    "Error: duplicate column entry; %s[%d] = %d.\n",
                    colindname, i, colind[i]);
            status = FAIL;
            errcnt++;
            if ( errcnt >= MAXERRCNT ) {
               CPXmsg(errorchan,
                  "Quitting after %d duplicate column entries found.\n",
                  MAXERRCNT);
               goto TERMINATE;
            }
         }
      }
      if ( status )  goto TERMINATE;
   }


   /* Check for NANs in the values array. */

   if ( values != NULL )
      status = checknan (env, values, cnt, valuesname, errorchan, reschan);

TERMINATE:

   if ( iwork )  free ((char *) iwork); 

   return (status);

} /* END checkvaldata */


static int
checkdata (CPXCENVptr   env,
           int          numcols,
           int          numrows,
           int          objsen,
           const double *obj,
           const double *rhs,
           const char   *sense, 
           const int    *matbeg,
           const int    *matcnt,
           const int    *matind, 
           const double *matval,
           const double *lb,
           const double *ub,
           const double *rngval,
           char         **colname,
           char         **rowname)
{
   int           j, status = SUCCEED;
   int           zeroind = FALSE;
   int           nzcnt = 0;
   unsigned      colnamenzcnt = 0, rownamenzcnt = 0;
   double        maxval = 0, minval = BIGREAL;
   CPXCHANNELptr errorchan = NULL;
   CPXCHANNELptr warnchan = NULL;
   CPXCHANNELptr reschan = NULL;

   /* Here, we get the CPLEX standard channels.  Alternatively, you
      you create your own, or set reschan, warnchan, errorchan, to be
      a channel you want to use */

   CPXgetchannels (env, &reschan, &warnchan, &errorchan, NULL);

   CPXmsg (reschan, "Checking problem data:\n");

   status = checksizes (env, numcols, numrows, colname, rowname,
                        errorchan, warnchan, reschan);
   if ( status )  goto TERMINATE;
    
   /*  Compute problem dimensions not immediately available from
       arguments. */

   if ( colname ) {
      for (j = 0; j < numcols; j++) {
         nzcnt        += matcnt[j];
         colnamenzcnt += strlen (colname[j]) + 1;
      }
   }
   else
      for (j = 0; j < numcols; j++) 
         nzcnt        += matcnt[j];

   if ( rowname )   
      for (j = 0; j < numrows; j++) 
         rownamenzcnt += strlen (rowname[j]) + 1;

   CPXmsg (reschan, "Rows                   %6d\n", numrows);
   CPXmsg (reschan, "Columns                %6d\n", numcols);
   CPXmsg (reschan, "Matrix nonzeros        %6d\n", nzcnt);
   if ( colname ) {
      CPXmsg (reschan, "Column name space used %6d\n", colnamenzcnt);
   }
   if ( rowname ) {
      CPXmsg (reschan, "Row    name space used %6d\n", rownamenzcnt);
   }
   CPXflushstdchannels (env);

   if ( (objsen != 1)  &&  (objsen != -1) ) {
      CPXmsg (errorchan,
      "Objective sense (%d) must be 1 (Min) or -1 (Max).\n", objsen);
      status = FAIL;
      goto TERMINATE;
   }

   status = checkrows (env, numcols, numrows, rhs, sense, matbeg, matcnt,
                       rngval, errorchan, warnchan, reschan);
   if ( status )  goto TERMINATE;

   status = checkcols (env, numcols, numrows, obj, matbeg,
                       matcnt, matind, matval, lb, ub, &zeroind,
                       &minval, &maxval, errorchan, warnchan, reschan);
   if ( status )  goto TERMINATE;

   status = checknames (env, numcols, numrows, colname, rowname,
                        errorchan, warnchan, reschan);
   if ( status )  goto TERMINATE;


   CPXmsg (reschan,
           "Minimum absolute nonzero matrix    value %18.10e.\n",
           minval);
   CPXmsg (reschan,
           "Maximum absolute         matrix    value %18.10e.\n",
           maxval);

   if ( zeroind )
      CPXmsg (warnchan, "Warning:  Matrix contains zero entries.\n");

   CPXflushstdchannels (env);

   minval = BIGREAL;
   maxval = 0.0;

   /* Not much real checking can be done in the objective, RHS,
      or bounds.  What we can do is allow the user to notice that
      they may have entered unexpectedly large or small values */

   for (j = 0; j < numcols; j++) {
      if ( obj[j]  &&  XABS(obj[j]) < minval ) {
         minval = XABS(obj[j]);
      }
      if ( XABS(obj[j]) > maxval ) {
         maxval = XABS(obj[j]);
      }
   }

   if ( maxval ) {
      CPXmsg (reschan,
              "Minimum absolute nonzero objective value %18.10e.\n",
              minval);
      CPXmsg (reschan,
              "Maximum absolute         objective value %18.10e.\n",
              maxval);
   }
   else {
      CPXmsg (reschan, "Objective is identically zero.\n");
   }
   CPXflushstdchannels (env);

   for (j = 0; j < numcols; j++) {
      if ( ub[j] < lb[j] ) {
         CPXmsg (errorchan,
         "Lower bound (%g) in column %d exceeds upper bound (%g).\n",
         lb[j], j, ub[j]);
         status = FAIL;
         goto TERMINATE;
      }
   }

   minval =  BIGREAL;
   maxval = -BIGREAL;

   for (j = 0; j < numcols; j++) {
      if ( (ub[j] <  CPX_INFBOUND)  &&  (ub[j] > maxval) ) {
         maxval = ub[j];
      }
      if ( (lb[j] > -CPX_INFBOUND)  &&  (lb[j] < minval) ) {
         minval = lb[j];
      }
   }

   if ( maxval != -BIGREAL ) {
      CPXmsg (reschan, "Maximum finite upper bound: %18.10e.\n",
              maxval);
   }
   if ( minval !=  BIGREAL ) {
      CPXmsg (reschan, "Minimum finite lower bound: %18.10e.\n",
              minval);
   }

#ifdef CHECK_NAME_CONFLICT
   status = checknameconflicts  ();
   if ( status )  goto TERMINATE;
#endif

TERMINATE:

   CPXflushstdchannels (env);

   return (status);

} /* END checkdata */


static int
checksizes (CPXCENVptr    env,
            int           numcols,
            int           numrows,  
            char          **colname,
            char          **rowname,
            CPXCHANNELptr errorchan,
            CPXCHANNELptr warnchan,
            CPXCHANNELptr reschan)
{
   int status = SUCCEED;

   if ( numcols <= 0 ) {
      CPXmsg (errorchan,
              "Number of columns (%d) must be strictly positive.\n",
              numcols);
      status = FAIL;
      goto TERMINATE;
   }

   if ( numrows <= 0 ) {
      CPXmsg (errorchan,
              "Number of rows (%d) must be strictly positive.\n",
              numrows);
      status = FAIL;
      goto TERMINATE;
   }

TERMINATE:

   CPXflushstdchannels (env);

   return (status);

} /* END checksizes */



static int
checkrows (CPXCENVptr    env,
           int           numcols,
           int           numrows,
           const double  *rhs,
           const char    *sense,
           const int     *matbeg,
           const int     *matcnt,
           const double  *rngval,
           CPXCHANNELptr errorchan,
           CPXCHANNELptr warnchan,
           CPXCHANNELptr reschan)
{
   int     i;
   int     rngcnt    = 0;
   int     status = SUCCEED;
   double  minval, maxval;

   /* Check for NULL pointers */

   if ( matbeg == NULL ||
        matcnt == NULL ||
        rhs    == NULL ||
        sense  == NULL   ) {
      CPXmsg (errorchan, "The following arrays are NULL:\n");
      CPXmsg (errorchan, "%s%s%s%s",
              (matbeg == NULL ? "matbeg\n": ""),
              (matcnt == NULL ? "matcnt\n": ""),
              (rhs    == NULL ? "rhs\n": ""),
              (sense  == NULL ? "sense\n": ""));
      status = FAIL;
      goto TERMINATE;
   }

   /* Check for invalid values in the sense array */

   for (i = 0; i < numrows; i++) {
      if ( sense[i] != 'L' &&
           sense[i] != 'G' &&
           sense[i] != 'E' &&
           sense[i] != 'R'   ) {
         CPXmsg (errorchan,
                 "Rows sense must be 'L', 'G', 'E', or 'R':  ");
         CPXmsg (errorchan,
                 "Entry %d is '%c'.\n", i, sense[i]);
         status = FAIL;
         goto TERMINATE;
      }
      else if ( (sense[i] == 'R')  &&  (rngval == NULL) ) {
         CPXmsg (errorchan,
           "Row %d has sense 'R' but range-value vector is NULL.\n",
           i);
         status = FAIL;
         goto TERMINATE;
      }
   }

   if ( rngval ) {
      minval =  BIGREAL;
      maxval = -BIGREAL;
      for (i = 0; i < numrows; i++) {
         if ( sense[i] == 'R' ) {
            rngcnt++;
            if ( rngval[i] > maxval )  maxval = rngval[i];
            if ( rngval[i] < minval )  minval = rngval[i];
         }
      }
      if ( rngcnt ) {

         CPXmsg (reschan,
                 "Number of ranged rows     : %d\n",
                 rngcnt);
         CPXmsg (reschan,
                 "Maximum range value       : %18.10e\n",
                 maxval);
         CPXmsg (reschan,
                 "Minimum range value       : %18.10e\n",
                 minval);
      }
      else {
         CPXmsg (warnchan,
        "Warning:  rngval[] allocated, but no ranged rows exist.\n");
      }

   }

   CPXflushstdchannels (env);

   minval = BIGREAL;
   maxval = 0.0;

   for (i = 0; i < numrows; i++) {
      if ( rhs[i]  &&  XABS(rhs[i]) < minval ) {
         minval = XABS(rhs[i]);
      }
      if ( XABS(rhs[i]) > maxval ) {
         maxval = XABS(rhs[i]);
      }
   }
   if ( maxval ) {
      CPXmsg (reschan,
              "Minimum absolute nonzero RHS       value %18.10e\n",
              minval);
      CPXmsg (reschan,
              "Maximum absolute         RHS       value %18.10e\n",
              maxval);

   }
   else {
      CPXmsg (reschan, "RHS is identically zero.\n");
   }

   CPXflushstdchannels (env);

   if ( rngval != NULL ) {
      if ( rngcnt )  status = checknan (env, rngval, numrows, "rngval", 
                                        errorchan, reschan);
   }
   status = checknan (env, rhs, numrows, "rhs", errorchan, reschan);
   if ( status )  goto TERMINATE;

TERMINATE:

   CPXflushstdchannels (env);

   return (status);

} /* END checkrows */



static int 
checkcols (CPXCENVptr    env,
           int           numcols,
           int           numrows,
           const double  *obj,
           const int     *matbeg,
           const int     *matcnt,
           const int     *matind, 
           const double  *matval,
           const double  *lb,
           const double  *ub, 
           int           *zeroind_p,
           double        *minval_p,
           double        *maxval_p,
           CPXCHANNELptr errorchan,
           CPXCHANNELptr warnchan,
           CPXCHANNELptr reschan)
{
   int j, k, errcnt = 0;
   int *iwork = NULL;
   int status = SUCCEED;

   /* Check for NULL pointers */

   if ( matbeg == NULL ||
        matcnt == NULL ||
        matind == NULL ||
        matval == NULL ||
        obj    == NULL ||
        lb     == NULL ||
        ub     == NULL   ) {
      CPXmsg (errorchan, "The following arrays are NULL:\n");
      CPXmsg (errorchan, "%s%s%s%s%s%s%s",
              (matbeg == NULL ? "matbeg\n": ""),
              (matcnt == NULL ? "matcnt\n": ""),
              (matind == NULL ? "matind\n": ""),
              (matval == NULL ? "matval\n": ""),
              (obj    == NULL ? "obj\n": ""),
              (lb     == NULL ? "lb\n": ""),
              (ub     == NULL ? "ub\n": ""));
      status = FAIL;
      goto TERMINATE;
   }

   /* Verify that components of matbeg are in ascending order */

   for (j = 0; j < numcols - 1; j++) {
      if ( matbeg[j] > matbeg[j+1] ) {
         CPXmsg (errorchan,
                 "Components of matbeg must be ascending, ");
         CPXmsg (errorchan,
                 "but matbeg[%d] (%d) > matbeg[%d] (%d).\n",
                 j, matbeg[j], j+1, matbeg[j+1]);
         status = FAIL;
         goto TERMINATE;
      }
   }

   /* Make sure matbeg has no negative entries. Since we know that
      matbeg is in ascending order when we get here, just look at
      matbeg[0] */

   if (( numcols > 0 ) && ( matbeg[0] < 0 )) {
      CPXmsg (errorchan,
            "Negative entry in matbeg: matbeg[0] = %d\n", matbeg[0]);
      status = FAIL;
      goto TERMINATE;
   }

   /* Check for invalid column counts and overlapping columns */

   iwork = (int *) calloc((size_t) numrows, sizeof(int));
   if ( iwork == NULL ) {
      CPXmsg (errorchan, "Work vector malloc failed in checkcols.\n");
      status = FAIL;
      goto TERMINATE;
   }

   *maxval_p = 0.0;
   *minval_p = BIGREAL;
   for (j = 0; j < numcols; j++) {
      if ( matcnt[j] < 0 ) {
         CPXmsg (errorchan,
                 "Count of entries in column %d is negative (%d).\n",
                 j, matcnt[j]);
         status = FAIL;
         goto TERMINATE;
      }
      if ( matcnt[j] > numrows ) {
         CPXmsg (errorchan,
                 "Count of entries in column %d (%d) > rows (%d).\n",
                 j, matcnt[j], numrows);
         status = FAIL;
         goto TERMINATE;
      }
      if ( (j < numcols-1)                      && 
           (matbeg[j] + matcnt[j] > matbeg[j+1])  ) {
         CPXmsg (errorchan,
                 "End of column %d overlaps start of column %d\n",
                 j, j+1);
         status = FAIL;
         goto TERMINATE;
      }

      /* Compute min and max absolute matrix elements. Also,
         check validity of entries in matind */

      for (k = matbeg[j]; k < matbeg[j] + matcnt[j]; k++) {
         if ( matind[k] < 0 ) {
            CPXmsg (errorchan,
                    "Entry matind[%d] is negative (%d).\n",
                    k, matind[k]);
            status = FAIL;
            goto TERMINATE;
         }
         if ( matind[k] >= numrows ) {
            CPXmsg (errorchan,
              "Entry matind[%d] (%d) invalid for number of rows (%d).\n",
              k, matind[k], numrows);
            status = FAIL;
            goto TERMINATE;
         }
         iwork[matind[k]]++;
         if ( !matval[k] )  *zeroind_p = TRUE;
         if ( matval[k]  &&  XABS(matval[k]) < *minval_p ) {
            *minval_p = XABS(matval[k]);
         }
         if ( XABS(matval[k]) > *maxval_p ) {
            *maxval_p = XABS(matval[k]);
         }
      }

      /* Check for duplicate row indices in a single column */

      for (k = matbeg[j]; k < matbeg[j] + matcnt[j]; k++) {
         if ( iwork[matind[k]] > 1) {
            CPXmsg (errorchan, "Duplicate row entry in matind: ");
            CPXmsg (errorchan, "column %d, row %d.\n", j, matind[k]);
            status = FAIL;
            errcnt++;
            if ( errcnt >= MAXERRCNT ) {
               CPXmsg(errorchan,
                  "Quitting after %d duplicate row entries found.\n",
                  MAXERRCNT);
               status = FAIL;
               goto TERMINATE;
            }
         }
         iwork[matind[k]] = 0;
      }
   }
   if ( status )  goto TERMINATE;

   status = checknan (env, obj, numcols, "obj", errorchan, reschan);
   if ( status )  goto TERMINATE;
   status = checkmatval (env, matbeg, matcnt, matval, numcols, "matval",
                         errorchan, reschan);
   if ( status )  goto TERMINATE;
   status = checknan (env, lb, numcols, "lb", errorchan, reschan);
   if ( status )  goto TERMINATE;
   status = checknan (env, ub, numcols, "ub", errorchan, reschan);
   if ( status )  goto TERMINATE;

TERMINATE:

   CPXflushstdchannels (env);

   if ( iwork )  free ((char *) iwork);

   return (status);

} /* END checkcols */



static int
checknames (CPXCENVptr    env,
            int           numcols,
            int           numrows,
            char          **colname, 
            char          **rowname, 
            CPXCHANNELptr errorchan, 
            CPXCHANNELptr warnchan,
            CPXCHANNELptr reschan)
{
   int   i, j;
   int   warncnt = 0;
   int   status = SUCCEED;

   /* colname */

   if ( colname ) {
      for (j = 0; j < numcols; j++) {
         if ( strlen(colname[j]) > MAXNAMELEN ) {
            warncnt++;
            if ( warncnt <= MAXWARNCNT ) {
               CPXmsg (warnchan,
         "Warning:  Name for column %d (%s) exceeds %d characters.\n",
         j, colname[j], MAXNAMELEN);
               CPXmsg (warnchan,
                "CPLEX may not be able to write LP or MPS files.\n");
            }
         }
      }
      if ( warncnt > MAXWARNCNT ) {
         CPXmsg (warnchan,
            "%d column name length warnings not printed.\n",
                 warncnt - MAXWARNCNT);
      }
      CPXflushstdchannels (env);
   }

   /* rowname */

   warncnt = 0;
   if ( rowname ) {
      for (i = 0; i < numrows; i++) {
         if ( strlen(rowname[i]) > MAXNAMELEN ) {
            warncnt++;
            if ( warncnt <= MAXWARNCNT ) {
               CPXmsg (warnchan,
         "Warning:  Name for row %d (%s) exceeds %d characters.\n",
                       i, rowname[i], MAXNAMELEN);
               CPXmsg (warnchan,
                "CPLEX may not be able to write LP or MPS files.\n");
            }
         }
      }
      if ( warncnt > MAXWARNCNT ) {
         CPXmsg (warnchan,
                 "%d row name length warnings not printed.\n",
                 warncnt - MAXWARNCNT);
      }
      CPXflushstdchannels (env);
   }

   CPXflushstdchannels (env);

   return (status);

} /* END checknames */



static int
checknan (CPXCENVptr    env,
          const double  *dx,
          int           len, 
          const char    *arrayname,
          CPXCHANNELptr errorchan,
          CPXCHANNELptr reschan)
{
   int    i;
   int  status = SUCCEED;

   if ( dx == NULL )  goto TERMINATE;
   CPXmsg (reschan, "Checking %s array for unrepresentable values:", 
           arrayname);
   CPXflushstdchannels (env);

   for (i = 0; i < len; i++) {

      /* Check for unrepresentable values (NaNs, etc.) 
         Note - some machines have a function isnan(x) that could
         be used here, i.e. use  if ( isnan (dx[i] ) )  */ 

      if ( dx[i] != dx[i] ) {
         CPXmsg (errorchan, 
                 "\nArray %s[%d] contains a number ", arrayname, i);
         CPXmsg (errorchan,
                 "not representable in exponential notation.\n");
         status = FAIL;
         goto TERMINATE;
      }
   }
   CPXmsg (reschan, " OK.\n");

TERMINATE:

   return (status);

} /* END checknan */


static int 
checkmatval (CPXCENVptr    env,
             const int     *matbeg,
             const int     *matcnt,
             const double  *matval, 
             int           numcols,
             const char    *arrayname,
             CPXCHANNELptr errorchan, 
             CPXCHANNELptr reschan)
{
   int j, k, status = SUCCEED;

   CPXmsg (reschan, "Checking array %s for unrepresentable values:", 
           arrayname);
   CPXflushstdchannels (env);
   for (j = 0; j < numcols; j++) {
      for (k = matbeg[j]; k < matbeg[j] + matcnt[j]; k++){

      /* Check for unrepresentable values (NaNs, etc.) 
         Note - some machines have a function isnan(x) that could
         be used here, i.e. use  if ( isnan (matval[k] ) )  */ 
         
         if ( matval[k] != matval[k] ) {
            CPXmsg (errorchan, 
                    "\nArray %s[%d] contains a number ", arrayname, k);
            CPXmsg (errorchan,
                    "not representable in exponential notation.\n");
            status = FAIL;
            goto TERMINATE;
	 }
      }
   }   
   
   
   CPXmsg (reschan, " OK.\n");

TERMINATE:

   return (status);

} /* END checkmatval */


/* Check if the memory heap has been corrupted. Users may wish to
   replace this simple, platform independent routine with a more
   specific one of their own. For example, one could replace with
   a routine that explicitly checks the memory heap */

static int
checkmalloc (void)
{
   int  *temp = NULL;
   int  status = SUCCEED;

   temp = (int *) malloc (2*sizeof(int));
   if ( temp == NULL ) {
      status = FAIL;
      goto TERMINATE;
   }
   else                 free ((char *) temp);

   temp = (int *) malloc (200*sizeof(int));
   if ( temp == NULL ) {
      status = FAIL;
      goto TERMINATE;
   }
   else                 free ((char *) temp);

   temp = (int *) malloc (2000*sizeof(int));
   if ( temp == NULL ) {
      status = FAIL;
      goto TERMINATE;
   }
   else                 free ((char *) temp);

   temp = (int *) malloc (20000*sizeof(int));
   if ( temp == NULL ) {
      status = FAIL;
      goto TERMINATE;
   }
   else                 free ((char *) temp);

TERMINATE:

   return (status);

} /* END checkmalloc */








#ifdef CHECK_NAME_CONFLICT

/*  The following routine tests for name conflicts between programmers
    names and those of the standard C library functions called by the
    CPLEX library.  The function calls to the various routines
    are not designed to do anything other than reveal the name
    conflict.  Note: this function may be platform dependent.
    You may need to either modify or comment out parts of it.
    Also, this routine uses Posix compliant features.  You will
    may need to compile with the appropriate option in the compile
    statement (e.g. -D_HPUX_SOURCE for HP systems, -D_POSIX_SOURCE
    for most other systems.  */

/* You may need to change the specific include files for some systems */

#include <sys/stat.h>
#include <errno.h>
#include <fcntl.h>

static int
checknameconflicts (CPXCHANNELptr errorchan, CPXCHANNELptr warnchan,
                    CPXCHANNELptr reschan)
{

   char   buffer [256];
   char   filebuf[L_tmpnam];
   char   *s, *filename;
   int    i, j;
   double t;
   FILE   *fp;
   int    *iptr;
   struct stat stbuf;

   /*  Memory functions */

   iptr = (int *) calloc (2, sizeof (int));
   if ( iptr == NULL || (iptr[0] || iptr[1]) ) {
      CPXmsg (errorchan,
        "Possible name conflict with 'calloc' C library function\n");
      status = FAIL;
   }
   iptr = (int *) realloc (iptr, 3*sizeof (int));
   if ( iptr == NULL ) {
      CPXmsg (errorchan,
       "Possible name conflict with 'realloc' C library function\n");
      status = FAIL;
   }
   free ((char *) iptr);
   iptr = (int *) malloc (2*sizeof (int));
   if ( iptr == NULL ) {
      CPXmsg (errorchan,
        "Possible name conflict with 'malloc' C library function\n");
      status = FAIL;
   }
   free ((char *) iptr);

   /* Integer arithmetic functions */

   i = abs (-3);
   if ( i != 3 ) {
      CPXmsg (errorchan,
           "Possible name conflict with 'abs' C library function\n");
      status = FAIL;
   }
   sprintf (buffer, "3");
   i = atoi (buffer);
   if ( i != 3 ) {
      CPXmsg (errorchan,
          "Possible name conflict with 'atoi' C library function\n");
      status = FAIL;
   }

   /* Double arithmetic functions */

   t = -3.14;
   if ( fabs (t) != 3.14 ) {
      CPXmsg (errorchan,
          "Possible name conflict with 'fabs' C library function\n");
      status = FAIL;
   }
   t = 4.5;
   if ( ceil (t) != 5.0 ) {
      CPXmsg (errorchan,
          "Possible name conflict with 'ceil' C library function\n");
      status = FAIL;
   }
   if ( floor (t) != 4.0 ) {
      CPXmsg (errorchan,
         "Possible name conflict with 'floor' C library function\n");
      status = FAIL;
   }
   t = floor (t);
   if ( sqrt (t) != 2.0 ) {
      CPXmsg (errorchan,
          "Possible name conflict with 'sqrt' C library function\n");
      status = FAIL;
   }
   if ( exp (0.0) != 1.0 ){
      CPXmsg (errorchan,
           "Possible name conflict with 'exp' C library function\n");
      status = FAIL;
   }
   if ( log (1.0) != 0.0 ) {
      CPXmsg (errorchan,
           "Possible name conflict with 'log' C library function\n");
      status = FAIL;
   }
   if ( log10 (1.0) != 0.0 ) {
      CPXmsg (errorchan,
         "Possible name conflict with 'log10' C library function\n");
      status = FAIL;
   }
   t = 2;
   if ( pow (t, t) != 4.0 ) {
      CPXmsg (errorchan,
           "Possible name conflict with 'pow' C library function\n");
      status = FAIL;
   }

   /* String and character functions */

   i = 3;
   j = sprintf (buffer, "i = %d", i);
   if ( j != 5 ) {
      CPXmsg (errorchan,
       "Possible name conflict with 'sprintf' C library function\n");
      status = FAIL;
   }
   j = sscanf ("45", "%d", &i);
   if ( j != 1 || i != 45 ){
      CPXmsg (errorchan,
        "Possible name conflict with 'sscanf' C library function\n");
      status = FAIL;
   }
   s = strncpy (buffer, "testing123", 7);
   if ( s != buffer ) {
      CPXmsg (errorchan,
       "Possible name conflict with 'strncpy' C library function\n");
      status = FAIL;
   }
   s = strcpy (buffer, "testing");
   if ( s != buffer ) {
      CPXmsg (errorchan,
        "Possible name conflict with 'strcpy' C library function\n");
      status = FAIL;
   }
   if ( strlen (buffer) != 7) {
      CPXmsg (errorchan,
        "Possible name conflict with 'strlen' C library function\n");
      status = FAIL;
   }
   s = strcat (buffer, "12");
   if ( s != buffer ) {
      CPXmsg (errorchan,
        "Possible name conflict with 'strcat' C library function\n");
      status = FAIL;
   }
   s = strncat (buffer, "345", 1);
   if ( s != buffer ) {
      CPXmsg (errorchan,
       "Possible name conflict with 'strncat' C library function\n");
      status = FAIL;
   }
   if ( strcmp (buffer, "testing123") != 0 ){
      CPXmsg (errorchan,
        "Possible name conflict with 'strcmp' C library function\n");
      status = FAIL;
   }
   if ( strncmp (buffer, "testing12345", 10) != 0 ){
      CPXmsg (errorchan,
       "Possible name conflict with 'strncmp' C library function\n");
      status = FAIL;
   }
   s = strchr (buffer, '1');
   if ( (*s) != '1' ) {
      CPXmsg (errorchan,
        "Possible name conflict with 'strchr' C library function\n");
      status = FAIL;
   }

   if ( toupper ('a') != 'A' ) {
      CPXmsg (errorchan,
       "Possible name conflict with 'toupper' C library function\n");
      status = FAIL;
   }
   if ( tolower ('A') != 'a' ) {
      CPXmsg (errorchan,
       "Possible name conflict with 'tolower' C library function\n");
      status = FAIL;
   }

   /* File I/O functions */

   filename = tmpnam (filebuf);
   i = stat (filename, &stbuf); /* Shouldn't find the file */
   if ( i != -1 ) {
      CPXmsg (errorchan,
          "Possible name conflict with 'stat' C library function\n");
      status = FAIL;
   }
   if ( errno == 0 ) {
      CPXmsg (errorchan,
         "Possible name conflict with 'errno' C library variable\n");
      status = FAIL;
   }

   fp = fopen (filename, "w");
   if ( fp == NULL ) {
      CPXmsg (errorchan,
         "Possible name conflict with 'fopen' C library function\n");
      status = FAIL;
   }
   else {
      if ( fprintf (fp, "3\n") != 2 ) {
         CPXmsg (errorchan,
       "Possible name conflict with 'fprintf' C library function\n");
         status = FAIL;
      }
      if ( fputs ("hello\n", fp) != 6 ) {
         CPXmsg (errorchan,
         "Possible name conflict with 'fputs' C library function\n");
         status = FAIL;
      }
      sprintf (buffer, "123");
      if ( fwrite (buffer, sizeof (char),
                   strlen (buffer), fp) != 3 ) {
         CPXmsg (errorchan,
        "Possible name conflict with 'fwrite' C library function\n");
         status = FAIL;
      }
      if ( fflush (fp) != 0 ) {
         CPXmsg (errorchan,
        "Possible name conflict with 'fflush' C library function\n");
         status = FAIL;
      }
      if ( fclose (fp) != 0 ) {
         CPXmsg (errorchan,
        "Possible name conflict with 'fclose' C library function\n");
         status = FAIL;
      }
      else if ( (fp = fopen (filename, "r")) != NULL ) {

         /* Read in what was just written to the file */

         if ( fscanf (fp, "%d\n", &i) != 1 ) {
            CPXmsg (errorchan,
        "Possible name conflict with 'fscanf' C library function\n");
            status = FAIL;
         }
         if ( fgets (buffer, 100, fp) != buffer ) {
            CPXmsg (errorchan,
         "Possible name conflict with 'fgets' C library function\n");
            status = FAIL;
         }
         if ( fread (buffer, sizeof (char), 3, fp) != 3 ) {
            CPXmsg (errorchan,
         "Possible name conflict with 'fread' C library function\n");
            status = FAIL;
         }
         fclose (fp);
      }
   }

   j = open (filename, O_RDWR, 0666);
   if ( j == -1 ) {
      CPXmsg (errorchan,
          "Possible name conflict with 'open' C library function\n");
      status = FAIL;
   }
   else {
      i = close (j);
      if ( i == -1 ) {
         CPXmsg (errorchan,
         "Possible name conflict with 'close' C library function\n");
         status = FAIL;
      }
   }
   remove (filename);
   /* Miscellaneous functions.  Note that putenv() may not be
      available under VMS or Macintosh operating systems; comment it
      out if necessary */

   if ( putenv ("CPXTEST=.") != 0 ) {
      CPXmsg (errorchan,
        "Possible name conflict with 'putenv' C library function\n");
      status = FAIL;
   }
   s = getenv ("CPXTEST");
   if ( strcmp (s, ".") ) {
      CPXmsg (errorchan,
        "Possible name conflict with 'getenv' C library function\n");
      status = FAIL;
   }

   srand (1);
   j = rand();
   if ( j < 0 ) {
      CPXmsg (errorchan,
          "Possible name conflict with 'rand' C library function\n");
      status = FAIL;
   }
} /* END checknameconflicts */

#endif /* CHECK_NAME_CONFLICT */

