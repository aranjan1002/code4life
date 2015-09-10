package edu.cp.project;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class UDPPacMenBoard {
	ArrayList<Client> pacMenList = new ArrayList<Client>();
	ArrayList<Client> ghostList = new ArrayList<Client>();
	
    public UDPPacMenBoard(List<UDPClientInfo> pacMenClients,
    		List<UDPClientInfo> ghostClients) {
    	for (UDPClientInfo pacMenClient : pacMenClients) {
    		Client cl = new Client("s");
    		clientInfoToClient.put(pacMenClient, cl);
    		pacMenList.add(cl);
    	}
    	
    	for (UDPClientInfo ghostClient : ghostClients) {
    		Client cl = new Client("s");
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
    
    public String move(UDPClientInfo clientInfo, String clientMsg) {
    	Client cl = clientInfoToClient.get(clientInfo);
    	cl.dir = clientMsg;
    	return toString();
    }
    
    class Client {
    	Client(String d) {
    		dir = d;
    	}
    	
    	public String toString() {
    		return dir + " ";
    	}
    	
    	String dir;
    }
   
    Map<UDPClientInfo, Client> clientInfoToClient = new HashMap<UDPClientInfo, Client>();
}
