package edu.cp.project;

import java.io.*;

public class ClientListener implements Runnable {
    ClientListener(ClientInfo clInfo) {
	clientInfo = clInfo;
    }

    public void run() {
	try {
	    while(!clientInfo.inFromClient.ready()) {
	    
		String clientMsg = clientInfo.inFromClient.readLine();
		MoveClientPositionMessage movClientMsg =
		    new MoveClientPositionMessage(clientMsg);
		ClientMessageReader.MOVE_DIR moveDIR = 
		    movClientMsg.getMovementDirection();
		PacMenServer.moveClientOnBoardAndBroadcast(clientInfo, 
							   moveDIR);
	    }
	} catch (Exception e) {
	    throw new RuntimeException(e.getMessage());
	}
    }

    ClientInfo clientInfo;

}