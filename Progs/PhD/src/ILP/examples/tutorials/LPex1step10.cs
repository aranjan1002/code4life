            double[] x     = cplex.GetValues(var[0]);
            double[] dj    = cplex.GetReducedCosts(var[0]);
            double[] pi    = cplex.GetDuals(rng[0]);
            double[] slack = cplex.GetSlacks(rng[0]);
          
            cplex.Output().WriteLine("Solution status = " 
                                     + cplex.GetStatus());
            cplex.Output().WriteLine("Solution value  = " 
                                     + cplex.ObjValue);
          
            int nvars = x.Length;
            for (int j = 0; j < nvars; ++j) {
               cplex.Output().WriteLine("Variable  :" 
                                         + j 
                                         +" Value = " 
                                         + x[j] 
                                         +" Reduced cost = " 
                                         + dj[j]);
            }
          
            int ncons = slack.Length;
            for (int i = 0; i < ncons; ++i) {
               cplex.Output().WriteLine("Constraint:" 
                                         + i 
                                         +" Slack = " 
                                         + slack[i] 
                                         +" Pi = " 
                                         + pi[i]);
            }
         }
