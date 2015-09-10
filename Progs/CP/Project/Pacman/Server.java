package edu.cp.project.server;

// PacMen Server

import java.io.IOException;
import java.net.*;
import java.util.*;

public final class Server implements Runnable {
	private static final int PACKETSIZE = 100;
	public static void main(String[] args) {
		new Thread(new Server()).start();
	}

	public void run() {
		try {
			System.out.println("Hi there");
			serverSocket = new DatagramSocket(6789);
			System.out.println("Server socket started");
			DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE) ;
			registerClients(packet);
			board = new ClientsPositions(pacMenClients, ghostClients);
			// send initial configuration to clients
			broadcast(board.toString());
			listenAndRespondToClients(packet);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
		}	finally {
			serverSocket.close();
		}
	}

	private void registerClients(DatagramPacket packet) {
		for (int i = 0; i < NUMCLIENT; i++) {
			try {
				Arrays.fill(packet.getData(), (byte) 0);
				serverSocket.receive(packet);
				ClientInfo clientInfo = new ClientInfo(packet);
				map.put(clientInfo.addr.toString() + clientInfo.port, clientInfo);
				if (clientInfo.clientType == RegisterClientMessage.
						CLIENT_TYPE.GHOST) {
					ghostClients.add(clientInfo);
				} else {
					pacMenClients.add(clientInfo);
				}   
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
	}
	
	private void listenAndRespondToClients(DatagramPacket packet) 
			throws IOException {
		while (true) {
			Arrays.fill(packet.getData(), (byte) 0);
			serverSocket.receive(packet);
			String msg = new String(packet.getData()).trim();
			System.out.println("client> " + msg);
			if (PACMANWINS.equals(msg)) {
				broadcast(PACMANWINS);
			} else if (GHOSTWINS.equals(msg)) {
				broadcast(GHOSTWINS);
			} else {
				updateClientsPositionAndBroadcast(map.get(packet.getAddress().toString() 
						+ packet.getPort()), 
						msg);
			}
		}
	}
	
	private void updateClientsPositionAndBroadcast(ClientInfo 
			clientInfo,
			String clientMsg) {
		String boardState = board.updateClientPosition(clientInfo, clientMsg);
		if (boardState != null) {
			broadcast(boardState);
		}
	}

	private void broadcast(String boardState) {
		
		DatagramPacket sendPacket;
		byte[] sendData = boardState.getBytes();
		for (ClientInfo clientInfo : pacMenClients) {
			System.out.println(boardState + " sending to client");
			sendPacket = new DatagramPacket(sendData,
											sendData.length,
											clientInfo.addr,
											clientInfo.port);
			try {
				serverSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//clientInfo.outToClient.println(boardState);
		}
		for (ClientInfo clientInfo : ghostClients) {
			System.out.println(boardState + " sending to client");
			sendPacket = new DatagramPacket(sendData,
					sendData.length,
					clientInfo.addr,
					clientInfo.port);
			try {
				serverSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static List<ClientInfo> pacMenClients = new ArrayList<ClientInfo>();
	static List<ClientInfo> ghostClients = new ArrayList<ClientInfo>();
	static DatagramSocket serverSocket = null;
	static ClientsPositions board;
	Socket clientSocket;
	HashMap<String, ClientInfo> map = new HashMap<String, ClientInfo>();
	private static final int NUMCLIENT = 2;
	private static final String PACMANWINS = "PW";
	private static final String GHOSTWINS = "GW";
}
