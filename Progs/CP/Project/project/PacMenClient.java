package edu.cp.project;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class PacMenClient implements KeyListener{
	int x;
	int y;
	static StringBuffer newkey = new StringBuffer();
	static StringBuffer previouskey = new StringBuffer();
	public PacMenClient() {
		x=0;
		y=0;
	}
	public static void main(String[] args) throws InterruptedException {
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			socket = new Socket("127.0.0.1", 6790);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (Exception e) {
			System.err.println("Socket connection failed!!");
			System.exit(1);
		}
		out.println("REG pacpac P");
		Thread boardThread = new ReadServer(in);
		boardThread.start();
		Thread keyThread = new WriteServer(out);
		keyThread.start();
		boardThread.join();
		keyThread.join();
		try {
			out.close();
			socket.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static class ReadServer extends Thread{
		BufferedReader in;
		String boardinfo;
		ReadServer(BufferedReader in){
			this.in=in;
		}
		public void run(){
			while(true){
				try {
					while (!in.ready()) {}
					boardinfo=in.readLine();
					//plotboard(boardinfo);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	static class WriteServer extends Thread{
		PrintWriter out = null;
		WriteServer(PrintWriter out){
			this.out=out;
		}
		public void run(){
			while(true){
				try {
					if (!newkey.equals(previouskey)){
						out.println(newkey);
						previouskey=newkey;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub11/21/12 Gmail - client
		https://mail.google.com/mail/u/0/?ui=2&ik=18a0b2df ed&v iew=pt&search=inbox&msg=13b265d15c5772a4 3/3
			System.out.println("In keyTyped");
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int ckey;
		System.out.println("In keyPressed");
		ckey = e.getKeyCode();
		if (ckey==38){
			//pacman.setX(x-1); //UP
			newkey=new StringBuffer("U");
		}
		if (ckey==40){
			//pacman.setX(x+1); //Down
			newkey=new StringBuffer("D");
		}
		if (ckey==37){
			//pacman.setY(y-1); //Right
			newkey=new StringBuffer("R");
		}
		if (ckey==39){
			//pacman.setY(y+1); //Left
			newkey=new StringBuffer("L");
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("In keyReleased");
	}
}