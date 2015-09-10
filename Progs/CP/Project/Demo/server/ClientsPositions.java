package edu.cp.project.server;

import java.util.*;
import java.util.List;


class ClientsPositions {
	private static final int PAC_SIZE = 30;
	ArrayList<ClientPos> pacMenList = new ArrayList<ClientPos>();
	ArrayList<ClientPos> ghostList = new ArrayList<ClientPos>();
	
    public ClientsPositions(List<ClientInfo> pacMenClients,
    		List<ClientInfo> ghostClients) {
    	for (ClientInfo pacMenClient : pacMenClients) {
    		ClientPos cl = new ClientPos(PAC_SIZE, PAC_SIZE, "s", "s");
    		clientInfoToClient.put(pacMenClient, cl);
    		pacMenList.add(cl);
    	}
    	
    	for (ClientInfo ghostClient : ghostClients) {
    		ClientPos cl = new ClientPos(11 * PAC_SIZE, 11 * PAC_SIZE, "s", "s");
    		clientInfoToClient.put(ghostClient, cl);
    		ghostList.add(cl);
    	}
    }
    	
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for (ClientPos pacMen : pacMenList) {
    		sb.append(pacMen.toString());
    	}
    	sb.append("G").append(" ");
    	for (ClientPos ghost : ghostList) {
    		sb.append(ghost.toString());
    	}
    	return sb.toString().trim();
    }
    
    public String updateClientPosition(ClientInfo clientInfo, String clientMsg) {
    	ClientPos cl = clientInfoToClient.get(clientInfo);
    	String[] str = clientMsg.split(" ");
    	int idx = 0;
    	cl.x = Integer.parseInt(str[idx]);
		cl.y = Integer.parseInt(str[idx + 1]);
		cl.currd = str[idx + 2];
		cl.newd = str[idx + 3];
		cl.flag = true;
    	return toString();
    }
    
    class ClientPos {
    	boolean flag = false;
    	int x = 0;
    	int y = 0;
    	String currd;
    	String newd;
    	String str = "* * * * ";
    	ClientPos(int x, int y, String cd, String nd) {
    		this.x = x;
    		this.y = y;
    		currd = cd;
    		newd = nd;
    	}
    	
    	public String toString() {
    		StringBuilder sb = new StringBuilder();
    		
    		if (flag) {
    		sb.append(x)
    		.append(" ")
    		.append(y)
    		.append(" ")
    		.append(currd)
    		.append(" ")
    		.append(newd)
    		.append(" ");

    		flag = false;
    		return sb.toString();
    		}
    		else {
    			return str;
    		}
    	}
    }
   
    Map<ClientInfo, ClientPos> clientInfoToClient = new HashMap<ClientInfo, ClientPos>();
}
