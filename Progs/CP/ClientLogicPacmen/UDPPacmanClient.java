package edu.cp.project;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;

public class UDPPacmanClient extends JFrame implements KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DatagramSocket requestSocket;
	DatagramPacket sendPacket;
	DatagramPacket rcvPacket = new DatagramPacket(new byte[100], 100);
 	String message;
 	int port = 6789;
 	String serverIpAddr = "127.0.0.1";
 	static JFrame frame=new JFrame();
	UDPPacmanClient(){
		frame.addKeyListener(this);
	}
	void run()
	{
		try{
			//1. creating a socket to connect to the server
			requestSocket = new DatagramSocket();
			serverAddr = InetAddress.getByName(serverIpAddr);
			String sendMsg = new String("REG bcc P");
			byte[] sendData = sendMsg.getBytes();
			sendPacket = new DatagramPacket(sendData,
											sendData.length,
											serverAddr,
											port);
			requestSocket.send(sendPacket);
			requestSocket.receive(rcvPacket);
			message = new String(rcvPacket.getData()).trim();
			ClientBoard board=new ClientBoard(frame,message);
			new Thread(board).start();
			do{	
				requestSocket.receive(rcvPacket);
				message = new String(rcvPacket.getData());
				System.out.println("server>" + message);
				board.changeClientsState(message);
			}while(!message.equals("bye"));
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
	
	void sendMessage(String msg)
	{
		//System.out.println("client>" + msg);
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
		UDPPacmanClient client = new UDPPacmanClient();
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
			sendMessage("u");
		}
		if (ckey==40){
			//pacman.setX(x+1); //Down
			sendMessage("d");
		}
		if (ckey==37){
			//pacman.setY(y-1); //Right
			sendMessage("l");
		}
		if (ckey==39){
			//pacman.setY(y+1); //Left
			sendMessage("r");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("In keyReleased");
	}
	
	InetAddress serverAddr;

}