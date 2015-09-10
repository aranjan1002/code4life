   internal static void PopulateByNonzero(IMPModeler model,
                                 INumVar[][] var,
                                 IRange[][] rng) {
      double[]    lb = {0.0, 0.0, 0.0};
      double[]    ub = {40.0, System.Double.MaxValue, System.Double.MaxValue};
      INumVar[] x  = model.NumVarArray(3, lb, ub);
      var[0] = x;
    
      double[] objvals = {1.0, 2.0, 3.0};
      model.Add(model.Maximize(model.ScalProd(x, objvals)));
    
      rng[0] = new IRange[2];
      rng[0][0] = model.AddRange(-System.Double.MaxValue, 20.0);
      rng[0][1] = model.AddRange(-System.Double.MaxValue, 30.0);
    
      rng[0][0].Expr = model.Sum(model.Prod(-1.0, x[0]),
                                 model.Prod( 1.0, x[1]),
                                 model.Prod( 1.0, x[2]));
      rng[0][1].Expr = model.Sum(model.Prod( 1.0, x[0]),
                                 model.Prod(-3.0, x[1]),
                                 model.Prod( 1.0, x[2]));
   }
