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
		rcvPkt = new DatagramPacket(rcvData, rcvData.length);
		UDPServer.server2ServerSocket.receive(rcvPkt);
		rec = new Record(rcvPkt.getData());
		cmd = rec.getCmd();
		System.out.println("CMD" + cmd);
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
		else {
		    System.out.println("Command not recognized");
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
	String srvname = rec.getName();
        if (UDPServer.isOnList(srvname) == false) {
            throw new IOException("srvname not in the list");
        }
        Record rec2 = UDPServer.getFromList(srvname);
        if (rec2.isConnected == false) {
            rec2.isConnected = true;
            String sendData = "Success: linked to server";
            System.out.println(sendData);
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

    private void ackreq() 
	throws IOException {
	System.out.println("Sending ack");
	rec.setCmd(CmdCode.ACK);
	sendResponse();
    }

    private void list() 
	throws IOException {
	System.out.println("Sending list");
	ArrayList<String> clientsList = UDPServer.
            getRegisteredClientNames();
	String name = rec.getName();
	String sendData = new String();
	sendData += "Cnt " + rec.getWildCardIp();
        for (String nameCl : clientsList) {
            sendData += " " + name + ":" + nameCl;
        }
	rec.setName(sendData);
	rec.setCmd(CmdCode.LISTACK);
	sendResponse();
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
	System.out.println ("Sending: " + rec.getCmd());
	
        DatagramPacket sendPkt2Server =
	    new DatagramPacket(rec.getBytes(),
			       rec.getBytes().length,
			       rcvPkt.getAddress(),
			       rcvPkt.getPort());
        UDPServer.sendToServer(sendPkt2Server);
    }

    byte[] rcvData = new byte[1024];
    DatagramPacket rcvPkt;
    Record rec;
}