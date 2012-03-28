Imports System.Net.Sockets
Imports System.Text

Class FileProcessor

    Shared Sub Main()

        Dim selectedFile As String
        Dim fileText As String
        Dim myClient As New System.Net.Sockets.TcpClient

        selectedFile = Command().Trim("""")

        Try
            fileText = My.Computer.FileSystem.ReadAllText(selectedFile)
        Catch ex As Exception
            MsgBox("File does not contain valid data.")
            Exit Sub
        End Try

        Try
            myClient.Connect("Localhost", 2129)
        Catch ex As Exception
            MsgBox("no client active to receive linked files.")
            Exit Sub
        End Try

        Dim networkStream As NetworkStream = myClient.GetStream()
        If networkStream.CanWrite Then
            Dim sendBytes As [Byte]() = Encoding.ASCII.GetBytes(fileText + vbCrLf)
            networkStream.Write(sendBytes, 0, sendBytes.Length)
            myClient.Close()
        Else
            myClient.Close()
            MsgBox("Could not write data to data stream")
        End If
    End Sub
End Class

