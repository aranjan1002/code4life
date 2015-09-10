// --------------------------------------------------------------------------
// File: MIPex1MSF.cs
// Version 12.2
// --------------------------------------------------------------------------
// Licensed Materials - Property of IBM
// 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
// Copyright IBM Corporation 2009, 2010. All Rights Reserved.
//
// US Government Users Restricted Rights - Use, duplication or
// disclosure restricted by GSA ADP Schedule Contract with
// IBM Corp.
// --------------------------------------------------------------------------
//
// MIPex1MSF.cs - Entering and optimizing a MIP problem
//
// The MIP problem solved in this example is
//   Maximize  x1 + 2 x2 + 3 x3 + x4
//   Subject to
//      - x1 +   x2 + x3 + 10 x4 <= 20
//        x1 - 3 x2 + x3         <= 30
//               x2      - 3.5x4  = 0
//   Bounds
//        0 <= x1 <= 40
//        0 <= x2
//        0 <= x3
//        2 <= x4 <= 3
//   Integers
//       x4

using System;
using Microsoft.SolverFoundation.Common;
using SolverFoundation.Plugin.Cplex;

namespace MIPex1MSF
{
    class MIPex1Solver
    {
        static void Main(string[] args)
        {
            try
            {
                int goal;
                int x1, x2, x3, x4;
                int row1, row2, row3;

                CplexSolver cplex = new CplexSolver();
                cplex.AddRow("goal", out goal);

                cplex.AddVariable("x1", out x1);
                cplex.SetBounds(x1, 0, 40);

                cplex.AddVariable("x2", out x2);
                cplex.SetBounds(x2, 0, Rational.PositiveInfinity);

                cplex.AddVariable("x3", out x3);
                cplex.SetBounds(x3, 0, Rational.PositiveInfinity);

                cplex.AddVariable("x4", out x4);
                cplex.SetBounds(x4, 2, 3);
                cplex.SetIntegrality(x4, true);

                cplex.AddRow("row1", out row1);
                cplex.AddRow("row2", out row2);
                cplex.AddRow("row3", out row3);

                cplex.SetCoefficient(row1, x1, -1.0);
                cplex.SetCoefficient(row1, x2, 1.0);
                cplex.SetCoefficient(row1, x3, 1.0);
                cplex.SetCoefficient(row1, x4, 10.0);
                cplex.SetBounds(row1, Rational.NegativeInfinity, 20.0);

                cplex.SetCoefficient(row2, x1, 1.0);
                cplex.SetCoefficient(row2, x2, -3.0);
                cplex.SetCoefficient(row2, x3, 1.0);
                cplex.SetBounds(row2, Rational.NegativeInfinity, 30.0);

                cplex.SetCoefficient(row3, x2, 1.0);
                cplex.SetCoefficient(row3, x4, -3.5);
                cplex.SetBounds(row3, 0, 0);

                cplex.AddGoal(goal, 1, false);
                cplex.SetCoefficient(goal, x1, 1.0);
                cplex.SetCoefficient(goal, x2, 2.0);
                cplex.SetCoefficient(goal, x3, 3.0);
                cplex.SetCoefficient(goal, x4, 1.0);

                // Turn on CPLEX log
                CplexParams p = new CplexParams();
                p.OutputFunction = Console.Write;

                cplex.Solve(p);

                Console.WriteLine();
                Console.WriteLine("Solution value  = " + 
                    cplex.GetSolutionValue(0));
                Console.WriteLine();

                foreach (int j in cplex.VariableIndices)
                {
                    Console.WriteLine("x" + j + ": Value = " + 
                        cplex.GetValue(j));
                }

                foreach (int i in cplex.RowIndices)
                {
                    Console.WriteLine("row activity " + i + ": Value = " 
                        + cplex.GetValue(i));
                }
            }
            catch ( Exception ex )
            {
                Console.WriteLine("Exception '" + ex);
            }

            Console.WriteLine("Press <RETURN> to continue ...");
            Console.Read();
        }
    }
}
