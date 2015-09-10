package edu.cp.project.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.util.Arrays;

import javax.swing.JFrame;

public class PacmanClient extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;
	DatagramSocket requestSocket;
	DatagramPacket sendPacket;
	DatagramPacket rcvPacket = new DatagramPacket(new byte[100], 100);
	String message;
	int port = 6789;
//	String serverIpAddr = "192.168.1.124";
	String serverIpAddr = "128.227.205.227";
	private static final String REGMSG = "REG pac P";
	static JFrame frame=new JFrame();
	PacmanClient(){
		frame.addKeyListener(this);
	}
	void run()
	{
		try{
			//1. creating a socket to connect to the server
			requestSocket = new DatagramSocket();
			serverAddr = InetAddress.getByName(serverIpAddr);
			registerWithServer();
			receiveInitialBoardStateAndInitializeBoard();
			listenToServer();
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			requestSocket.close();
			System.exit(0);
		}
	}

	private void listenToServer() throws IOException {
		do{	
			Arrays.fill(rcvPacket.getData(), (byte) 0);
			requestSocket.receive(rcvPacket);
			message = new String(rcvPacket.getData());
			System.out.println("server>" + message);
			if (PACMANWINS.equals(message)) {
				board.gameState = 1;
			} else if(GHOSTWINS.equals(message)) {
				board.gameState = 2;
			} else {
				// create new thread so that we keep listening to server as
				// board is being updated
				new Thread() {
						public void run() {
							board.changeClientsState(message);
						}
				}.start();
			}
		} while(board.gameState == 0);
	}

	void registerWithServer() throws IOException {
		byte[] sendData = REGMSG.getBytes();
		sendPacket = new DatagramPacket(sendData,
				sendData.length,
				serverAddr,
				port);
		requestSocket.send(sendPacket);
	}

	private void receiveInitialBoardStateAndInitializeBoard() throws IOException {
		requestSocket.receive(rcvPacket);
		message = new String(rcvPacket.getData()).trim();
		board=new Board(frame,message);
		new Thread(board).start();
	}

	private void sendToServer(String msg)
	{
		//System.out.println("client>" + msg);
		if (board.gameState == 1) {
			msg = PACMANWINS;
		} else if (board.gameState == 2) {
			msg = GHOSTWINS;
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(board.getPacmanClientX(myClientIdx))
			.append(" ")
			.append(board.getPacmanClientY(myClientIdx))
			.append(" ")
			.append(msg);
			msg = sb.toString();
		}
		System.out.println("client>"+msg);
		byte[] sendData = msg.getBytes();
		sendPacket = new DatagramPacket(sendData,
				sendData.length,
				serverAddr,
				port);
		try {
			requestSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String args[])
	{
		PacmanClient client = new PacmanClient();
		client.run();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("In keyTyped");
	}

	@Override
	public void  keyPressed(KeyEvent e) {
		int ckey;
		//System.out.println("In keyPressed");
		ckey = e.getKeyCode();
		if (ckey==38){
			//pacman.setX(x-1); //UP
			if (lastKeyPressed != 1) {
				sendToServer("u");
				lastKeyPressed = 1;
			}
		}
		if (ckey==40){
			//pacman.setX(x+1); //Down
			if (lastKeyPressed != 2) {
				sendToServer("d");
				lastKeyPressed = 2;
			}
		}
		if (ckey==37){
			//pacman.setY(y-1); //Right
			if (lastKeyPressed != 3) {
				sendToServer("l");
				lastKeyPressed = 3;
			}
		}
		if (ckey==39){
			//pacman.setY(y+1); //Left
			if (lastKeyPressed != 4) {
				sendToServer("r");
				lastKeyPressed = 4;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("In keyReleased");
	}

	InetAddress serverAddr;
	Board board;
	int myClientIdx = 0;
	private static final String PACMANWINS = "PW";
	private static final String GHOSTWINS = "GW";
	int lastKeyPressed = 0;
}