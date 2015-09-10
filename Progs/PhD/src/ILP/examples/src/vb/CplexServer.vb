' ---------------------------------------------------------------------------
' File: CplexServer.vb
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
'
' CplexServer.vb - Entering a problem using CplexModeler and
'                  serializing it for solving
'
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System
Imports System.IO
Imports System.Runtime.Serialization
Imports System.Runtime.Serialization.Formatters.Binary




Public Class CplexServer 

   ' define class to transfer model to server
   <System.Serializable()> _
   Friend Class ModelData
      Friend model  As IModel
      Friend vars() As INumVar
      
      Friend Sub New(m As IModel, v() As INumVar)
         model = m
         vars = v
      End Sub 'New
   End Class 'ModelData
   
   ' define class to transfer back solution
   <System.Serializable()> _
   Friend Class SolutionData
      Friend status As Cplex.CplexStatus
      Friend obj    As Double
      Friend vals() As Double
   End Class 'SolutionData
   
   
   Public Overloads Shared Sub Main(ByVal args() As String)
      Try
         ' setup files to transfer model to server
         Dim mfile As String = "Model.dat"
         Dim sfile As String = "Solution.dat"
         
         
         ' build model
         Dim var(1)() As INumVar
         Dim rng(1)() As IRange
         
         Dim model As New CplexModeler()
         PopulateByRow(model, var, rng)
         
         Dim mstream As New FileStream(mfile, FileMode.Create)
         Dim formatter As New BinaryFormatter()
         formatter.Serialize(mstream, New ModelData(model, var(0)))
         mstream.Close()
         
         ' start server
         Dim server As New Server(mfile, sfile)
         
         Dim sol As SolutionData = Nothing
         Dim sstream As New FileStream(sfile, FileMode.Open)
         sol = CType(formatter.Deserialize(sstream), SolutionData)
         sstream.Close()
         
         System.Console.WriteLine(("Solution status = " + sol.status.ToString))
         
         If sol.status.Equals(Cplex.CplexStatus.Optimal) Then
            System.Console.WriteLine(("Solution value = " & sol.obj))
            Dim nvars As Integer = var(0).Length
            Dim j As Integer
            
            For j = 0 To nvars - 1
               System.Console.WriteLine(("Variable " & j & ": Value = " & sol.vals(j)))
            Next j
         End If 
      Catch e As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception '" + e.ToString + "' caught"))
      Catch t As System.Exception
         System.Console.WriteLine(("terminating due to exception " + t.ToString))
      End Try
   End Sub 'Main
   
   
   
   ' The following method populates the problem with data for the
   ' following linear program:
   '
   '    Maximize
   '     x1 + 2 x2 + 3 x3
   '    Subject To
   '     - x1 + x2 + x3 <= 20
   '     x1 - 3 x2 + x3 <= 30
   '    Bounds
   '     0 <= x1 <= 40
   '    End
   '
   ' using the IModeler API
   Friend Shared Sub PopulateByRow(model As IModeler, var()() As INumVar, rng()() As IRange)
      Dim lb As Double() =  {0.0, 0.0, 0.0}
      Dim ub As Double() =  {40.0, System.Double.MaxValue, System.Double.MaxValue}
      Dim varname As String() =  {"x1", "x2", "x3"}
      Dim x As INumVar() = model.NumVarArray(3, lb, ub, varname)
      var(0) = x
      
      Dim objvals As Double() =  {1.0, 2.0, 3.0}
      model.AddMaximize(model.ScalProd(x, objvals))
      
      rng(0) = New IRange(2) {}
      rng(0)(0) = model.AddLe(model.Sum(model.Prod(- 1.0, x(0)), model.Prod(1.0, x(1)), model.Prod(1.0, x(2))), 20.0, "c1")
      rng(0)(1) = model.AddLe(model.Sum(model.Prod(1.0, x(0)), model.Prod(- 3.0, x(1)), model.Prod(1.0, x(2))), 30.0, "c2")
   End Sub 'PopulateByRow
   
   
   ' The server class
   Friend Class Server
      Friend sfile As String
      Friend mfile As String
      
      
      Friend Sub New(modelfile As String, solutionfile As String)
         mfile = modelfile
         sfile = solutionfile
         Try
            Dim mstream As New FileStream(mfile, FileMode.Open)
            Dim sstream As New FileStream(sfile, FileMode.Create)
            
            Dim cplex As New Cplex()
            Dim data As ModelData = Nothing
            
            Dim formatter As New BinaryFormatter()
            data = CType(formatter.Deserialize(mstream), ModelData)
            mstream.Close()
            
            
            cplex.SetModel(data.model)
            
            Dim sol As New SolutionData()
            If cplex.Solve() Then
               sol.obj = cplex.ObjValue
               sol.vals = cplex.GetValues(data.vars)
            End If
            sol.status = cplex.GetCplexStatus()
            
            formatter.Serialize(sstream, sol)
            sstream.Close()
            
            cplex.End()
         Catch t As System.Exception
            System.Console.WriteLine(("server terminates due to " + t.ToString))
         End Try
      End Sub 'New
   End Class 'Server
End Class 'Cplexserver
