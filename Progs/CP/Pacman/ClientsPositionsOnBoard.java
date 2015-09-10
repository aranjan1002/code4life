package edu.cp.project.server;

import java.util.*;
import java.util.List;


class ClientsPositionsOnBoard {
	private static final int PAC_SIZE = 30;
	ArrayList<Client> pacMenList = new ArrayList<Client>();
	ArrayList<Client> ghostList = new ArrayList<Client>();
	
    public ClientsPositionsOnBoard(List<ClientInfo> pacMenClients,
    		List<ClientInfo> ghostClients) {
    	for (ClientInfo pacMenClient : pacMenClients) {
    		Client cl = new Client(PAC_SIZE, PAC_SIZE, "s");
    		clientInfoToClient.put(pacMenClient, cl);
    		pacMenList.add(cl);
    	}
    	
    	for (ClientInfo ghostClient : ghostClients) {
    		Client cl = new Client(11 * PAC_SIZE, 11 * PAC_SIZE, "s");
    		clientInfoToClient.put(ghostClient, cl);
    		ghostList.add(cl);
    	}
    }
    	
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for (Client pacMen : pacMenList) {
    		sb.append(pacMen.toString());
    	}
    	sb.append("G").append(" ");
    	for (Client ghost : ghostList) {
    		sb.append(ghost.toString());
    	}
    	return sb.toString().trim();
    }
    
    public String updateClientPosition(ClientInfo clientInfo, String clientMsg) {
    	Client cl = clientInfoToClient.get(clientInfo);
    	String[] str = clientMsg.split(" ");
    	int idx = 0;
    	cl.x = Integer.parseInt(str[idx]);
		cl.y = Integer.parseInt(str[idx + 1]);
		cl.dir = str[idx + 2];
		cl.flag = true;
    	return toString();
    }
    
    class Client {
    	boolean flag = false;
    	int x = 0;
    	int y = 0;
    	String str = "* * * ";
    	Client(int x, int y, String d) {
    		this.x = x;
    		this.y = y;
    		dir = d;
    	}
    	
    	public String toString() {
    		StringBuilder sb = new StringBuilder();
    		//System.out.println("Clients dir: " + dir);
    		if (flag) {
    		sb.append(x)
    		.append(" ")
    		.append(y)
    		.append(" ")
    		.append(dir)
    		.append(" ");
    		flag = false;
    		return sb.toString();
    		}
    		else {
    			return str;
    		}
    	}
    	
    	String dir;
    }
   
    Map<ClientInfo, Client> clientInfoToClient = new HashMap<ClientInfo, Client>();
}
