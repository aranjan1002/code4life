      IObjective cost       = model.AddMinimize();
      IRange[]   constraint = new IRange[nNutrs];
    
      for (int i = 0; i < nNutrs; i++) {
         constraint[i] = model.AddRange(data.nutrMin[i], data.nutrMax[i]);
      }
