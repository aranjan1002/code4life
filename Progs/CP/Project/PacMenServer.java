package edu.cp.project;

// PacMen Server

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.awt.Font;
import java.util.*;

public final class PacMenServer implements Runnable {
    public void run() {
	try {
	    serverSocket = new ServerSocket(6789);
	} catch (IOException e) {
	    System.err.println("Could not listen on port: 4444.");
	    System.exit(1);
	}
	
	// Waiting for 2 clients
	BufferedReader in = null;
	for (int i = 0; i < numClient; i++) {
	    try {
		clientSocket = serverSocket.accept();
		ClientInfo clientInfo = getClientRegistrationInfo(clientSocket);
		if (clientInfo.clientType == ClientMessageReader.
		    CLIENT_TYPE.GHOST) {
		    ghostClients.add(clientInfo);
		} else {
		    pacMenClients.add(clientInfo);
		}   
	    } catch (Exception e) {
		System.err.println(e.getMessage());
		System.exit(1);
	    }
	}
	// make the pacmen board
	board = new PacMenBoard(pacMenClients, ghostClients);
	
	for (ClientInfo clientInfo : pacMenClients) {
	    new Thread(new ClientListener(clientInfo)).start();
	}

	for (ClientInfo clientInfo : ghostClients) {
	    new Thread(new ClientListener(clientInfo)).start();
	}
    }

    static synchronized void moveClientOnBoardAndBroadcast(ClientInfo 
							   clientInfo,
							   ClientMessageReader.
							   MOVE_DIR moveDIR) {
	String boardState = board.move(clientInfo, moveDIR);
	if (boardState != null) {
	    broadcast(boardState);
	}
    }

    private static synchronized void broadcast(String boardState) {
	for (ClientInfo clientInfo : pacMenClients) {
	    clientInfo.outToClient.println(boardState);
	}
	for (ClientInfo clientInfo : ghostClients) {
	    clientInfo.outToClient.println(boardState);
	}
    }

    private ClientInfo getClientRegistrationInfo(Socket clientSocket) {
	try {
	    InputStreamReader isr =
		new InputStreamReader(clientSocket.getInputStream());
	    BufferedReader inFromClient = new BufferedReader(isr);
	    String clientMsg = inFromClient.readLine();
	    RegisterClientMessage registerClientMsg = 
		new RegisterClientMessage(clientMsg);
	    PrintWriter outToClient = 
		new PrintWriter(clientSocket.getOutputStream(), true);
	    String clientName = registerClientMsg.getClientName();
	    ClientInfo clientInfo = new ClientInfo(clientName, 
						   inFromClient,
						   outToClient,
						   registerClientMsg.
						   getClientType());
	    return clientInfo;
	} catch (Exception e) {
	    throw new RuntimeException(e.getMessage());
	}

    }

    static List<ClientInfo> pacMenClients = new ArrayList<ClientInfo>();
    static List<ClientInfo> ghostClients = new ArrayList<ClientInfo>();
    ServerSocket serverSocket = null;
    final int numClient = 2;
    ClientMessageReader clientMsgReader = new ClientMessageReader();
    static PacMenBoard board;
    Socket clientSocket;
}
