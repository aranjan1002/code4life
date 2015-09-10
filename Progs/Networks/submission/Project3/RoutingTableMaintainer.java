package networks.project;

import java.io.*;
import java.net.*;
import java.util.*;

class RoutingTableMaintainer implements Runnable {
    public void run() {
	while (true) {
	    ArrayList<Record> connectedServersList 
		= UDPServer.getConnectedServers();
	    for (Record record : connectedServersList) {
		Record recordToServer 
		    = new Record(CmdCode.TEST);
		DatagramPacket packetToServer 
		    = new DatagramPacket(recordToServer.getBytes(),
					 recordToServer.getBytes().length,
					 record.getIpAddr(),
					 record.getPort());
		UDPServer.sendToServer(packetToServer);
		}
	    try {
		Thread.sleep(30000);
	    }
	    catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    boolean hasRoutingTableChanged = false;
	    ArrayList<String> ackedServersList =
		UDPServer.getAckedServersList();
	    for (int i = 0; i < connectedServersList.size(); i++) {
		Record record = connectedServersList.get(i);
		String serverName = record.getName();
		if (ackedServersList.contains(serverName) == false) {
		    record.isConnected = false;
		    connectedServersList.remove(record);
		    UDPServer.removeFromLiveServersList(serverName);
		    UDPServer.routingTable.removeNeighbor(serverName);
		    hasRoutingTableChanged = true;
		    broadcast();
		}
		UDPServer.removeFromAckedServersList(serverName);
	    }
	    if (hasRoutingTableChanged == true) {
		broadcast();
		/*		Record recordToServer
		    = new Record(UDPServer
				 .routingTable
				 .toString(),
				 CmdCode.MERGE_ROUTING_TABLE);
		for (Record connectedServer : connectedServersList) {
		    DatagramPacket packetToServer
			= new DatagramPacket(recordToServer.getBytes(),
					     recordToServer.getBytes().length,
					     connectedServer.getIpAddr(),
					     connectedServer.getPort());
		    UDPServer.sendToServer(packetToServer);
		}
		}*/
	    }
	}
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

}