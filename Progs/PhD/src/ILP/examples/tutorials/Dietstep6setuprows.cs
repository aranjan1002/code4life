   internal static void BuildModelByRow(IModeler    model,
                                        Data        data,
                                        INumVar[]   Buy,
                                        NumVarType  type) {
      int nFoods = data.nFoods;
      int nNutrs = data.nNutrs;
