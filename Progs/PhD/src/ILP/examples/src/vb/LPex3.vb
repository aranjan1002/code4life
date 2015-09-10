' ---------------------------------------------------------------------------
' File: LPex3.vb
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
' LPex3.vb, example of adding constraints to solve a problem
'
' Modified example from Chvatal, "Linear Programming", Chapter 26.
'   minimize  c*x
'   subject to  Hx = d
'               Ax = b
'               l <= x <= u
'   where
'
'   H = (  0  0  0  0  0  0  0 -1 -1 -1  0  0 )  d = ( -1 )
'       (  1  0  0  0  0  1  0  1  0  0  0  0 )      (  4 )
'       (  0  1  0  1  0  0  1  0  1  0  0  0 )      (  1 )
'       (  0  0  1  0  1  0  0  0  0  1  0  0 )      (  1 )
'       (  0  0  0  0  0 -1 -1  0  0  0 -1  1 )      ( -2 )
'       (  0  0  0 -1 -1  0  1  0  0  0  1  0 )      ( -2 )
'       ( -1 -1 -1  0  0  0  0  0  0  0  0 -1 )      ( -1 )
'
'   A = (  0  0  0  0  0  0  0  0  0  0  2  5 )  b = (  2 )
'       (  1  0  1  0  0  1  0  0  0  0  0  0 )      (  3 )
'
'   c = (  1  1  1  1  1  1  1  0  0  0  2  2 )
'   l = (  0  0  0  0  0  0  0  0  0  0  0  0 )
'   u = ( 50 50 50 50 50 50 50 50 50 50 50 50 )
'
'
'  Treat the constraints with A as the complicating constraints, and
'  the constraints with H as the "simple" problem.
'  
'  The idea is to solve the simple problem first, and then add the
'  constraints for the complicating constraints, and solve with dual.
'
Imports ILOG.Concert
Imports ILOG.CPLEX

 _


Public Class LPex3
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         Dim nvars As Integer = 12 

         Dim cplex As New Cplex()
         Dim lp As ILPMatrix = cplex.AddLPMatrix()

         ' add empty corresponding to new variables columns to lp
         Dim x As INumVar() = cplex.NumVarArray(cplex.ColumnArray(lp, nvars), 0, 50)

         ' add rows to lp
         Dim d As Double() = {-1.0, 4.0, 1.0, 1.0, -2.0, -2.0, -1.0}
         Dim valH As Double()() = {New Double() {-1.0, -1.0, -1.0}, _
                                   New Double() { 1.0,  1.0,  1.0}, _
                                   New Double() { 1.0,  1.0,  1.0,  1.0}, _
                                   New Double() { 1.0,  1.0,  1.0}, _
                                   New Double() {-1.0, -1.0, -1.0,  1.0}, _
                                   New Double() {-1.0, -1.0,  1.0}, _
                                   New Double() {-1.0, -1.0, -1.0, -1.0}}
         Dim indH As Integer()() = {New Integer() {7, 8, 9}, _
                                    New Integer() {0, 5, 7}, _
                                    New Integer() {1, 3, 6, 8}, _
                                    New Integer() {2, 4, 9}, _
                                    New Integer() {5, 6, 10, 11}, _
                                    New Integer() {3, 4, 10}, _
                                    New Integer() {4, 6, 7}}
         lp.AddRows(d, d, indH, valH)

         ' add the objective function
         Dim objvals As Double() = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, _
                                    1.0, 0.0, 0.0, 0.0, 2.0, 2.0}
         cplex.AddMinimize(cplex.ScalProd(x, objvals))

         ' Solve initial problem with the network optimizer
         cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Network)
         cplex.Solve()
         System.Console.WriteLine(("After network optimization, objective is " & cplex.ObjValue))

         ' add rows from matrix A to lp
         Dim b As Double() = {2.0, 3.0}
         Dim valA As Double()() = {New Double() {2.0, 5.0}, _
                                   New Double() {1.0, 1.0, 1.0}}
         Dim indA As Integer()() = {New Integer() {10, 11}, _
                                    New Integer() {0, 2, 5}}
         lp.AddRows(b, b, indA, valA)

         ' Because the problem is dual feasible with the rows added, using
         ' the dual simplex method is indicated.
         cplex.SetParam(cplex.IntParam.RootAlg, cplex.Algorithm.Dual)
         If cplex.Solve() Then
            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " + cplex.ObjValue.ToString))

            Dim sol As Double() = cplex.GetValues(lp)
            Dim j As Integer

            For j = 0 To nvars - 1
               System.Console.WriteLine(("Variable " & j & ": Value = " & sol(j)))
            Next j
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + e.ToString + "' caught"))
      End Try
   End Sub 'Main
End Class 'LPex3
