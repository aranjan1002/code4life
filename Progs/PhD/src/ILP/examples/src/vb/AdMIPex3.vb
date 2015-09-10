' ---------------------------------------------------------------------------
' File: AdMIPex3.vb
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
' AdMIPex3.vb -  Using the branch callback for optimizing a MIP
'                problem with Special Ordered Sets Type 1, with
'                all the variables binary
'
' To run this example, command line arguments are required.
' i.e.,   AdMIPex3   filename
'
' Example:
'     AdMIPex3  example.mps
'
Imports ILOG.Concert
Imports ILOG.CPLEX
Imports System.Collections


Public Class AdMIPex3
   Friend Shared EPS As Double = 0.0001

   Public Class SOSbranch
      Inherits Cplex.BranchCallback
      Friend _sos() As ISOS1


      Public Sub New(ByVal sos() As ISOS1)
         _sos = sos
      End Sub 'New


      Public Overrides Sub Main()
         Dim bestx As Double = EPS
         Dim besti As Integer = -1
         Dim bestj As Integer = -1
         Dim num As Integer = _sos.Length

         Dim var As INumVar() = Nothing
         Dim x As Double() = Nothing

         Dim i As Integer

         For i = 0 To num - 1
            If GetSOSFeasibility(_sos(i)).Equals(Cplex.IntegerFeasibilityStatus.Infeasible) Then
               var = _sos(i).NumVars
               x = GetValues(var)

               Dim n As Integer = var.Length
               Dim j As Integer

               For j = 0 To n - 1
                  Dim inf As Double = System.Math.Abs((x(j) - System.Math.Round(x(j))))
                  If inf > bestx Then
                     bestx = inf
                     besti = i
                     bestj = j
                  End If
               Next j
            End If
         Next i

         If besti >= 0 Then
            var = _sos(besti).NumVars
            Dim n As Integer = var.Length

            Dim dir(n) As Cplex.BranchDirection
            Dim val(n) As Double

            Dim j As Integer

            For j = 0 To n - 1
               If j <> bestj Then
                  dir(j) = Cplex.BranchDirection.Down
                  val(j) = 0.0
               Else
                  dir(j) = Cplex.BranchDirection.Up
                  val(j) = 1.0
               End If
            Next j
            MakeBranch(var, val, dir, ObjValue)
            MakeBranch(var(bestj), 0.0, Cplex.BranchDirection.Down, ObjValue)
         End If
       End Sub 'Main
   End Class 'SOSbranch

   Public Overloads Shared Sub Main(ByVal args() As String)
      If args.Length <> 1 Then
         System.Console.WriteLine("Usage: AdMIPex1 filename")
         System.Console.WriteLine("   where filename is a file with extension ")
         System.Console.WriteLine("      MPS, SAV, or LP (lower case is allowed)")
         System.Console.WriteLine(" Exiting...")
         System.Environment.Exit(-1)
      End If

      Try
         Dim cplex As New Cplex()

         cplex.ImportModel(args(0))

         If cplex.NSOS1 > 0 Then
            Dim sos1(cplex.NSOS1-1) As ISOS1
            Dim i As Integer = 0
            Dim sosenum As IEnumerator = cplex.GetSOS1Enumerator()

            Do While sosenum.MoveNext()
               sos1(i) = CType(sosenum.Current, ISOS1)
               i = i + 1
            Loop
            cplex.Use(New SOSbranch(sos1))
            System.Console.WriteLine(("using SOS branch callback for " & sos1.Length & " SOS1s"))
         End If

	 cplex.SetParam(cplex.IntParam.MIPSearch, cplex.MIPSearch.Traditional)
         If cplex.Solve() Then
            Dim matrixEnum As IEnumerator = cplex.GetLPMatrixEnumerator()
            matrixEnum.MoveNext()

            Dim matrix As ILPMatrix = CType(matrixEnum.Current, ILPMatrix)
            Dim vals As Double() = cplex.GetValues(matrix)

            System.Console.WriteLine(("Solution status = " + cplex.GetStatus().ToString))
            System.Console.WriteLine(("Solution value  = " & cplex.ObjValue))
         End If
         cplex.End()
      Catch exc As ILOG.Concert.Exception
         System.Console.WriteLine(("Concert exception caught: " + exc.ToString))
      End Try
   End Sub 'Main
End Class 'AdMIPex3
