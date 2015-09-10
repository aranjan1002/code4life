package networks.project;

import java.net.*;
import java.io.*;

class ClientResponder implements Runnable {
    public void run() {
	try {
	    int cmd;
	    while (true) {
		while (UDPServer.needToSendPktsToClient() == false);
		System.out.println("Sending a packet to client:");
		DatagramPacket pkt = UDPServer.getPacketToSendToClient();
		System.out.println(new String(pkt.getData()).toString());
		UDPServer.
		    serverSocket.send(pkt);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}