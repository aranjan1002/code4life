package edu.cp.project;

import java.io.PrintWriter;
import java.io.BufferedReader;

class ClientInfo {
    ClientInfo(String clName,
               BufferedReader inFrmCl,
               PrintWriter outToCl,
               ClientMessageReader.CLIENT_TYPE clType) {
        clientName = clName;
        inFromClient = inFrmCl;
        outToClient = outToCl;
        clientType = clType;
    }

    public String toString() {
    	return clientName + " " + clientType;
    }
    
    String clientName;
    BufferedReader inFromClient;
    PrintWriter outToClient;
    ClientMessageReader.CLIENT_TYPE clientType;
}
