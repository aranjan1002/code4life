      for (int i = 0; i < nNutrs; i++) {
         model.AddRange(data.nutrMin[i],
                        model.ScalProd(data.nutrPerFood[i], Buy),
                        data.nutrMax[i]);
      }
   }
