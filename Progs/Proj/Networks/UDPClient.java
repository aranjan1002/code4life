package networks.project;

import java.io.*;
import java.net.*;
import java.util.*;

class UDPClient {
    UDPClient(InetAddress svrIpAddr, int svrPrt)
	throws SocketException, UnknownHostException {
	clientSocket = new DatagramSocket();
	this.srvIp = svrIpAddr;
	this.srvPort = svrPrt;
    }

    void test() 
	throws IOException, NetworkProjectException {
	Record rec = new Record(CmdCode.TEST);
	sendData = rec.getBytes();
        sendPacket();
        receiveResponse(5000);
    }

    void kill() 
	throws IOException, NetworkProjectException {
	Record rec = new Record(CmdCode.KILL);
	sendPacketAndReceiveResp(rec);
    }

    void delete(String name, InetAddress ipAddr, int port)
	throws IOException, NetworkProjectException {
	Record rec = new Record(name, ipAddr, port, CmdCode.DELETE);
	sendPacketAndReceiveResp(rec);
    }

    void delete(String name)
	throws IOException, NetworkProjectException {
	Record rec = new Record(name, CmdCode.DELETE);
	sendPacketAndReceiveResp(rec);
    }

    void insert(String name, InetAddress ipAddr, int port) 
	throws IOException, NetworkProjectException {
	Record rec = new Record(name, ipAddr, port, CmdCode.INSERT);
	sendPacketAndReceiveResp(rec);
    }

    void find(String name, String wcIp) 
	throws IOException, NetworkProjectException {
	Record rec = new Record(name, wcIp, CmdCode.FIND);
	sendPacketAndReceiveRespIteratively(rec);
    }

    void link(String srvname) 
	throws IOException, NetworkProjectException {
	Record rec = new Record(srvname, CmdCode.LINK);
	sendPacketAndReceiveResp(rec);
    }

    void unlink(String srvname) 
	throws IOException, NetworkProjectException {
	Record rec = new Record(srvname, CmdCode.UNLINK);
	sendPacketAndReceiveResp(rec);
    }

    void register(String name, int port) 
	throws IOException, NetworkProjectException {
	Record rec = new Record(name, null, port, CmdCode.REGISTER);
	sendPacketAndReceiveResp(rec);
    }

    void unregister(String name)
        throws IOException, NetworkProjectException {
        Record rec = new Record(name, CmdCode.UNREGISTER);
        sendPacketAndReceiveResp(rec);
    }

    void list(String list) 
	throws IOException, NetworkProjectException {
        Record rec = new Record(list, CmdCode.LIST);
	sendData = rec.getBytes();
        sendPacket();
	receiveResponseIteratively(23);
    }

    void sendPacketAndReceiveRespIteratively(Record rec) 
	throws NetworkProjectException, IOException {
	sendData = rec.getBytes();
	sendPacket();
	receiveResponseIteratively();
    }
    
    void receiveResponseIteratively()
        throws NetworkProjectException, IOException {
        String response = new String();
	do {
	    rcvData = new byte[1024];
	    rcvPkt = new DatagramPacket(rcvData, rcvData.length);
	    clientSocket.receive(rcvPkt);
	    response = new String(rcvPkt.getData()).trim();
	    if (response.startsWith("name")) {
		System.out.println(response);
	    }
	    else if (response.equals("Success")) {
		break;
	    }
	    else {
		throw new NetworkProjectException(response);
	    }
	} while (true);
    }

    void receiveResponseIteratively(int x) 
	throws NetworkProjectException, IOException {
        String response = new String();
        do {
            rcvData = new byte[1024];
            rcvPkt = new DatagramPacket(rcvData, rcvData.length);
            clientSocket.receive(rcvPkt);
            response = new String(rcvPkt.getData()).trim();
	    // System.out.println(str);
	    String[] strSplit = response.split(" ");
	    if (z == 0) {
		z = Integer.parseInt(strSplit[1]) - 1;
	    }
	    else z--;
	    int l = strSplit.length;
	    for (int i = 2; i < strSplit.length; i++) {
		System.out.println(strSplit[i]);
	    }
        } while (z > 0);
    }
    
    private static int z = 0;
    void sendPacketAndReceiveResp(Record rec) 
	throws IOException, NetworkProjectException {
	sendData = rec.getBytes();
	sendPacket();
	receiveResponse();
    }

    /*void copyToSendData(byte[] recBytes, byte cmd_code) {
	int l = recBytes.length;
	sendData = new byte[l + 1];
	for (int i = 0; i < l; i++) {
	    sendData[i] = recBytes[i];
	}
	sendData[l] = cmd_code;
	System.out.println(new String(sendData));
    }
    */

    void sendPacket() 
	throws IOException {
	// System.out.println("Sending: " + new String(sendData));
        sendPkt = new DatagramPacket(sendData,
                                     sendData.length,
                                     srvIp,
                                     srvPort);
        clientSocket.send(sendPkt);
    }
    
    void receiveResponse()
	throws NetworkProjectException, IOException {
	rcvData = new byte[1024];
	rcvPkt = new DatagramPacket(rcvData, rcvData.length);
	// System.out.println("receiving response");
        clientSocket.receive(rcvPkt);

        String response = new String(rcvPkt.getData()).trim();
        if (!response.startsWith("Success")) {
            throw new NetworkProjectException(response);
        }
        else {
            System.out.println("Server Responded: " + response);
	    System.out.println();
	    System.out.println();
        }
    }

    void receiveResponse(int timeout) 
	throws NetworkProjectException, IOException {
	clientSocket.setSoTimeout(timeout);
	try {
	    receiveResponse();
	    clientSocket.setSoTimeout(0);
	} catch (SocketTimeoutException e) {
	    clientSocket.setSoTimeout(0);
	    throw new NetworkProjectException(e.getMessage());
	}
    }

    void close() {
	if (clientSocket != null) {
	    clientSocket.close();
	}
    }

    DatagramSocket clientSocket;
    InetAddress srvIp;
    int srvPort;
    DatagramPacket rcvPkt, sendPkt;
    byte[] sendData;
    byte[] rcvData = new byte[1024];
}