      for (int j = 0; j < nFoods; j++) {
         Buy[j] = model.NumVar(data.foodMin[j], data.foodMax[j], type);
      }
