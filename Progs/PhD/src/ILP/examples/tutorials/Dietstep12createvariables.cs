      for (int j = 0; j < nFoods; j++) {

         Column col = model.Column(cost, data.foodCost[j]);

         for (int i = 0; i < nNutrs; i++) {
            col = col.And(model.Column(constraint[i], 
                                       data.nutrPerFood[i][j]));
         }

         Buy[j] = model.NumVar(col, data.foodMin[j], data.foodMax[j], type);

      }
   }
