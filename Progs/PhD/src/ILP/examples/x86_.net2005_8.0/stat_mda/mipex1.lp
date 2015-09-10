\Problem name: ilog.cplex

Maximize
 obj: IloX0 + 2 IloX1 + 3 IloX2 + IloX3
Subject To
 IloC0: - IloX0 + IloX1 + IloX2 + 10 IloX3 <= 20
 IloC1: IloX0 - 3 IloX1 + IloX2 <= 30
 IloC2: IloX1 - 3.5 IloX3  = 0
Bounds
 0 <= IloX0 <= 40
 2 <= IloX3 <= 3
Generals
 IloX3 
End
