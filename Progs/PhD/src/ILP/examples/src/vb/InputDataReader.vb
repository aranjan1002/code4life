' ---------------------------------------------------------------------------
' File: InputDataReader.vb
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
' This is a helper class used by several examples to read input data files
' containing arrays in the format [x1, x2, ..., x3].  Up to two-dimensional
' arrays are supported.
'
Imports System
Imports System.Globalization


Public Class InputDataReader

   Public Class InputDataReaderException
      Inherits System.Exception
      
      Friend Sub New(file As String)
         MyBase.New("'" + file + "' contains bad data format")
      End Sub 'New 
   End Class 'InputDataReaderException
   
   Friend _tokens() As String
   Friend _current As Integer
   Friend _fileName As String

   Friend _nfi As NumberFormatInfo = NumberFormatInfo.InvariantInfo
   
   Friend Function NextToken() As String
      Dim token As String = _tokens(_current) 
      _current = _current+1
      Do While token = ""
         token = _tokens(_current)
         _current = _current+1
      Loop
      Return token
   End Function 'NextToken
   
   
   Public Sub New(fileName As String)
      _fileName = fileName
      Dim reader As New System.IO.StreamReader(fileName)
      
      Dim [text] As String = reader.ReadToEnd()
      
      [text] = [text].Replace("[", " [ ")
      [text] = [text].Replace("]", " ] ")
      [text] = [text].Replace(",", " , ")
      [text] = [text].Replace(ControlChars.Quote, " "c)

      _tokens = [text].Split(Nothing)
      
      reader.Close()
      
      _current = 0
   End Sub 'New
   
   
   Friend Function ReadDouble() As Double
      Return [Double].Parse(NextToken(), _nfi)
   End Function 'ReadDouble
   
   
   Friend Function ReadInt() As Integer
      Return Int32.Parse(NextToken(), _nfi)
   End Function 'ReadInt
   
   
   Friend Function ReadString() As String
      Return NextToken()
   End Function 'ReadString
   
   
   Friend Function ReadDoubleArray() As Double()
      Dim token As String = NextToken() ' Read the '['
      If token <> "[" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      Dim values As New System.Collections.ArrayList()
      token = NextToken()
      Do While token <> "]"
         values.Add([Double].Parse(token, _nfi))
         token = NextToken()
         
         If token = "," Then
            token = NextToken()
         Else
            If token <> "]" Then
               Throw New InputDataReaderException(_fileName)
            End If
         End If
      Loop
      If token <> "]" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      ' Fill the array.
      Dim res(values.Count-1) As Double
      Dim i As Integer
      For i = 0 To values.Count - 1
         res(i) = CDbl(values(i))
      Next i
      
      Return res
   End Function 'ReadDoubleArray
   
   
   Friend Function ReadDoubleArrayArray() As Double()()
      Dim token As String = NextToken() ' Read the '['
      If token <> "[" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      Dim values As New System.Collections.ArrayList()
      token = NextToken()
      
      Do While token = "["
         _current -= 1
         
         values.Add(ReadDoubleArray())
         
         token = NextToken()
         If token = "," Then
            token = NextToken()
         Else
            If token <> "]" Then
               Throw New InputDataReaderException(_fileName)
            End If
         End If
      Loop
      If token <> "]" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      ' Fill the array.
      Dim res(values.Count-1)() As Double
      Dim i As Integer
      For i = 0 To values.Count - 1
         res(i) = CType(values(i), Double())
      Next i 
      Return res
   End Function 'ReadDoubleArrayArray
   
   
   Friend Function ReadIntArray() As Integer()
      Dim token As String = NextToken() ' Read the '['
      If token <> "[" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      Dim values As New System.Collections.ArrayList()
      token = NextToken()
      Do While token <> "]"
         values.Add(Int32.Parse(token, _nfi))
         token = NextToken()
         
         If token = "," Then
            token = NextToken()
         Else
            If token <> "]" Then
               Throw New InputDataReaderException(_fileName)
            End If
         End If
      Loop
      If token <> "]" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      ' Fill the array.
      Dim res(values.Count-1) As Integer
      Dim i As Integer
      For i = 0 To values.Count - 1
         res(i) = CInt(values(i))
      Next i 
      Return res
   End Function 'ReadIntArray
   
   
   Friend Function ReadIntArrayArray() As Integer()()
      Dim token As String = NextToken() ' Read the '['
      If token <> "[" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      Dim values As New System.Collections.ArrayList()
      token = NextToken()
      
      Do While token = "["
         _current -= 1
         
         values.Add(ReadIntArray())
         
         token = NextToken()
         If token = "," Then
            token = NextToken()
         Else
            If token <> "]" Then
               Throw New InputDataReaderException(_fileName)
            End If
         End If
      Loop
      If token <> "]" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      ' Fill the array.
      Dim res(values.Count-1)() As Integer
      Dim i As Integer
      For i = 0 To values.Count - 1
         res(i) = CType(values(i), Integer())
      Next i 
      Return res
   End Function 'ReadIntArrayArray
   
   
   Friend Function ReadStringArray() As String()
      Dim token As String = NextToken() ' Read the '['
      If token <> "[" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      Dim values As New System.Collections.ArrayList()
      token = NextToken()
      Do While token <> "]"
         values.Add(token)
         token = NextToken()
         
         If token = "," Then
            token = NextToken()
         Else
            If token <> "]" Then
               Throw New InputDataReaderException(_fileName)
            End If
         End If
      Loop
      If token <> "]" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      ' Fill the array.
      Dim res(values.Count-1) As String
      Dim i As Integer
      For i = 0 To values.Count - 1
         res(i) = CStr(values(i))
      Next i 
      Return res
   End Function 'ReadStringArray
   
   
   Friend Function ReadStringArrayArray() As String()()
      Dim token As String = NextToken() ' Read the '['
      If token <> "[" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      Dim values As New System.Collections.ArrayList()
      token = NextToken()
      
      Do While token = "["
         _current -= 1
         
         values.Add(ReadStringArray())
         
         token = NextToken()
         If token = "," Then
            token = NextToken()
         Else
            If token <> "]" Then
               Throw New InputDataReaderException(_fileName)
            End If
         End If
      Loop
      If token <> "]" Then
         Throw New InputDataReaderException(_fileName)
      End If 
      ' Fill the array.
      Dim res(values.Count-1)() As String
      Dim i As Integer
      For i = 0 To values.Count - 1
         res(i) = CType(values(i), String())
      Next i 
      Return res
   End Function 'ReadStringArrayArray
End Class 'InputDataReader
