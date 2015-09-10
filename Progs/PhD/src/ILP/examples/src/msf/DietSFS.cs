// --------------------------------------------------------------------------
// File: DietSFS.cs   
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
using System.Collections.Generic;
using System.Data;
using Microsoft.SolverFoundation.Common;
using Microsoft.SolverFoundation.Services;
using SolverFoundation.Plugin.Cplex;

namespace DietSFS
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

        // Convert DataTable to a bindable collection for SFS Parameter Binding
        public static IEnumerable<Dictionary<string, object>> 
            ConvertDataTableToBindableCollection(DataTable table, 
                                                 string valueField, 
                                                 params string[] keyFields)
        {
            bool isDoubleValueField = 
                (table.Columns[valueField].DataType == typeof(double));
            bool isIntValueField = 
                (table.Columns[valueField].DataType == typeof(int));
            // Add more supported type here... for example 
            // isBoolValueField, isInt16ValueField
            foreach (DataRow row in table.Rows)
            {
                Dictionary<string, object> item = 
                    new Dictionary<string, object>();
                // Cast to Rational with unboxed value to prevent
                // InvalidCaseException in hydrator
                if (isDoubleValueField)
                    item[valueField] = 
                        (Rational)(UnBox<double>(row[valueField]));
                else if (isIntValueField)
                    item[valueField] = 
                        (Rational)(UnBox<int>(row[valueField]));
                // Add more supported type here...
                else
                    item[valueField] = (Rational)row[valueField];
                foreach (string keyField in keyFields)
                {
                    item[keyField] = row[keyField];
                }
                yield return item;
            }
        }
        private static T UnBox<T>(object obj)
        {
            return (T)obj;
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
                SolverContext context = SolverContext.GetContext();
                Model model = context.CreateModel();

                Decision buy = null;
                Set sFoods = new Set(Domain.Real, "foods");
                Set sNutrs = new Set(Domain.Real, "nutrs");

                if (IsIP)
                {
                    buy = new Decision(Domain.Integer, "buy", sFoods);
                }
                else
                {
                    buy = new Decision(Domain.Real, "buy", sFoods);
                }

                Parameter foodcost = 
                    new Parameter(Domain.Real, "foodcost", sFoods);
                DataSet ds = new DataSet();
                DataTable cost = ds.Tables.Add("foodcost");
                cost.Columns.Add("Index", typeof(int));
                cost.Columns.Add("Cost", typeof(double));
                for (int i = 0; i < nFoods; ++i)
                {
                    cost.Rows.Add(i, data.foodCost[i]);
                }

                foodcost.SetBinding
                (ConvertDataTableToBindableCollection(ds.Tables["foodcost"],
                                                      "Cost", "Index"),
                                                      "Cost", "Index");

                Parameter foodmin = 
                    new Parameter(Domain.Real, "foodmin", sFoods);
                DataTable fmin = ds.Tables.Add("foodmin");
                fmin.Columns.Add("Index", typeof(int));
                fmin.Columns.Add("Min", typeof(double));
                for (int i = 0; i < nFoods; ++i)
                {
                    fmin.Rows.Add(i, data.foodMin[i]);
                }

                foodmin.SetBinding
                    (ConvertDataTableToBindableCollection(ds.Tables["foodmin"],
                                                          "Min", "Index"),
                                                          "Min", "Index");

                Parameter foodmax = new Parameter(Domain.Real, "foodmax", sFoods);
                DataTable fmax = ds.Tables.Add("foodmax");
                fmax.Columns.Add("Index", typeof(int));
                fmax.Columns.Add("Max", typeof(double));
                for (int i = 0; i < nFoods; ++i)
                {
                    fmax.Rows.Add(i, data.foodMax[i]);
                }

                foodmax.SetBinding
                    (ConvertDataTableToBindableCollection(ds.Tables["foodmax"],
                                                          "Max", "Index"),
                                                          "Max", "Index");

                Parameter nutrmin = 
                    new Parameter(Domain.Real, "nutrmin", sNutrs);
                DataTable nmin = ds.Tables.Add("nutrmin");
                nmin.Columns.Add("Index", typeof(int));
                nmin.Columns.Add("Min", typeof(double));
                for (int i = 0; i < nNutrs; ++i)
                {
                    nmin.Rows.Add(i, data.nutrMin[i]);
                }

                nutrmin.SetBinding
                    (ConvertDataTableToBindableCollection(ds.Tables["nutrmin"],
                                                          "Min", "Index"),
                                                          "Min", "Index");

                Parameter nutrmax = new Parameter(Domain.Real, "nutrmax", sNutrs);
                DataTable nmax = ds.Tables.Add("nutrmax");
                nmax.Columns.Add("Index", typeof(int));
                nmax.Columns.Add("Max", typeof(double));
                for (int i = 0; i < nNutrs; ++i)
                {
                    nmax.Rows.Add(i, data.nutrMax[i]);
                }

                nutrmax.SetBinding
                    (ConvertDataTableToBindableCollection(ds.Tables["nutrmax"], 
                                                          "Max", "Index"),
                                                          "Max", "Index");

                Parameter nutrperfood = 
                    new Parameter(Domain.Real, "nutrperfood", sNutrs, sFoods);
                DataTable npf = ds.Tables.Add("nutrperfood");
                npf.Columns.Add("Nutr", typeof(int));
                npf.Columns.Add("Food", typeof(int));
                npf.Columns.Add("Value", typeof(double));

                for (int i = 0; i < nNutrs; ++i)
                {
                    for (int j = 0; j < nFoods; ++j)
                    {
                        npf.Rows.Add(i, j, data.nutrPerFood[i][j]);
                    }
                }
                nutrperfood.SetBinding
                (ConvertDataTableToBindableCollection(ds.Tables["nutrperfood"],
                                                      "Value", "Nutr", "Food"),
                                                      "Value", "Nutr", "Food");

                // Add all Parameters to model
                model.AddParameters(foodmin, foodmax, foodcost, 
                                    nutrmin, nutrmax, nutrperfood);

                model.AddDecision(buy);
                Goal goal = model.AddGoal("Goal", 
                                          GoalKind.Minimize, 
                                          Model.Sum(
                                            Model.ForEach(
                                                sFoods, 
                                                f => buy[f] * foodcost[f])));

                // Add Constraints
                model.AddConstraint("food_restriction", 
                    Model.ForEach(sFoods, 
                        f => foodmin[f] <= buy[f] <= foodmax[f]));

                model.AddConstraint
                ("nutrition_restriction",
                    Model.ForEach(sNutrs, n =>
                    nutrmin[n] <= Model.Sum(
                                                Model.ForEach
                                                (
                                                    sFoods,
                                                    f => buy[f] * nutrperfood[n, f]
                                                ) 
                                            )
                    <= nutrmax[n]
                    )
                );


                // Solve Model
                CplexDirective cplex = new CplexDirective();
                cplex.OutputFunction = Console.Write;
                Solution solution = context.Solve(cplex);

                // Display Solution
                Report r = solution.GetReport();
                Console.WriteLine(r.ToString());
                context.ClearModel();
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
            System.Console.WriteLine("usage: DietSFS [options] <data file>");
            System.Console.WriteLine("         -i  use integer variables");
            System.Console.WriteLine(" ");
        }
    }
}

/*  Sample output

Diet cost   = 14.8557
  Buy 0 = 4.38525
  Buy 1 = 0
  Buy 2 = 0
  Buy 3 = 0
  Buy 4 = 0
  Buy 5 = 6.14754
  Buy 6 = 0
  Buy 7 = 3.42213
  Buy 8 = 0
*/
