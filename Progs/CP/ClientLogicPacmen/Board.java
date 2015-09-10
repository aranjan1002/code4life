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

			boolean gamestillon = false;
			
			g.setColor(Color.black);
			g.fillRect(0, 0, MAX_X, MAX_Y);
			
			
			for (int i = 0; i < MAZE_SIZE; i++) {  //creating the board layout
				for (int j = 0; j < MAZE_SIZE; j++) {
					if (mazeArray[i][j] == 1) {
						g.setColor(Color.blue);
						g.fillRect(j * PAC_SIZE, i * PAC_SIZE, PAC_SIZE,
								PAC_SIZE);
					}
					else if (mazeArray[i][j] == 0){
						g.setColor(Color.MAGENTA);
						g.fillOval(j * PAC_SIZE + 13, i * PAC_SIZE + 13, 3, 3);
						gamestillon = true;
					}
					else{
						g.setColor(Color.black);
						g.fillRect(j * PAC_SIZE, i * PAC_SIZE, PAC_SIZE, PAC_SIZE);
					}
					
				}
			} //finish board layout
			
			if(!gamestillon){
				//JOptionPane.showMessageDialog(frame, "All Pellets Eaten");
			}
			g.setColor(Color.yellow);
			
			//System.out.println(pacman.getX());
			//System.out.println(pacman.getY());
			
			int tmpx = pacman.getX();
			int tmpy = pacman.getY();
			char curDir = pacman.getCurrDir();
			char newDir = pacman.getNewDir();
			
			if((tmpx % PAC_SIZE == 0) && (tmpy % PAC_SIZE == 0) && curDir!='s'){
				int i = tmpx / PAC_SIZE;
				int j = tmpy / PAC_SIZE;
				//System.out.println(i + " " + j);
				if(mazeArray[j][i]==0){
					mazeArray[j][i] = -1;
				}
				if(newDir == 'u'){
					if(j==0){
						j = 13;
					}
					if(mazeArray[j-1][i]==1){
						newDir = 's';
					}
				}
				else if(newDir == 'd'){
					if(j== 12){
						j = -1;
					}
					if(mazeArray[j+1][i]==1){
						newDir = 's';
					}
				}
				else if(newDir == 'l'){
					if(i == 0){
						i = 13;
					}
					if(mazeArray[j][i-1]==1){
						newDir = 's';
					}
				}
				else if(newDir == 'r'){
					if(i== 12){
						i = -1;
					}
					if(mazeArray[j][i+1]==1){
						newDir = 's';
					}
				}
				curDir = newDir;
				pacman.setCurrDir(newDir);
			}
			
			if(curDir == 's') // movement not yet started. pacman in initial position
			{
				pacman.setCurrDir(newDir);
				g.fillOval(tmpx+4, tmpy+4, PAC_SIZE-8, PAC_SIZE-8);
			}
			else if(curDir == 'u'){
				if ( tmpy == 0 - PAC_SIZE/2 ){
					 tmpy = 12* PAC_SIZE + PAC_SIZE/2;
				}
				g.fillOval(tmpx+4,(--tmpy)+4, PAC_SIZE-8, PAC_SIZE-8);
			}
			else if(curDir == 'd'){
				if ( tmpy == 12* PAC_SIZE + PAC_SIZE/2 ){
					tmpy = 0 - PAC_SIZE/2;
				}
				g.fillOval(tmpx+4,(++tmpy)+4, PAC_SIZE-8, PAC_SIZE-8);
			}
			else if(curDir == 'l'){
				if ( tmpx == 0 - PAC_SIZE/2 ){
					 tmpx = 12* PAC_SIZE + PAC_SIZE/2;
				}
				g.fillOval((--tmpx)+4,tmpy+4, PAC_SIZE-8, PAC_SIZE-8);
			}
			else if(curDir == 'r'){
				if ( tmpx == 12* PAC_SIZE + PAC_SIZE/2){
					tmpx = -PAC_SIZE/2;
				}
				g.fillOval((++tmpx)+4,tmpy+4, PAC_SIZE-8, PAC_SIZE-8);
			}
			
			pacman.setX(tmpx);
			pacman.setY(tmpy);
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("In keyTyped");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int ckey;
		int x,y;
		x = pacman.getX();
		y = pacman.getY();
		
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