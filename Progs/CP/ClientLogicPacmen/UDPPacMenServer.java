package edu.cp.project;

// PacMen Server

import java.io.IOException;
import java.net.*;
import java.util.*;

public final class UDPPacMenServer implements Runnable {
	private static final int PACKETSIZE = 100;
	public static void main(String[] args) {
		new Thread(new UDPPacMenServer()).start();

	}

	public void run() {
		try {
			System.out.println("Hi there");
			serverSocket = new DatagramSocket(6789);
			System.out.println("UDPServer2 socket started");
			DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE) ;
			for (int i = 0; i < numClient; i++) {
				try {
					serverSocket.receive(packet);
					UDPClientInfo clientInfo = new UDPClientInfo(packet);
					map.put(clientInfo.addr.toString() + clientInfo.port, clientInfo);
					if (clientInfo.clientType == ClientMessageReader.
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

			board = new UDPPacMenBoard(pacMenClients, ghostClients);
			broadcast(board.toString());

			while (true) {
				Arrays.fill(packet.getData(), (byte) 0);
				serverSocket.receive(packet);
				String msg = new String(packet.getData()).trim();
				System.out.println("client> " + msg);
				moveClientOnBoardAndBroadcast(map.get(packet.getAddress().toString() 
							+ packet.getPort()), 
							msg);
			}

		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
		}	finally {
			serverSocket.close();
		}
	}

	static synchronized void moveClientOnBoardAndBroadcast(UDPClientInfo 
			clientInfo,
			String clientMsg) {
		String boardState = board.move(clientInfo, clientMsg);
		if (boardState != null) {
			broadcast(boardState);
		}
	}

	private static synchronized void broadcast(String boardState) {
		//System.out.println(boardState + " sending to client");
		DatagramPacket sendPacket;
		byte[] sendData = boardState.getBytes();
		for (UDPClientInfo clientInfo : pacMenClients) {
			
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
		for (UDPClientInfo clientInfo : ghostClients) {
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

	

	static List<UDPClientInfo> pacMenClients = new ArrayList<UDPClientInfo>();
	static List<UDPClientInfo> ghostClients = new ArrayList<UDPClientInfo>();
	static DatagramSocket serverSocket = null;
	final int numClient = 2;
	ClientMessageReader clientMsgReader = new ClientMessageReader();
	static UDPPacMenBoard board;
	Socket clientSocket;
	HashMap<String, UDPClientInfo> map = new HashMap<String, UDPClientInfo>();
}
