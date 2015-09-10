package networks.project;

import java.io.*;
import java.net.*;
import java.util.*;

class ClientSideListener implements Runnable {
    public void run() {
	try {
	    int cmd;
	    while (true) {
		rcvPkt = new DatagramPacket(rcvData, rcvData.length);
                UDPClient.clientReceiverSocket.receive(rcvPkt);
		System.out.println("Received packet on client side");
                String message = new String(rcvPkt.getData()).trim();
		System.out.println("Received Message: " + message);
	    }
	}
	catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    byte[] rcvData = new byte[1024];
    DatagramPacket rcvPkt, sendPkt;
    String sendData;
    Record rec;
}