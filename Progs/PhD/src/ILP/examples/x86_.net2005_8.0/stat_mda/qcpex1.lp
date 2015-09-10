\Problem name: ilog.cplex

Maximize
 obj: IloX0 + 2 IloX1 + 3 IloX2 + [ - 33 IloX0 ^2 + 12 IloX0 * IloX1
      - 22 IloX1 ^2 + 23 IloX1 * IloX2 - 11 IloX2 ^2 ] / 2
Subject To
 IloC0: - IloX0 + IloX1 + IloX2 <= 20
 IloC1: IloX0 - 3 IloX1 + IloX2 <= 30
 IloC0: [ IloX0 ^2 + IloX1 ^2 + IloX2 ^2 ] <= 1
Bounds
 0 <= IloX0 <= 40
End
