package edu.cp.project;

import java.util.*;

import java.awt.*;
import javax.swing.*;
class ClientBoard extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -864005501773990684L;
	JFrame frame;
	ArrayList<Client> pacMenList = new ArrayList<Client>();
	ArrayList<Client> ghostList = new ArrayList<Client>();

	public ClientBoard(JFrame frame, String clientsPos) {
		this.frame = frame;
		String[] str = clientsPos.split(" ");
		
		int i = 0;
		while (str[i].equals("G") == false) {
			System.out.println(str[i]);
			Client cl = new Client(0, 0);
			changeClientsState(cl, str, i);
			pacMenList.add(cl);
			i++;
		}
		i++;
		while (i != str.length) {
			Client cl = new Client(12 * PAC_SIZE, 12 * PAC_SIZE);
			changeClientsState(cl, str, i);
			ghostList.add(cl);
			i++;
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		drawPanel = new BoardPanel();

		frame.getContentPane().add(drawPanel);
		frame.setSize(MAX_X, MAX_Y);
		frame.setVisible(true);
		frame.setResizable(false);
	}

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

	public void run() {
		while(true){
			drawPanel.repaint();	
		}
	}

	class BoardPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		public void paintComponent(Graphics comp) {
			Graphics2D g = (Graphics2D) comp;
			boolean gamestillon = false;

			g.setColor(Color.black);

			g.fillRect(0, 0, MAX_X, MAX_Y);
			for (int i = 0; i < MAZE_SIZE; i++) { //creating the board layout
				for (int j = 0; j < MAZE_SIZE; j++) {
					if (mazeArray[i][j] == 1) {
						g.setColor(Color.blue);
						g.fillRect(j * PAC_SIZE, i * PAC_SIZE, PAC_SIZE,
								PAC_SIZE);
					} else if (mazeArray[i][j] == 0){
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

			for (Client pacMen : pacMenList) {
				g.setColor(Color.yellow);
				eatPelletAndDrawClient(pacMen, g);
			}

			for (Client ghost : ghostList) {
				g.setColor(Color.gray);
				drawClient(ghost, g);
			}
		}

		void eatPelletAndDrawClient(Client pacMen, Graphics2D g) {
			int tmpx = pacMen.x;
			int tmpy = pacMen.y;
			if((tmpx % PAC_SIZE == 0) && (tmpy % PAC_SIZE == 0)){
				int i = pacMen.x / PAC_SIZE;
				int j = pacMen.y / PAC_SIZE;
				//System.out.println(i + " " + j);
				if(mazeArray[j][i]==0){ 
					mazeArray[j][i] = -1; //eats pellet
				}
			}
			drawClient(pacMen, g);
		}

		void drawClient(Client cl, Graphics2D g) {
			int tmpx = cl.getX();
			int tmpy = cl.getY();
			char curDir = cl.getCurrDir();
			char newDir = cl.getNewDir();
			
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
				cl.setCurrDir(newDir);
			}
			
			if(curDir == 's') // movement not yet started. pacman in initial position
			{
				cl.setCurrDir(newDir);
				g.fillOval(tmpx+4, tmpy+4, PAC_SIZE-8, PAC_SIZE-8);
			}
			else if(curDir == 'u'){
				if ( tmpy == 0 - PAC_SIZE/2 ){
					 tmpy = 12* PAC_SIZE + PAC_SIZE/2;
				}
				tmpy -= SPEED;
				g.fillOval(tmpx+4,(--tmpy)+4, PAC_SIZE-8, PAC_SIZE-8);
			}
			else if(curDir == 'd'){
				if ( tmpy == 12* PAC_SIZE + PAC_SIZE/2 ){
					tmpy = 0 - PAC_SIZE/2;
				}
				tmpy += SPEED;
				g.fillOval(tmpx+4,(++tmpy)+4, PAC_SIZE-8, PAC_SIZE-8);
			}
			else if(curDir == 'l'){
				if ( tmpx == 0 - PAC_SIZE/2 ){
					 tmpx = 12* PAC_SIZE + PAC_SIZE/2;
				}
				tmpx -= SPEED;
				g.fillOval((--tmpx)+4,tmpy+4, PAC_SIZE-8, PAC_SIZE-8);
			}
			else if(curDir == 'r'){
				if ( tmpx == 12* PAC_SIZE + PAC_SIZE/2){
					tmpx = -PAC_SIZE/2;
				}
				tmpx += SPEED;
				g.fillOval((++tmpx)+4,tmpy+4, PAC_SIZE-8, PAC_SIZE-8);
			}
			
			cl.setX(tmpx);
			cl.setY(tmpy);
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

		public void changeClientsState(String clientPos) {
			String[] str = clientPos.trim().split(" ");
			int i = 0;
			int cnt = 0;
			while (str[i].equals("G") == false) {
				System.out.println(str[i]);
				changeClientsState(pacMenList.get(cnt++), str, i);
				i++;
			}
			i++;
			cnt = 0;
			while (i < str.length) {
				changeClientsState(ghostList.get(cnt++), str, i);
				i++;
			}

			drawPanel.repaint();
		}

		public void changeClientsState(Client cl, String[] str, int idx) {
			//cl.x = Integer.parseInt(str[idx]);
			//cl.y = Integer.parseInt(str[idx + 1]);
			cl.newDir = str[idx].charAt(0);
		}
		
		class Client {
			int x = 12 * PAC_SIZE;
			int y = 12 * PAC_SIZE; //the x and y coordinates of the pacman

			char newDir =  's';
			char currDir = 's';

			Client() {
			}

			Client(int x, int y) {
				this.x = x;
				this.y = y;
			}

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

		BoardPanel drawPanel;

		static final int MAZE_SIZE = 13;
		static final int PAC_SIZE = 30;
		static final int MAX_X = 400; // widest the playing screen can be
		static final int MAX_Y = 420; // tallest the playing screen can be
		static final int SPEED = 0;
	}
