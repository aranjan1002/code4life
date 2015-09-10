package edu.cp.project.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Client extends JFrame implements KeyListener{
	Client(){
		frame.addKeyListener(this);
		frame.setLocation(400,100);
		REGMSG = null;
		clientType = null;
	}
	void run()
	{
		try{
			requestSocket = new DatagramSocket();
			serverAddr = InetAddress.getByName(serverIpAddr);
			registerWithServer();
			getIdFromServer();
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
		}
	}

	private void listenToServer() throws IOException {
		do{	
			Arrays.fill(rcvPacket.getData(), (byte) 0);
			requestSocket.receive(rcvPacket);
			message = new String(rcvPacket.getData()).trim();
			System.out.println("server>" + message);
			if (PACMANWINS.equals(message)) {
				JOptionPane.showMessageDialog(frame, "GAME OVER Pac-Man wins!!");
				board.gameState = 1;
				break;
			} else if(GHOSTWINS.equals(message)) {
				JOptionPane.showMessageDialog(frame, "GAME OVER Ghosts wins!!");
				board.gameState = 2;
				break;
			} else {
				// create new thread so that we keep listening to server as
				// board is being updated
				new Thread() {
					public void run() {
						board.changeClientsState(message);
					}
				}.start();
			}
		} while(true);
	}

	void registerWithServer() throws IOException {
		byte[] sendData = REGMSG.getBytes();
		sendPacket = new DatagramPacket(sendData,
				sendData.length,
				serverAddr,
				port);
		requestSocket.send(sendPacket);
	}

	void getIdFromServer() throws IOException {
		requestSocket.receive(rcvPacket);
		message = new String(rcvPacket.getData()).trim();
		myClientIdx = Integer.parseInt(message);
	}

	private void receiveInitialBoardStateAndInitializeBoard() throws IOException {
		requestSocket.receive(rcvPacket);
		message = new String(rcvPacket.getData()).trim();
		System.out.println("server >" + message);
		board=new Board(frame,message);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initializeBoardUI();
			}
		});
	}

	static void sendToServer(String msg)
	{
		DatagramPacket sendPacket;
		System.out.println("client>"+msg);
		byte[] sendData = msg.getBytes();
		sendPacket = new DatagramPacket(sendData,
				sendData.length,
				serverAddr,
				port);
		try {
			requestSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeBoardUI() {
		new Thread(board).start();

	}

	public static void main(String args[])
	{
		Client client = new Client();
		client.run();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void  keyPressed(KeyEvent e) {
		int ckey;
		ckey = e.getKeyCode();
		if (ckey==38){ 
            //UP
			if (lastKeyPressed != 1) {
				prependClientInfoAndSendToServer("u");
				lastKeyPressed = 1;
			}
		}
		if (ckey==40){
			//Down
			if (lastKeyPressed != 2) {
				prependClientInfoAndSendToServer("d");
				lastKeyPressed = 2;
			}
		}
		if (ckey==37){
			//Left
			if (lastKeyPressed != 3) {
				prependClientInfoAndSendToServer("l");
				lastKeyPressed = 3;
			}
		}
		if (ckey==39){
			//Right
			if (lastKeyPressed != 4) {
				prependClientInfoAndSendToServer("r");
				lastKeyPressed = 4;
			}
		}
	}

	public void prependClientInfoAndSendToServer(String msg) {
		StringBuilder sb = new StringBuilder();
		Board.COWClient.ClientPosition cl = board.getMyClientPosition(myClientIdx, clientType);
		sb.append(cl.getX())
		.append(" ")
		.append(cl.getY())
		.append(" ")
		.append(cl.getCurrDir())
		.append(" ")
		.append(msg);
		msg = sb.toString();
		sendToServer(msg);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	private static final long serialVersionUID = 1L;
	static DatagramSocket requestSocket;
	DatagramPacket sendPacket;
	DatagramPacket rcvPacket = new DatagramPacket(new byte[100], 100);
	String message;
	static int port = 6790;
	String serverIpAddr = "127.0.0.1";
	protected static String REGMSG;
	protected static String clientType;
	static JFrame frame=new JFrame();
	static InetAddress serverAddr;
	Board board;
	int myClientIdx = 0;
	static final String PACMANWINS = "PW";
	static final String GHOSTWINS = "GW";
	int lastKeyPressed = 0;
}