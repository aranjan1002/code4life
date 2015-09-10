' ---------------------------------------------------------------------------
' File: Transport.vb
' Version 12.2  
' ---------------------------------------------------------------------------
' Licensed Materials - Property of IBM
' 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
' Copyright IBM Corporation 2001, 2010. All Rights Reserved.
' 
' US Government Users Restricted Rights - Use, duplication or
' disclosure restricted by GSA ADP Schedule Contract with
' IBM Corp.
' ---------------------------------------------------------------------------
Imports ILOG.CPLEX
Imports ILOG.Concert


Public Class Transport
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      If args.Length < 1 Then
         System.Console.WriteLine("Usage: Transport <type>")
         System.Console.WriteLine("  type = 0 -> convex  piecewise linear model")
         System.Console.WriteLine("  type = 1 -> concave piecewise linear model")
         Return
      End If

      Try
         Dim cplex As New Cplex()

         Dim nbDemand As Integer = 4
         Dim nbSupply As Integer = 3
         Dim supply As Double() = {1000.0, 850.0, 1250.0}
         Dim demand As Double() = {900.0, 1200.0, 600.0, 400.0}

         Dim x(nbSupply)() As INumVar
         Dim y(nbSupply)() As INumVar

         Dim i As Integer
         For i = 0 To nbSupply - 1
            x(i) = cplex.NumVarArray(nbDemand, 0.0, System.Double.MaxValue)
            y(i) = cplex.NumVarArray(nbDemand, 0.0, System.Double.MaxValue)
         Next i

         For i = 0 To nbSupply - 1 ' supply must meet demand
            cplex.AddEq(cplex.Sum(x(i)), supply(i))
         Next i
         Dim j As Integer
         For j = 0 To nbDemand - 1 ' demand must meet supply
            Dim v As ILinearNumExpr = cplex.LinearNumExpr()
            For i = 0 To nbSupply - 1
               v.AddTerm(1.0, x(i)(j))
            Next i
            cplex.AddEq(v, demand(j))
         Next j

         Dim points() As Double
         Dim slopes() As Double
         If args(0).ToCharArray()(0) = "0"c Then ' convex case
            points = New Double() {200.0, 400.0}
            slopes = New Double() {30.0, 80.0, 130.0}
         Else ' concave case
            points = New Double() {200.0, 400.0}
            slopes = New Double() {120.0, 80.0, 50.0}
         End If

         For i = 0 To nbSupply - 1  
            For j = 0 To nbDemand - 1
               cplex.AddEq(y(i)(j), cplex.PiecewiseLinear(x(i)(j), points, slopes, 0.0, 0.0))
            Next j
         Next i 

         Dim expr As ILinearNumExpr = cplex.LinearNumExpr()

         For i = 0 To nbSupply - 1
            For j = 0 to nbDemand - 1
               expr.AddTerm(y(i)(j), 1.0)
            Next j
         Next i 

         cplex.AddMinimize(expr)

         If cplex.Solve() Then
            System.Console.WriteLine(" - Solution: ")
            For i = 0 To nbSupply - 1
               System.Console.Write(("   " & i & ": "))
               For j = 0 To nbDemand - 1
                  System.Console.Write(("" & cplex.GetValue(x(i)(j)) & "" & ControlChars.Tab))
               Next j
               System.Console.WriteLine()
            Next i
            System.Console.WriteLine(("   Cost = " & cplex.ObjValue))
         End If
         cplex.End()
      Catch exc As ILOG.Concert.Exception
         System.Console.WriteLine(exc)
      End Try
   End Sub 'Main
End Class 'Transport
