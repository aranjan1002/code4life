// --------------------------------------------------------------------------
// File: DietMSF.cs   
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
// A dietary model.
//
// Input data:
// foodMin[j]          minimum amount of food j to use
// foodMax[j]          maximum amount of food j to use 
// foodCost[j]         cost for one unit of food j
// nutrMin[i]          minimum amount of nutrient i
// nutrMax[i]          maximum amount of nutrient i
// nutrPerFood[i][j]   nutrition amount of nutrient i in food j
//
// Modeling variables:
// Buy[j]          amount of food j to purchase
//
// Objective:
// minimize sum(j) Buy[j] * foodCost[j]
//
// Constraints:
// forall foods i: nutrMin[i] <= sum(j) Buy[j] * nutrPer[i][j] <= nutrMax[j]
//
using System;
using SolverFoundation.Plugin.Cplex;

namespace DietSolver
{
    class DietSFS
    {
        internal class Data
        {
            internal int nFoods;
            internal int nNutrs;
            internal double[] foodCost;
            internal double[] foodMin;
            internal double[] foodMax;
            internal double[] nutrMin;
            internal double[] nutrMax;
            internal double[][] nutrPerFood;

            internal Data(string filename)
            {
                InputDataReader reader = new InputDataReader(filename);

                foodCost = reader.ReadDoubleArray();
                foodMin = reader.ReadDoubleArray();
                foodMax = reader.ReadDoubleArray();
                nutrMin = reader.ReadDoubleArray();
                nutrMax = reader.ReadDoubleArray();
                nutrPerFood = reader.ReadDoubleArrayArray();

                nFoods = foodMax.Length;
                nNutrs = nutrMax.Length;

                if (nFoods != foodMin.Length ||
                     nFoods != foodMax.Length)
                    throw new Exception("inconsistent data in file " + 
                                        filename);
                if (nNutrs != nutrMin.Length ||
                     nNutrs != nutrPerFood.Length)
                    throw new Exception("inconsistent data in file " + 
                                        filename);
                for (int i = 0; i < nNutrs; ++i)
                {
                    if (nutrPerFood[i].Length != nFoods)
                        throw new Exception("inconsistent data in file " + 
                                            filename);
                }
            }
        }

        static void Main(string[] args)
        {
            try
            {
                string filename = 
                    "..\\..\\..\\examples\\data\\diet.dat";

                bool IsIP = false;

                for (int i = 0; i < args.Length; i++)
                {
                    if (args[i].ToCharArray()[0] == '-')
                    {
                        switch (args[i].ToCharArray()[1])
                        {
                            case 'i':
                                IsIP = true;
                                break;
                            default:
                                Usage();
                                return;
                        }
                    }
                    else
                    {
                        filename = args[i];
                        break;
                    }
                }

                // Read the data
                Data data = new Data(filename);
                int nFoods = data.nFoods;
                int nNutrs = data.nNutrs;

                // Build model
                int goal;
                CplexSolver cplex = new CplexSolver();

                cplex.AddRow("goal", out goal);
                cplex.AddGoal(goal, 1, true);

                // The variables are the amount of each food to purchase
                for (int i = 0; i < nFoods; ++i)
                {
                    int vid;
                    cplex.AddVariable("buy" + i.ToString(), out vid);
                    cplex.SetBounds(vid, data.foodMin[i], data.foodMax[i]);

                    // Require foods to be integral quantities
                    if (IsIP)
                    {
                        cplex.SetIntegrality(vid, true);
                    }
                }

                for (int i = 0; i < nNutrs; i++)
                {
                    int rid;
                    cplex.AddRow("nutrs" + i.ToString(), out rid);
                    for (int j = 0; j < nFoods; ++j)
                    {
                        cplex.SetCoefficient(rid, 
                                             j + 1, 
                                             data.nutrPerFood[i][j]);
                    }
                    cplex.SetBounds(rid, data.nutrMin[i], data.nutrMax[i]);
                }

                for (int i = 0; i < nFoods; ++i)
                {
                    cplex.SetCoefficient(goal, i + 1, data.foodCost[i]);
                }

                // Solve model
                CplexParams p = new CplexParams();
                p.OutputFunction = Console.Write;
                cplex.Solve(p);

                // Display solution
                System.Console.WriteLine();
                System.Console.WriteLine(" Diet cost = " +
                    cplex.GetSolutionValue(goal).ToDouble());
                System.Console.WriteLine();
                for (int i = 0; i < nFoods; i++)
                {
                    System.Console.WriteLine("  Buy Food" + i + 
                        " = " + cplex.GetValue(i + 1).ToDouble());
                }
                System.Console.WriteLine();
                cplex.Shutdown();
            }
            catch (Exception ex)
            {
                System.Console.WriteLine("MSF Error: " + ex);
            }

            Console.WriteLine("Press <RETURN> to continue ...");
            Console.Read();
        }


        internal static void Usage()
        {
            System.Console.WriteLine(" ");
            System.Console.WriteLine("usage: DietMSF [options] <data file>");
            System.Console.WriteLine("         -i  use integer variables");
            System.Console.WriteLine(" ");
        }
    }
}

/*  Sample output

Diet cost   = 14.8557
  Buy Food0 = 4.38525
  Buy Food1 = 0
  Buy Food2 = 0
  Buy Food3 = 0
  Buy Food4 = 0
  Buy Food5 = 6.14754
  Buy Food6 = 0
  Buy Food7 = 3.42213
  Buy Food8 = 0
*/
