   internal static void PopulateByColumn(IMPModeler model,
                                INumVar[][] var,
                                IRange[][] rng) {
      IObjective obj = model.AddMaximize();
    
      rng[0] = new IRange[2];
      rng[0][0] = model.AddRange(-System.Double.MaxValue, 20.0);
      rng[0][1] = model.AddRange(-System.Double.MaxValue, 30.0);
    
      IRange r0 = rng[0][0];
      IRange r1 = rng[0][1];
    
      var[0] = new INumVar[3];
      var[0][0] = model.NumVar(model.Column(obj,  1.0).And(
                               model.Column(r0,  -1.0).And(
                               model.Column(r1,   1.0))),
                               0.0, 40.0);
      var[0][1] = model.NumVar(model.Column(obj,  2.0).And(
                               model.Column(r0,   1.0).And(
                               model.Column(r1,  -3.0))),
                               0.0, System.Double.MaxValue);
      var[0][2] = model.NumVar(model.Column(obj,  3.0).And(
                               model.Column(r0,   1.0).And(
                               model.Column(r1,   1.0))),
                               0.0, System.Double.MaxValue);
   }
