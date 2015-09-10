package networks.project;

import java.io.*;

class ClientResponder implements Runnable {
    public void run() {
	try {
	    int cmd;
	    while (true) {
		while (UDPServer.needToSendPktsToClient() == false);
		System.out.println("Sending a packet to client");
		UDPServer.
		    serverSocket.send(UDPServer.getPacketToSendToClient());
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}