            System.Console.WriteLine();
            System.Console.WriteLine("Solution status = " 
                                     + cplex.GetStatus());
            System.Console.WriteLine();
            System.Console.WriteLine(" cost = " + cplex.ObjValue);
            for (int i = 0; i < nFoods; i++) {
               System.Console.WriteLine(" Buy" 
                                        + i 
                                        + " = " 
                                        + cplex.GetValue(Buy[i]));
            }
            System.Console.WriteLine();
         }
