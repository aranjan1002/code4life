Attribute VB_Name = "example2"
' --------------------------------------------------------------------------
' File: example2.bas
' Version 12.2
' --------------------------------------------------------------------------
' Licensed Materials - Property of IBM
' 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
' Copyright IBM Corporation 2008, 2010. All Rights Reserved.
'
' US Government Users Restricted Rights - Use, duplication or
' disclosure restricted by GSA ADP Schedule Contract with
' IBM Corp.
' --------------------------------------------------------------------------

Option Explicit

' This example uses procedures found in the cplexvba.bas file which is
' in the excel sub-folder of the folder where CPLEX was installed.  Import
' both this file and cplexvba.bas into the Microsoft Excel Visual Basic
' Editor in order to run the example.

Sub Example2()
    ' Minimize A2+A3 so that 2A2+3A3 >= 5 and A2,A3 >= 0.
    
    ' Clear CPLEX data
    CPXclear
    
    ' Objective function.
    Dim Obj As Range
    Set Obj = ActiveSheet.Cells(1, 1)
    Obj.Formula = "=" & ActiveSheet.Cells(2, 1).Address & "+" & ActiveSheet.Cells(3, 1).Address
    CPXsetObjective ObjCell:=Obj, Sense:=2
    
    ' Variables
    CPXaddVariable Variable:=Range(ActiveSheet.Cells(2, 1), ActiveSheet.Cells(3, 1)), Lb:=0
    
    ' Constraints
    Dim Cons As Range
    Set Cons = ActiveSheet.Cells(1, 2)
    Cons.Formula = "=2*" & ActiveSheet.Cells(2, 1).Address & "+3*" & ActiveSheet.Cells(3, 1).Address
    CPXaddConstraint Constraint:=Cons, Lb:=5
    
    ' Solve
    CPXsolve
End Sub

