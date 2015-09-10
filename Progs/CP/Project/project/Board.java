package edu.cp.project;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

public class Board extends JFrame implements KeyListener{
	static final int MAX_X = 400; // widest the playing screen can be
	static final int MAX_Y = 420; // tallest the playing screen can be

	int[][] mazeArray = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						  { 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0 },
						  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						  { 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 },
						  { 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0 },
						  { 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0 },
						  { 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0 },
						  { 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0 },
						  { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
						  { 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
						  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						  { 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0 },
						  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	static final int MAZE_SIZE = 13;
	static final int PAC_SIZE = 30;
	
	PacMan pacman;
	BoardPanel drawPanel;
	JFrame frame;
	
	public Board() {
		pacman = new PacMan();
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		drawPanel = new BoardPanel();
		
		frame.getContentPane().add(drawPanel);
		frame.setSize(MAX_X, MAX_Y);
		frame.setVisible(true);
		frame.setResizable(false);
		
		frame.addKeyListener(this);
		
		//System.out.println("In constructor");
	}
	
	public static void main(String[] args) {
		Board board = new Board();
		board.go();
	}

	public void go() {
		while(true){
			drawPanel.repaint();	
		}
	}


	class PacMan {

		int x = 12 * PAC_SIZE;
		int y = 12 * PAC_SIZE; //the x and y coordinates of the pacman

		char newDir =  's';
		char currDir = 's';
		
		synchronized public int getX() {
			return x;
		}

		synchronized public void setX(int x) {
			this.x = x;
		}

		synchronized public int getY() {
			return y;
		}

		synchronized public void setY(int y) {
			this.y = y;
		}
		
		public char getNewDir() {
			return newDir;
		}

		public void setNewDir(char newDir) {
			this.newDir = newDir;
		}

		public char getCurrDir() {
			return currDir;
		}

		public void setCurrDir(char currDir) {
			this.currDir = currDir;
		}
	}
	
	class BoardPanel extends JPanel {
		public void paintComponent(Graphics comp) {
			Graphics2D g = (Graphics2D) comp;

			g.setColor(Color.black);
			g.fillRect(0, 0, MAX_X, MAX_Y);

			g.setColor(Color.blue);

			for (int i = 0; i < MAZE_SIZE; i++) {  //creating the board layout
				for (int j = 0; j < MAZE_SIZE; j++) {
					if (mazeArray[i][j] == 1) {
						g.fillRect(j * PAC_SIZE, i * PAC_SIZE, PAC_SIZE,
								PAC_SIZE);
					}
				}
			} //finish board layout
			
			g.setColor(Color.yellow);
			
			//System.out.println(pacman.getX());
			//System.out.println(pacman.getY());
			//while(true) {
			int tmpx = pacman.getX();
			int tmpy = pacman.getY();
			
			if(pacman.getNewDir() == 's') // movement not yet started. pacman in initial position
			{
				g.fillOval(tmpx, tmpy, PAC_SIZE, PAC_SIZE);
			}
			else if(pacman.getNewDir() == 'u'){
				if ( tmpy == 0 ){
					 tmpy = 13* PAC_SIZE;
				}
				tmpy -= 5;
				g.fillOval(tmpx, tmpy, PAC_SIZE, PAC_SIZE);
			}
			else if(pacman.getNewDir() == 'd'){
				if ( tmpy == 13* PAC_SIZE ){
					tmpy = 0;
				}
				tmpy += 5;
				g.fillOval(tmpx, tmpy, PAC_SIZE, PAC_SIZE);
			}
			else if(pacman.getNewDir() == 'l'){
				if ( tmpx == 0 ){
					 tmpx = 13* PAC_SIZE;
				}
				tmpx -= 5;
				g.fillOval(tmpx,tmpy, PAC_SIZE, PAC_SIZE);
			}
			else if(pacman.getNewDir() == 'r'){
				if ( tmpx == 13* PAC_SIZE ){
					tmpx = 0;
				}
				tmpx += 5;
				g.fillOval(tmpx,tmpy, PAC_SIZE, PAC_SIZE);
			}
			
			pacman.setX(tmpx);
			pacman.setY(tmpy);
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("In keyTyped");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int ckey;
		int x,y;
		x = pacman.getX();
		y = pacman.getY();
		
		System.out.println("In keyPressed");
		
        ckey = e.getKeyCode();
        if (ckey==38){
            //pacman.setX(x-1); //UP
        	pacman.setNewDir('u'); //newDir = 'u';
        }
        if (ckey==40){
        	//pacman.setX(x+1); //Down
        	pacman.setNewDir('d'); //newDir = 'd';
        }
        if (ckey==39){
            //pacman.setY(y-1); //Right
        	pacman.setNewDir('r'); //newDir = 'r';
        }
        if (ckey==37){
        	//pacman.setY(y+1); //Left
        	pacman.setNewDir('l'); //newDir = 'l';
        }
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("In keyReleased");
	}
}