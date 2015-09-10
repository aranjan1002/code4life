package edu.cp.project.server;

import java.net.DatagramPacket;
import java.net.InetAddress;

class ClientInfo {
    ClientInfo(DatagramPacket pkt) {
    	addr = pkt.getAddress();
    	port = pkt.getPort();
    	String clientMsg = new String(pkt.getData());
    	System.out.println("Client msg: " + clientMsg);
    	RegisterClientMessage registerClientMsg = 
    			new RegisterClientMessage(clientMsg.trim());
    	clientName = registerClientMsg.clientName;
    	clientType = registerClientMsg.clientType;
    }

    public String toString() {
    	return clientName + " " + clientType;
    }
    
    String clientName;
    RegisterClientMessage.CLIENT_TYPE clientType;
    InetAddress addr;
    int port;
}
