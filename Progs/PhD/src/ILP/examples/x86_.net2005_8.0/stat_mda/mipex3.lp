\Problem name: ilog.cplex

Maximize
 obj: x0 + 2 x1 + 3 x2 + x3
Subject To
 rng0: - x0 + x1 + x2 + 10 x3 <= 20
 rng1: x0 - 3 x1 + x2 <= 30
 rng2: x1 - 3.5 x3  = 0
Bounds
 0 <= x0 <= 40
      x1 >= 0
      x2 >= 0
 2 <= x3 <= 3
Generals
 x1  x2  x3 
SOS
 IloS0: S1 ::  x3 : 18  x2 : 25
End
