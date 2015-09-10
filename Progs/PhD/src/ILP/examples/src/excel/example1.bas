Attribute VB_Name = "example1"
' --------------------------------------------------------------------------
' File: example1.bas
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

Sub Example1()
    ' Maximize cell A1/R1C1 with respect to some constraints.
    
    Dim Cell As Range
    Set Cell = ActiveSheet.Cells(1, 1)
    
    CPXclear                                               ' Clear any CPLEX data from sheet.
    CPXsetObjective ObjCell:=Cell, Sense:=1                ' Maximize A1/R1C1
    CPXaddVariable Variable:=Cell, Ub:=8.5, Integral:=True ' A1/R1C1 is also a variable that
                                                           ' must be integral and must not
                                                           ' exceed 8.5
    ' We add no constraints. The only constraint is the upper bound of the variable.
    
    CPXsolve                                               ' Solve the problem.
End Sub

