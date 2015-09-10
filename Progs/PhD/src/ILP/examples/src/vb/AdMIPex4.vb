' ---------------------------------------------------------------------------
' File: AdMIPex4.vb
' Version 12.2  
' ---------------------------------------------------------------------------
' Licensed Materials - Property of IBM
' 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
' Copyright IBM Corporation 1999, 2010. All Rights Reserved.
' 
' US Government Users Restricted Rights - Use, duplication or
' disclosure restricted by GSA ADP Schedule Contract with
' IBM Corp.
' ---------------------------------------------------------------------------
'
' AdMIPex4.vb -- Solving noswot by adding cuts
'
' Examples AdMIPex4.vb and AdMIPex5.vb both solve the MIPLIB
' 3.0 model noswot.mps by adding user cuts.  AdMIPex4.vb adds
' these cuts to the cut table before the branch-and-cut process
' begins, while AdMIPex5.vb adds them through the user cut callback
' during the branch-and-cut process. 

Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System.Collections


Public Class AdMIPex4
   
   Public Shared Function MakeCuts(m As IModeler, lp As ILPMatrix) As IRange()
      Dim x11 As INumVar = Nothing
      Dim x12 As INumVar = Nothing
      Dim x13 As INumVar = Nothing
      Dim x14 As INumVar = Nothing
      Dim x15 As INumVar = Nothing
      Dim x21 As INumVar = Nothing
      Dim x22 As INumVar = Nothing
      Dim x23 As INumVar = Nothing
      Dim x24 As INumVar = Nothing
      Dim x25 As INumVar = Nothing
      Dim x31 As INumVar = Nothing
      Dim x32 As INumVar = Nothing
      Dim x33 As INumVar = Nothing
      Dim x34 As INumVar = Nothing
      Dim x35 As INumVar = Nothing
      Dim x41 As INumVar = Nothing
      Dim x42 As INumVar = Nothing
      Dim x43 As INumVar = Nothing
      Dim x44 As INumVar = Nothing
      Dim x45 As INumVar = Nothing
      Dim x51 As INumVar = Nothing
      Dim x52 As INumVar = Nothing
      Dim x53 As INumVar = Nothing
      Dim x54 As INumVar = Nothing
      Dim x55 As INumVar = Nothing
      Dim w11 As INumVar = Nothing
      Dim w12 As INumVar = Nothing
      Dim w13 As INumVar = Nothing
      Dim w14 As INumVar = Nothing
      Dim w15 As INumVar = Nothing
      Dim w21 As INumVar = Nothing
      Dim w22 As INumVar = Nothing
      Dim w23 As INumVar = Nothing
      Dim w24 As INumVar = Nothing
      Dim w25 As INumVar = Nothing
      Dim w31 As INumVar = Nothing
      Dim w32 As INumVar = Nothing
      Dim w33 As INumVar = Nothing
      Dim w34 As INumVar = Nothing
      Dim w35 As INumVar = Nothing
      Dim w41 As INumVar = Nothing
      Dim w42 As INumVar = Nothing
      Dim w43 As INumVar = Nothing
      Dim w44 As INumVar = Nothing
      Dim w45 As INumVar = Nothing
      Dim w51 As INumVar = Nothing
      Dim w52 As INumVar = Nothing
      Dim w53 As INumVar = Nothing
      Dim w54 As INumVar = Nothing
      Dim w55 As INumVar = Nothing
      
      Dim vars As INumVar() = lp.NumVars
      Dim num As Integer = vars.Length
      
      Dim i As Integer
      
      For i = 0 To num - 1
         If vars(i).Name.Equals("X11") Then
            x11 = vars(i)
         ElseIf vars(i).Name.Equals("X12") Then
            x12 = vars(i)
         ElseIf vars(i).Name.Equals("X13") Then
            x13 = vars(i)
         ElseIf vars(i).Name.Equals("X14") Then
            x14 = vars(i)
         ElseIf vars(i).Name.Equals("X15") Then
            x15 = vars(i)
         ElseIf vars(i).Name.Equals("X21") Then
            x21 = vars(i)
         ElseIf vars(i).Name.Equals("X22") Then
            x22 = vars(i)
         ElseIf vars(i).Name.Equals("X23") Then
            x23 = vars(i)
         ElseIf vars(i).Name.Equals("X24") Then
            x24 = vars(i)
         ElseIf vars(i).Name.Equals("X25") Then
            x25 = vars(i)
         ElseIf vars(i).Name.Equals("X31") Then
            x31 = vars(i)
         ElseIf vars(i).Name.Equals("X32") Then
            x32 = vars(i)
         ElseIf vars(i).Name.Equals("X33") Then
            x33 = vars(i)
         ElseIf vars(i).Name.Equals("X34") Then
            x34 = vars(i)
         ElseIf vars(i).Name.Equals("X35") Then
            x35 = vars(i)
         ElseIf vars(i).Name.Equals("X41") Then
            x41 = vars(i)
         ElseIf vars(i).Name.Equals("X42") Then
            x42 = vars(i)
         ElseIf vars(i).Name.Equals("X43") Then
            x43 = vars(i)
         ElseIf vars(i).Name.Equals("X44") Then
            x44 = vars(i)
         ElseIf vars(i).Name.Equals("X45") Then
            x45 = vars(i)
         ElseIf vars(i).Name.Equals("X51") Then
            x51 = vars(i)
         ElseIf vars(i).Name.Equals("X52") Then
            x52 = vars(i)
         ElseIf vars(i).Name.Equals("X53") Then
            x53 = vars(i)
         ElseIf vars(i).Name.Equals("X54") Then
            x54 = vars(i)
         ElseIf vars(i).Name.Equals("X55") Then
            x55 = vars(i)
         ElseIf vars(i).Name.Equals("W11") Then
            w11 = vars(i)
         ElseIf vars(i).Name.Equals("W12") Then
            w12 = vars(i)
         ElseIf vars(i).Name.Equals("W13") Then
            w13 = vars(i)
         ElseIf vars(i).Name.Equals("W14") Then
            w14 = vars(i)
         ElseIf vars(i).Name.Equals("W15") Then
            w15 = vars(i)
         ElseIf vars(i).Name.Equals("W21") Then
            w21 = vars(i)
         ElseIf vars(i).Name.Equals("W22") Then
            w22 = vars(i)
         ElseIf vars(i).Name.Equals("W23") Then
            w23 = vars(i)
         ElseIf vars(i).Name.Equals("W24") Then
            w24 = vars(i)
         ElseIf vars(i).Name.Equals("W25") Then
            w25 = vars(i)
         ElseIf vars(i).Name.Equals("W31") Then
            w31 = vars(i)
         ElseIf vars(i).Name.Equals("W32") Then
            w32 = vars(i)
         ElseIf vars(i).Name.Equals("W33") Then
            w33 = vars(i)
         ElseIf vars(i).Name.Equals("W34") Then
            w34 = vars(i)
         ElseIf vars(i).Name.Equals("W35") Then
            w35 = vars(i)
         ElseIf vars(i).Name.Equals("W41") Then
            w41 = vars(i)
         ElseIf vars(i).Name.Equals("W42") Then
            w42 = vars(i)
         ElseIf vars(i).Name.Equals("W43") Then
            w43 = vars(i)
         ElseIf vars(i).Name.Equals("W44") Then
            w44 = vars(i)
         ElseIf vars(i).Name.Equals("W45") Then
            w45 = vars(i)
         ElseIf vars(i).Name.Equals("W51") Then
            w51 = vars(i)
         ElseIf vars(i).Name.Equals("W52") Then
            w52 = vars(i)
         ElseIf vars(i).Name.Equals("W53") Then
            w53 = vars(i)
         ElseIf vars(i).Name.Equals("W54") Then
            w54 = vars(i)
         ElseIf vars(i).Name.Equals("W55") Then
            w55 = vars(i)
         End If
      Next i

      Dim cut(7) As IRange
      cut(0) = m.Le(m.Diff(x21, x22), 0.0)
      cut(1) = m.Le(m.Diff(x22, x23), 0.0)
      cut(2) = m.Le(m.Diff(x23, x24), 0.0)
      cut(3) = m.Le(m.Sum(m.Sum(m.Prod(2.08, x11), m.Prod(2.98, x21), m.Prod(3.47, x31), _
                                m.Prod(2.24, x41), m.Prod(2.08, x51)), _
                          m.Sum(m.Prod(0.25, w11), m.Prod(0.25, w21), m.Prod(0.25, w31), _
                                m.Prod(0.25, w41), m.Prod(0.25, w51))), 20.25)
      cut(4) = m.Le(m.Sum(m.Sum(m.Prod(2.08, x12), m.Prod(2.98, x22), m.Prod(3.47, x32), _
                                m.Prod(2.24, x42), m.Prod(2.08, x52)), _
                          m.Sum(m.Prod(0.25, w12), m.Prod(0.25, w22), m.Prod(0.25, w32), _
                                  m.Prod(0.25, w42), m.Prod(0.25, w52))), 20.25)
      cut(5) = m.Le(m.Sum(m.Sum(m.Prod(2.08, x13), m.Prod(2.98, x23), m.Prod(3.47, x33), _
                                m.Prod(2.24, x43), m.Prod(2.08, x53)), _
                          m.Sum(m.Prod(0.25, w13), m.Prod(0.25, w23), m.Prod(0.25, w33), _
                                m.Prod(0.25, w43), m.Prod(0.25, w53))), 20.25)
      cut(6) = m.Le(m.Sum(m.Sum(m.Prod(2.08, x14), m.Prod(2.98, x24), m.Prod(3.47, x34), _
                                m.Prod(2.24, x44), m.Prod(2.08, x54)), _
                          m.Sum(m.Prod(0.25, w14), m.Prod(0.25, w24), m.Prod(0.25, w34), _
                                m.Prod(0.25, w44), m.Prod(0.25, w54))), 20.25)
      cut(7) = m.Le(m.Sum(m.Sum(m.Prod(2.08, x15), m.Prod(2.98, x25), m.Prod(3.47, x35), _
                                m.Prod(2.24, x45), m.Prod(2.08, x55)), _
                          m.Sum(m.Prod(0.25, w15), m.Prod(0.25, w25), m.Prod(0.25, w35), _
                                m.Prod(0.25, w45), m.Prod(0.25, w55))), 16.25)

      Return cut
   End Function 'MakeCuts

   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim cplex As New Cplex()

         Dim filename As String = "../../../../examples/data/noswot.mps"
         If args.Length > 0 Then
            filename = args(0)
            If filename.IndexOf("noswot") < 0 Then
               System.Console.WriteLine("Error: noswot model is required.")
               Return
            End If
         End If
         cplex.ImportModel(filename)

         Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
         matrixEnum.MoveNext()

         Dim lp As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)

         ' Use AddUserCuts when the added constraints strengthen the
         ' formulation.  Use AddLazyConstraints when the added constraints
         ' remove part of the feasible region.  Use AddCuts when you are
         ' not certain.

         cplex.AddUserCuts(MakeCuts(cplex, lp))

         cplex.SetParam(cplex.IntParam.MIPInterval, 1000)
         If cplex.Solve() Then
            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " + cplex.ObjValue.ToString))
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      End Try
   End Sub 'Main
End Class 'AdMIPex4
