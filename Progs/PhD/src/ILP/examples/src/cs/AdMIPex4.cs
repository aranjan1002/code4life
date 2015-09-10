// --------------------------------------------------------------------------
// File: AdMIPex4.cs
// Version 12.2  
// --------------------------------------------------------------------------
// Licensed Materials - Property of IBM
// 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
// Copyright IBM Corporation 2003, 2010. All Rights Reserved.
//
// US Government Users Restricted Rights - Use, duplication or
// disclosure restricted by GSA ADP Schedule Contract with
// IBM Corp.
// --------------------------------------------------------------------------
//
// AdMIPex4.cs -- Solving noswot by adding cuts
//
// Examples AdMIPex4.cs and AdMIPex5.cs both solve the MIPLIB
// 3.0 model noswot.mps by adding user cuts.  AdMIPex4.cs adds
// these cuts to the cut table before the branch-and-cut process
// begins, while AdMIPex5.cs adds them through the user cut callback
// during the branch-and-cut process. 

using ILOG.Concert;
using ILOG.CPLEX;
using System.Collections;


public class AdMIPex4 {
   public static IRange[] MakeCuts(IModeler m, ILPMatrix lp) {
      INumVar x11 = null, x12 = null, x13 = null, x14 = null, x15 = null;
      INumVar x21 = null, x22 = null, x23 = null, x24 = null, x25 = null;
      INumVar x31 = null, x32 = null, x33 = null, x34 = null, x35 = null;
      INumVar x41 = null, x42 = null, x43 = null, x44 = null, x45 = null;
      INumVar x51 = null, x52 = null, x53 = null, x54 = null, x55 = null;
      INumVar w11 = null, w12 = null, w13 = null, w14 = null, w15 = null;
      INumVar w21 = null, w22 = null, w23 = null, w24 = null, w25 = null;
      INumVar w31 = null, w32 = null, w33 = null, w34 = null, w35 = null;
      INumVar w41 = null, w42 = null, w43 = null, w44 = null, w45 = null;
      INumVar w51 = null, w52 = null, w53 = null, w54 = null, w55 = null;

      INumVar[] vars = lp.NumVars;
      int       num  = vars.Length;

      for (int i = 0; i < num; ++i) {
         if      ( vars[i].Name.Equals("X11") ) x11 = vars[i];
         else if ( vars[i].Name.Equals("X12") ) x12 = vars[i];
         else if ( vars[i].Name.Equals("X13") ) x13 = vars[i];
         else if ( vars[i].Name.Equals("X14") ) x14 = vars[i];
         else if ( vars[i].Name.Equals("X15") ) x15 = vars[i];
         else if ( vars[i].Name.Equals("X21") ) x21 = vars[i];
         else if ( vars[i].Name.Equals("X22") ) x22 = vars[i];
         else if ( vars[i].Name.Equals("X23") ) x23 = vars[i];
         else if ( vars[i].Name.Equals("X24") ) x24 = vars[i];
         else if ( vars[i].Name.Equals("X25") ) x25 = vars[i];
         else if ( vars[i].Name.Equals("X31") ) x31 = vars[i];
         else if ( vars[i].Name.Equals("X32") ) x32 = vars[i];
         else if ( vars[i].Name.Equals("X33") ) x33 = vars[i];
         else if ( vars[i].Name.Equals("X34") ) x34 = vars[i];
         else if ( vars[i].Name.Equals("X35") ) x35 = vars[i];
         else if ( vars[i].Name.Equals("X41") ) x41 = vars[i];
         else if ( vars[i].Name.Equals("X42") ) x42 = vars[i];
         else if ( vars[i].Name.Equals("X43") ) x43 = vars[i];
         else if ( vars[i].Name.Equals("X44") ) x44 = vars[i];
         else if ( vars[i].Name.Equals("X45") ) x45 = vars[i];
         else if ( vars[i].Name.Equals("X51") ) x51 = vars[i];
         else if ( vars[i].Name.Equals("X52") ) x52 = vars[i];
         else if ( vars[i].Name.Equals("X53") ) x53 = vars[i];
         else if ( vars[i].Name.Equals("X54") ) x54 = vars[i];
         else if ( vars[i].Name.Equals("X55") ) x55 = vars[i];
         else if ( vars[i].Name.Equals("W11") ) w11 = vars[i];
         else if ( vars[i].Name.Equals("W12") ) w12 = vars[i];
         else if ( vars[i].Name.Equals("W13") ) w13 = vars[i];
         else if ( vars[i].Name.Equals("W14") ) w14 = vars[i];
         else if ( vars[i].Name.Equals("W15") ) w15 = vars[i];
         else if ( vars[i].Name.Equals("W21") ) w21 = vars[i];
         else if ( vars[i].Name.Equals("W22") ) w22 = vars[i];
         else if ( vars[i].Name.Equals("W23") ) w23 = vars[i];
         else if ( vars[i].Name.Equals("W24") ) w24 = vars[i];
         else if ( vars[i].Name.Equals("W25") ) w25 = vars[i];
         else if ( vars[i].Name.Equals("W31") ) w31 = vars[i];
         else if ( vars[i].Name.Equals("W32") ) w32 = vars[i];
         else if ( vars[i].Name.Equals("W33") ) w33 = vars[i];
         else if ( vars[i].Name.Equals("W34") ) w34 = vars[i];
         else if ( vars[i].Name.Equals("W35") ) w35 = vars[i];
         else if ( vars[i].Name.Equals("W41") ) w41 = vars[i];
         else if ( vars[i].Name.Equals("W42") ) w42 = vars[i];
         else if ( vars[i].Name.Equals("W43") ) w43 = vars[i];
         else if ( vars[i].Name.Equals("W44") ) w44 = vars[i];
         else if ( vars[i].Name.Equals("W45") ) w45 = vars[i];
         else if ( vars[i].Name.Equals("W51") ) w51 = vars[i];
         else if ( vars[i].Name.Equals("W52") ) w52 = vars[i];
         else if ( vars[i].Name.Equals("W53") ) w53 = vars[i];
         else if ( vars[i].Name.Equals("W54") ) w54 = vars[i];
         else if ( vars[i].Name.Equals("W55") ) w55 = vars[i];
      }

      IRange[] cut = new IRange[8];

      cut[0] = m.Le(m.Diff(x21, x22), 0.0);
      cut[1] = m.Le(m.Diff(x22, x23), 0.0);
      cut[2] = m.Le(m.Diff(x23, x24), 0.0);
      cut[3] = m.Le(m.Sum(m.Sum(m.Prod(2.08, x11),
                                m.Prod(2.98, x21),
                                m.Prod(3.47, x31),
                                m.Prod(2.24, x41),
                                m.Prod(2.08, x51)),
                          m.Sum(m.Prod(0.25, w11),
                                m.Prod(0.25, w21),
                                m.Prod(0.25, w31),
                                m.Prod(0.25, w41),
                                m.Prod(0.25, w51))), 20.25);
      cut[4] = m.Le(m.Sum(m.Sum(m.Prod(2.08, x12),
                                m.Prod(2.98, x22),
                                m.Prod(3.47, x32),
                                m.Prod(2.24, x42),
                                m.Prod(2.08, x52)),
                          m.Sum(m.Prod(0.25, w12),
                                m.Prod(0.25, w22),
                                m.Prod(0.25, w32),
                                m.Prod(0.25, w42),
                                m.Prod(0.25, w52))), 20.25);
      cut[5] = m.Le(m.Sum(m.Sum(m.Prod(2.08, x13),
                                m.Prod(2.98, x23),
                                m.Prod(3.47, x33),
                                m.Prod(2.24, x43),
                                m.Prod(2.08, x53)),
                          m.Sum(m.Prod(0.25, w13),
                                m.Prod(0.25, w23),
                                m.Prod(0.25, w33),
                                m.Prod(0.25, w43),
                                m.Prod(0.25, w53))), 20.25);
      cut[6] = m.Le(m.Sum(m.Sum(m.Prod(2.08, x14),
                                m.Prod(2.98, x24),
                                m.Prod(3.47, x34),
                                m.Prod(2.24, x44),
                                m.Prod(2.08, x54)),
                          m.Sum(m.Prod(0.25, w14),
                                m.Prod(0.25, w24),
                                m.Prod(0.25, w34),
                                m.Prod(0.25, w44),
                                m.Prod(0.25, w54))), 20.25);
      cut[7] = m.Le(m.Sum(m.Sum(m.Prod(2.08, x15),
                                m.Prod(2.98, x25),
                                m.Prod(3.47, x35),
                                m.Prod(2.24, x45),
                                m.Prod(2.08, x55)),
                          m.Sum(m.Prod(0.25, w15),
                                m.Prod(0.25, w25),
                                m.Prod(0.25, w35),
                                m.Prod(0.25, w45),
                                m.Prod(0.25, w55))), 16.25);

      return cut;
   }

   public static void Main(string[] args) {
      try {
         Cplex cplex = new Cplex();
       
         string filename = "../../../../examples/data/noswot.mps";
         if ( args.Length > 0 ) {
            filename = args[0];
            if ( filename.IndexOf("noswot") < 0 ) {
               System.Console.WriteLine("Error: noswot model is required."); 
               return;
            }
         }
         cplex.ImportModel(filename);

         IEnumerator matrixEnum = cplex.GetLPMatrixEnumerator();
         matrixEnum.MoveNext();

         ILPMatrix lp = (ILPMatrix)matrixEnum.Current;

         // Use AddUserCuts when the added constraints strengthen the
         // formulation.  Use AddLazyConstraints when the added constraints
         // remove part of the feasible region.  Use AddCuts when you are
         // not certain.
       
         cplex.AddUserCuts(MakeCuts(cplex, lp));
       
         cplex.SetParam(Cplex.IntParam.MIPInterval, 1000);
         if ( cplex.Solve() ) {
            System.Console.WriteLine("Solution status = " + cplex.GetStatus());
            System.Console.WriteLine("Solution value  = " + cplex.ObjValue);
         }
         cplex.End();
      }
      catch (ILOG.Concert.Exception e) {
         System.Console.WriteLine("Concert exception caught: " + e);
      }
   }
}
