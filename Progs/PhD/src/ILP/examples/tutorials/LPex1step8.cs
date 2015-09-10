         switch ( args[0].ToCharArray()[1] ) {
         case 'r': PopulateByRow(cplex, var, rng);
                   break;
         case 'c': PopulateByColumn(cplex, var, rng);
                   break;
         case 'n': PopulateByNonzero(cplex, var, rng);
                   break;
         default:  Usage();
                   return;
         }
