   internal static void PopulateByRow(IMPModeler model,
                             INumVar[][] var,
                             IRange[][] rng) {
      double[]    lb = {0.0, 0.0, 0.0};
      double[]    ub = {40.0, System.Double.MaxValue, System.Double.MaxValue};
      INumVar[] x  = model.NumVarArray(3, lb, ub);
      var[0] = x;
    
      double[] objvals = {1.0, 2.0, 3.0};
      model.AddMaximize(model.ScalProd(x, objvals));
    
      rng[0] = new IRange[2];
      rng[0][0] = model.AddLe(model.Sum(model.Prod(-1.0, x[0]),
                                        model.Prod( 1.0, x[1]),
                                        model.Prod( 1.0, x[2])), 20.0);
      rng[0][1] = model.AddLe(model.Sum(model.Prod( 1.0, x[0]),
                                        model.Prod(-3.0, x[1]),
                                        model.Prod( 1.0, x[2])), 30.0);
   }
