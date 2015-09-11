﻿// --------------------------------------------------------------------------
// File: LPex1SFS.cs
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
// LPex1Solver.cs - Entering and optimizing an LP problem
//
//The LP problem solved in this example is
//  Maximize  x1 + 2 x2 + 3 x3
//  Subject to
//     - x1 +   x2 + x3 <= 20
//       x1 - 3 x2 + x3 <= 30
//  Bounds
//       0 <= x1 <= 40
//       0 <= x2
//       0 <= x3
using System;
using Microsoft.SolverFoundation.Common;
using Microsoft.SolverFoundation.Services;
using SolverFoundation.Plugin.Cplex;

namespace LPex1SFS
{
    class LPex1SFS
    {
        static void Main(string[] args)
        {
            try
            {
                SolverContext context = SolverContext.GetContext();
                Model model = context.CreateModel();

                Decision x1 = new Decision(Domain.RealRange(0, 40), "x1");
                Decision x2 = new Decision
                    (Domain.RealRange(0, Rational.PositiveInfinity), "x2");
                Decision x3 = new Decision
                    (Domain.RealRange(0, Rational.PositiveInfinity), "x3");

                model.AddDecisions(x1, x2, x3);

                model.AddConstraint("Row1", -x1 + x2 + x3 <= 20);
                model.AddConstraint("Row2", x1 - 3 * x2 + x3 <= 30);

                Goal goal = model.AddGoal("Goal",
                                          GoalKind.Maximize,
                                          x1 + 2 * x2 + 3 * x3);

                // Turn on CPLEX log
                CplexDirective cplexDirective = new CplexDirective();
                cplexDirective.OutputFunction = Console.Write;

                Solution solution = context.Solve(cplexDirective);
                Report report = solution.GetReport();
                Console.WriteLine("x: {0}, {1}, {2}", x1, x2, x3);
                Console.Write("{0}", report);
                context.ClearModel();
            }
            catch (Exception ex)
            {
                Console.WriteLine("MSF Exception '" + ex);
            }

            Console.WriteLine("Press <RETURN> to continue ...");
            Console.Read();
        }
    }
}