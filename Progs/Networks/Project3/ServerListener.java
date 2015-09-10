package networks.project;

import java.io.*;
import java.net.*;
import java.util.*;

class ServerListener implements Runnable {
    public void run() {
        try {
	    int cmd;
	    while (true) {
		// System.out.println("Listening to server");
		rcvData = new byte[rcvData.length];
		rcvPkt = new DatagramPacket(rcvData, rcvData.length);
		UDPServer.server2ServerSocket.receive(rcvPkt);
		System.out.println("Received Packet: " +
				   new String(rcvPkt.getData()).toString());
		rec = new Record(rcvPkt.getData());
		cmd = rec.getCmd();
		System.out.println("CMD: " + cmd);
		
		if (cmd == CmdCode.ACKREQ) {
		    ackreq();
		}
		else if (cmd == CmdCode.ACK) {
		    ack();
		}
		else if (cmd == CmdCode.LIST) {
		    list();
		}
		else if (cmd == CmdCode.LISTACK) {
		    listack();
		}
		else if (cmd == CmdCode.INVALID_CMD) {
		    System.out.println("Invalid command");
		}
		else if (cmd == CmdCode.GETNAME) {
		    getName();
		}
		else if (cmd == CmdCode.SETNAME) {
		    setName();
		}
		else if (cmd == CmdCode.NEIGHBORS) {
		    neighbors();
		}
		else if (cmd == CmdCode.MERGE_ROUTING_TABLE) {
		    merge();
		}
		else if (cmd == CmdCode.FRWD) {
                    frwd();
		}
		else if (cmd == CmdCode.SEND) {
		    send();
		}
		else if (cmd == CmdCode.TEST) {
		    test();
		}
		else if (cmd == CmdCode.TESTACK) {
		    testAck();
		}
		else if (cmd == CmdCode.UNLINK) {
		    unlink();
		}
		else if (cmd == CmdCode.UNLINKACK) {
		    unlinkAck();
		}
		else {
		    System.out.println("Command not recognized");
		    rec.setCmd(CmdCode.INVALID_CMD);
		    sendResponse();
		}
	    }
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
	catch (NetworkProjectException e) {
	    e.printStackTrace();
	}
    }

    private void unlink() 
	throws IOException {
	disconnectServer();
	rec.setName(UDPServer.myName);
        rec.setCmd(CmdCode.UNLINKACK);
	sendResponse();
	broadcast();
    }

    private void unlinkAck() {
	disconnectServer();
	String sendData = "Server successfully unlinked";
	broadcast();
	sendToClient(sendData);
    }

    private void disconnectServer() {
	String serverName = rec.getName();
        if (UDPServer.isOnList(serverName) == false) {
            System.out.println("Fatal Error: server " + serverName +
                               " not on list");
            System.exit(1);
        }
        if (UDPServer.isServerLive(serverName) == false) {
            System.out.println("Fatal Error: server " + serverName +
                               "is not live, hence cannot unlink");
            System.exit(1);
        }
        Record serverRecord = UDPServer.getFromList(serverName);
        serverRecord.isConnected = false;
        UDPServer.removeFromLiveServersList(serverName);
        UDPServer.routingTable.removeNeighbor(serverName);
    }

    void sendToClient(String sendData) {
	DatagramPacket pktToClient = 
	    new DatagramPacket(sendData.getBytes(),
			       sendData.getBytes().length,
			       rec.getIpAddr(),
			       rec.getPort());
	UDPServer.sendToClient(pktToClient);
    }

    /*private void link() 
	throws IOException {
	String srvname = rec.getName();
	if (rec2.isConnected == false) {
	    rec.setCmd(CmdCode.ACK);
	    sendResponse();
	} 
	else {
	    throw new IOException("Invalid link cmd");
	}
	}*/

    private void ack() 
	throws IOException {
	String serverName = rec.getWildCardIp();
        if (UDPServer.isOnList(serverName) == false) {
            throw new IOException("srvname not in the list: " + serverName);
        }
        Record rec2 = UDPServer.getFromList(serverName);
        if (rec2.isConnected == false) {
            rec2.isConnected = true;
            String sendData = "Success: linked to server";
	    UDPServer.routingTable.insertNeighbor(serverName);
	    UDPServer.routingTable.mergeAndGetHasStateChanged
		(new RoutingTable(rec.getName()),
		 serverName);
	    UDPServer.addToLiveServersList(serverName);
	    broadcast();
            //System.out.println(sendData);
	    System.out.println("My new routing table after linking: "
			       + UDPServer.routingTable.toString());
            DatagramPacket sendPkt2Client =
                new DatagramPacket(sendData.getBytes(),
                                   sendData.length(),
                                   rec.getIpAddr(),
                                   rec.getPort());
            UDPServer.sendToClient(sendPkt2Client);
        }
        else {
            throw new IOException("not a valid ack");
        }
    }
    
    private void sendToClient(ClientInfo cl, String message) {
        DatagramPacket pkt =  new DatagramPacket(message.getBytes(),
                                                 message.getBytes().length,
                                                 cl.addr,
                                                 cl.portNumber);
        UDPServer.sendToClient(pkt);
    }

    private void send() {
	String clNames = rec.getName();
	String message = rec.getWildCardIp();
	String[] split = clNames.split(" ");
	String sendData = "CNT " + split[0] + " From.server." + UDPServer.myName + ": ";
	sendData += "Message.sent.to:";
	String notSentTo = new String();
	clNames = split[1];
	if (clNames.equals("*")) {
	    sendData += sendToAllClients(message);
	}
	else {
	    clNames = clNames.replaceAll("\"", "");
	    String[] splits = clNames.split(":");
	    for (String sp : splits) {
		if (UDPServer.isClientRegistered(sp)) {
		    sendToDestinationClients(UDPServer.
					     getClient(sp), 
					     message,
					     sp);
		    sendData += sp;
		}
		else {
		    notSentTo += sp;
		}
	    }
	}
	if (notSentTo.length() != 0) {
	    sendData += " Clients.not.found:" + notSentTo;
	}
	sendToSourceClient(sendData);
    }

    private String sendToAllClients(String message) {
	String messageSentTo = new String();
        ArrayList<String> registeredClients =
            UDPServer.getRegisteredClientNames();
        for (String registeredClient : registeredClients) {
            sendToDestinationClients(UDPServer
				     .getClient(registeredClient), 
				     message,
				     registeredClient);
	    messageSentTo += registeredClient + ",";
	}
	return messageSentTo;
    }

    private void sendToDestinationClients(ClientInfo cl, 
					  String message, 
					  String registeredClient) {
        DatagramPacket pkt =  new DatagramPacket(message.getBytes(),
                                                 message.getBytes().length,
                                                 cl.addr,
                                                 cl.portNumber);
        System.out.println("Sending message: " +
                           message + " " +
                           cl.addr + " " +
                           cl.portNumber);
        UDPServer.sendToClient(pkt);
	//String sendData = "Message sent to client: " + registeredClient;
	//sendToSourceClient(sendData);
    }

    private void sendToSourceClient(String sendData) {
	DatagramPacket sendPkt2Client =
            new DatagramPacket(sendData.getBytes(),
                               sendData.getBytes().length,
                               rec.getIpAddr(),
                               rec.getPort());
        UDPServer.sendToClient(sendPkt2Client);
    }

    private void ackreq() 
	throws IOException {
	System.out.println("Sending ack");
	String serverName = rec.getWildCardIp();
	System.out.println("linking with " + serverName);
	UDPServer.routingTable.insertNeighbor(serverName);
	UDPServer.routingTable.mergeAndGetHasStateChanged
	    (new RoutingTable(rec.getName()),
	     rec.getWildCardIp());
	Record record = new Record(serverName,
				   rcvPkt.getAddress(),
				   rcvPkt.getPort());
	record.isConnected = true;
	UDPServer.addToList(serverName, record);
	
	UDPServer.addToLiveServersList(serverName);
	rec.setWcIp(UDPServer.myName);
	System.out.println("My new routing table: " 
			   + UDPServer.routingTable.toString());
	rec.setName(UDPServer.routingTable.toString());
	rec.setCmd(CmdCode.ACK);
	sendResponse();
	broadcast();
    }

    private void broadcast() {
	ArrayList<String> list = UDPServer.getLiveServersList();
	String s = UDPServer.routingTable.toString();
	Record rSend = new Record(s,
				  UDPServer.myName,
				  CmdCode.MERGE_ROUTING_TABLE);
	System.out.println("Broadcast to list: " + list);
	for (String name : list) {
	    System.out.print("Broadcasting to " + name + ":");
	    System.out.println(new String(rSend.getBytes()).toString());
	    Record r = UDPServer.getFromList(name);
	    DatagramPacket pkt = new DatagramPacket(rSend.getBytes(),
						    rSend.getBytes().length,
						    r.getIpAddr(),
						    r.getPort());
	    UDPServer.sendToServer(pkt);
	}
    }

    private void merge() {
	RoutingTable toMerge = new RoutingTable(rec.getName());
	System.out.println("merging with " + toMerge.toString());
	boolean change = UDPServer.routingTable
	    .mergeAndGetHasStateChanged(toMerge, rec.getWildCardIp());
	System.out.println("change = " + change);
	if (change == true) {
	    broadcast();
	}
    }

    private void getName() 
	throws IOException {
	rec.setName(UDPServer.myName);
        rec.setCmd(CmdCode.SETNAME);
        sendResponse();
    }

    private void setName() {
	UDPServer.addToList(rec.getName(), rec);
    }

    private void list() 
	throws IOException {
	System.out.println("Sending list");
	ArrayList<String> clientsList = UDPServer.
            getRegisteredClientNames();
	//String name = rec.getName();
	String sendData = new String();
	String[] splits = rec.getName().split(" ");
	sendData += "Cnt " + splits[0];
	String clNames = splits[1];
	if (clNames.equals("*")) {
	    for (String nameCl : clientsList) {
		sendData += " " + UDPServer.myName + ":" + nameCl;
	    }
	}
	else {
	    clNames = clNames.replaceAll("\"", "");
	    String[] splits2 = clNames.split(":");
	    for (String sp : splits2) {
		if (UDPServer.isClientRegistered(sp)) {
		    sendData += " " + UDPServer.myName + sp;
		}
	    }
	}
	rec.setName(sendData);
	rec.setCmd(CmdCode.LISTACK);
	sendResponse();
    }

    private void test() 
	throws IOException {
	rec.setName(UDPServer.myName);
        rec.setCmd(CmdCode.TESTACK);
        sendResponse();
    }

    private void testAck() 
	throws IOException {
	UDPServer.addToAckedServersList(rec.getName());
    }

    private void listack()
	throws IOException {
        System.out.println("Sending list ack");
	DatagramPacket sendPkt2Client =
	    new DatagramPacket(rec.getName().getBytes(),
			       rec.getName().getBytes().length,
			       rec.getIpAddr(),
			       rec.getPort());
	UDPServer.sendToClient(sendPkt2Client);
    }

    private void sendResponse()
        throws IOException {
	//System.out.println ("Sending: " + rec.getCmd() +
	//		    "to address: " + rcvPkt.getAddress() +
	//		    "to port: " + rcvPkt.getPort());
	
        DatagramPacket sendPkt2Server =
	    new DatagramPacket(rec.getBytes(),
			       rec.getBytes().length,
			       rcvPkt.getAddress(),
			       rcvPkt.getPort());
        UDPServer.sendToServer(sendPkt2Server);
    }

    private void frwd() 
	throws IOException {
	//System.out.println("getting table");
        String rt = UDPServer.routingTable.toString();
        
        String result = "Server Name "
            + UDPServer.myName
	    + "|"
	    + rt;

        DatagramPacket sendPkt2Client =
            new DatagramPacket(result.getBytes(),
                               result.length(),
                               rec.getIpAddr(),
                               rec.getPort());
        UDPServer.sendToClient(sendPkt2Client);
    }

    private void neighbors() 
	throws IOException {
	//System.out.println("getting neighbors");
	String neighborsList = new String();
	ArrayList<String> list = UDPServer.getLiveServersList();
	for (String name : list) {
	    Record r = UDPServer.getFromList(name);
            neighborsList += r.getName() + "-" + r.getIpAddr() + "-" + 
		r.getPort() + "|";
        }
	String result = "Server Name "
            + UDPServer.myName
            + " neighbors |"
	    + neighborsList;

	DatagramPacket sendPkt2Client =
            new DatagramPacket(result.getBytes(),
                               result.length(),
                               rec.getIpAddr(),
                               rec.getPort());
        UDPServer.sendToClient(sendPkt2Client);
    }
    
    byte[] rcvData = new byte[1024];
    DatagramPacket rcvPkt;
    Record rec;
}