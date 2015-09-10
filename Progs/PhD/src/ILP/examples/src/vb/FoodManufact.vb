' ---------------------------------------------------------------------------
' File: FoodManufact.vb
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
' FoodManufact.vb - An implementation of an example from H.P.
'                   Williams' book Model Building in Mathematical
'                   Programming.  This example solves a
'                   food production planning problem.  It
'                   demonstrates the use of CPLEX's
'                   linearization capability.
' 
Imports ILOG.Concert
Imports ILOG.CPLEX


Public Class FoodManufact
   Friend Shared v1 As Integer = 0
   Friend Shared v2 As Integer = 1
   Friend Shared o1 As Integer = 2
   Friend Shared o2 As Integer = 3
   Friend Shared o3 As Integer = 4
   
   Friend Shared cost As Double()() =  {New Double() {110.0, 120.0, 130.0, 110.0, 115.0}, _
                                        New Double() {130.0, 130.0, 110.0, 90.0, 115.0},  _
                                        New Double() {110.0, 140.0, 130.0, 100.0, 95.0}, _
                                        New Double() {120.0, 110.0, 120.0, 120.0, 125.0}, _
                                        New Double() {100.0, 120.0, 150.0, 110.0, 105.0}, _
                                        New Double() {90.0, 100.0, 140.0, 80.0, 135.0}}
   
   Public Overloads Shared Sub Main(ByVal args() As String)
   
      Dim nMonths   As Integer = cost.Length
      Dim nProducts As Integer = cost(0).Length
      
      Try
         Dim cplex As New Cplex()
         
         Dim produce As INumVar() = cplex.NumVarArray(nMonths, 0, System.Double.MaxValue)
         Dim use(nMonths)()   As INumVar
         Dim buy(nMonths)()   As INumVar
         Dim store(nMonths)() As INumVar
         
         Dim i As Integer
         For i = 0 To nMonths - 1
            use(i) = cplex.NumVarArray(nProducts, 0.0, System.Double.MaxValue)
            buy(i) = cplex.NumVarArray(nProducts, 0.0, System.Double.MaxValue)
            store(i) = cplex.NumVarArray(nProducts, 0.0, 1000.0)
         Next i
         
         Dim p As Integer
         For p = 0 To nProducts - 1
            store((nMonths - 1))(p).LB = 500.0
            store((nMonths - 1))(p).UB = 500.0
         Next p
         
         Dim profit As INumExpr = cplex.NumExpr()
         For i = 0 To nMonths - 1
            ' Not more than 200 tons of vegetable oil can be refined
            cplex.AddLe(cplex.Sum(use(i)(v1), use(i)(v2)), 200.0)
            
            ' Not more than 250 tons of non-vegetable oil can be refined
            cplex.AddLe(cplex.Sum(use(i)(o1), use(i)(o2), use(i)(o3)), 250.0)
            
            ' Constraints on food composition
            cplex.AddLe(cplex.Prod(3.0, produce(i)), _
                        cplex.Sum(cplex.Prod(8.8, use(i)(v1)), _
                                  cplex.Prod(6.1, use(i)(v2)), _
                                  cplex.Prod(2.0, use(i)(o1)), _
                                  cplex.Prod(4.2, use(i)(o2)), _
                                  cplex.Prod(5.0, use(i)(o3))))
            cplex.AddGe(cplex.Prod(6.0, produce(i)), _
                        cplex.Sum(cplex.Prod(8.8, use(i)(v1)), _
                                  cplex.Prod(6.1, use(i)(v2)),  _
                                  cplex.Prod(2.0, use(i)(o1)), _
                                  cplex.Prod(4.2, use(i)(o2)),  _
                                  cplex.Prod(5.0, use(i)(o3))))
            cplex.AddEq(produce(i), cplex.Sum(use(i)))
            
            ' Raw oil can be stored for later use
            If i = 0 Then
               For p = 0 To nProducts - 1
                  cplex.AddEq(cplex.Sum(500.0, buy(i)(p)), cplex.Sum(use(i)(p), store(i)(p)))
               Next p
            Else
               For p = 0 To nProducts - 1
                  cplex.AddEq(cplex.Sum(store((i - 1))(p), buy(i)(p)), cplex.Sum(use(i)(p), store(i)(p)))
               Next p
            End If
            
            ' Logical constraints:
            ' The food cannot use more than 3 oils
            ' (or at least two oils must not be used)
            cplex.AddGe(cplex.Sum(cplex.Eq(use(i)(v1), 0), _
                                  cplex.Eq(use(i)(v2), 0), _
                                  cplex.Eq(use(i)(o1), 0), _
                                  cplex.Eq(use(i)(o2), 0), _
                                  cplex.Eq(use(i)(o3), 0)), 2)
            
            ' When an oil is used, the quantity must be at least 20 tons
            For p = 0 To nProducts - 1
               cplex.Add(cplex.Or(cplex.Eq(use(i)(p), 0), _
                                  cplex.Ge(use(i)(p), 20)))
            Next p
            
            ' If products v1 or v2 are used, then product o3 is also used
            cplex.Add(cplex.IfThen(cplex.Or(cplex.Ge(use(i)(v1), 20), _
                                            cplex.Ge(use(i)(v2), 20)), _
                                            cplex.Ge(use(i)(o3), 20)))
            
            ' Objective function
            profit = cplex.Sum(profit, cplex.Prod(150, produce(i)))
            profit = cplex.Diff(profit, cplex.ScalProd(cost(i), buy(i)))
            profit = cplex.Diff(profit, cplex.Prod(5, cplex.Sum(store(i))))
         Next i
         
         cplex.AddMaximize(profit)
         
         If cplex.Solve() Then
            System.Console.WriteLine((" Maximum profit = " + cplex.ObjValue.ToString))
            For i = 0 To nMonths - 1
               System.Console.WriteLine(" Month {0}", i)

               System.Console.Write("  . buy   ")
               For p = 0 To nProducts - 1
                  System.Console.Write("{0,8:F2} ", cplex.GetValue(buy(i)(p)))
               Next p
               System.Console.WriteLine()

               System.Console.Write("  . use   ")
               For p = 0 To nProducts - 1
                  System.Console.Write("{0,8:F2} ", cplex.GetValue(use(i)(p)))
               Next p
               System.Console.WriteLine()

               System.Console.Write("  . store ")
               For p = 0 To nProducts - 1
                  System.Console.Write("{0,8:F2} ", cplex.GetValue(store(i)(p)))
               Next p
               System.Console.WriteLine()
            Next i
         End If
         cplex.End()
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + e.ToString))
      End Try
   End Sub 'Main
End Class 'FoodManufact
