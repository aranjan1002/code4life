Attribute VB_Name = "example3"
' --------------------------------------------------------------------------
' File: example3.bas
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

Sub Example3()
    ' Solve a more complex problem:
    ' Minimize x1 + 2x2 + 3x3 + 4x4 + 5x5 + 6x6 + 7x7 + 8x8
    ' So that 2x1 + 3x2 + 5x3 + 7x4 + 11x5 + 13x6 + 17x7 + 19x8 = 1013
    ' and all variables are non-negative and integral.
    ' Variables are the cells in range Vars. The other two ranges hold
    ' the coefficients for the objective function and the constraint.
    Dim Vars As Range
    Set Vars = Range(ActiveSheet.Cells(1, 3), ActiveSheet.Cells(1, 10))
    Dim ObjCoefs As Range
    Set ObjCoefs = Range(ActiveSheet.Cells(2, 1), ActiveSheet.Cells(9, 1))
    Dim ConsCoefs As Range
    Set ConsCoefs = Range(ActiveSheet.Cells(2, 2), ActiveSheet.Cells(9, 2))
    
    ' Clear all CPLEX related data.
    CPXclear

    ' Setup objective function. First set coefficient values, then define
    ' a cell to be the matrix product of coefficients and variables and
    ' require this cell to be minimized.
    ActiveSheet.Cells(2, 1).Value = 1
    ActiveSheet.Cells(3, 1).Value = 2
    ActiveSheet.Cells(4, 1).Value = 3
    ActiveSheet.Cells(5, 1).Value = 4
    ActiveSheet.Cells(6, 1).Value = 5
    ActiveSheet.Cells(7, 1).Value = 6
    ActiveSheet.Cells(8, 1).Value = 7
    ActiveSheet.Cells(9, 1).Value = 8
    ActiveSheet.Cells(1, 1).Formula = "=MMULT(" & Vars.Address & "," & ObjCoefs.Address & ")"
    CPXsetObjective ObjCell:=ActiveSheet.Cells(1, 1), Sense:=2
    
    ' Add the variables. All variables must be non-negative and integral.
    CPXaddVariable Variable:=Vars, Lb:=0, Integral:=True
    
    ' Setup constraints. First set the coefficient values, then define
    ' a cell to be the matrix product of coefficients and variables and
    ' require this cell to be equal to 1013. Notice how equality is enforced
    ' by specifying identical lower and upper bounds.
    ActiveSheet.Cells(2, 2).Value = 2
    ActiveSheet.Cells(3, 2).Value = 3
    ActiveSheet.Cells(4, 2).Value = 5
    ActiveSheet.Cells(5, 2).Value = 7
    ActiveSheet.Cells(6, 2).Value = 11
    ActiveSheet.Cells(7, 2).Value = 13
    ActiveSheet.Cells(8, 2).Value = 17
    ActiveSheet.Cells(9, 2).Value = 19
    ActiveSheet.Cells(1, 2).Formula = "=MMULT(" & Vars.Address & "," & ConsCoefs.Address & ")"
    CPXaddConstraint Constraint:=ActiveSheet.Cells(1, 2), Lb:=1013, Ub:=1013
    
    ' Warn about functions that are replaced by
    ' their presumed linear or quadratic equivalent. CPLEX does not know about
    ' the MMULT function. But as this is a linear function it can calculate the linear
    ' coefficients defining that function and replace MMULT by the function on the
    ' calculated coefficients. We will get an information message about that.
    ' Pass False if you do not want to see that message.
    CPXsetSpecial LinOrQuad:=True
    
    ' Solve the problem.
    CPXsolve
End Sub
