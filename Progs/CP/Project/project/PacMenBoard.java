package edu.cp.project;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class PacMenBoard extends JFrame {
	
	JFrame frame;
	
    public PacMenBoard(List<ClientInfo> pacMenClients,
    		List<ClientInfo> ghostClients) {
		clientInfoToPos.put(pacMenClients.get(0), new ClientPos(0, 0));
		clientInfoToPos.put(ghostClients.get(0), new ClientPos(MAZE_SIZE - 1, 
															MAZE_SIZE - 1));
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		drawPanel = new BoardPanel();
		
		frame.getContentPane().add(drawPanel);
		frame.setSize(MAX_X, MAX_Y);
		frame.setVisible(true);
		frame.setResizable(false);
		}

    String move(ClientInfo clInfo, ClientMessageReader.MOVE_DIR movDIR) {
    	ClientPos clientPos = clientInfoToPos.get(clInfo);
    	if (move(clientPos, movDIR)) {
    		drawPanel.repaint();
    		System.out.println("Successfully moved the client " + clInfo + " " + movDIR);
    		return toString();
    	} else {
    		return null;
    	}
    }
    
    boolean move(ClientPos currPos, ClientMessageReader.MOVE_DIR movDIR) {
    	if (movDIR == ClientMessageReader.MOVE_DIR.UP) {
    		return currPos.up();
    	} else if (movDIR == ClientMessageReader.MOVE_DIR.DOWN) {
    		return currPos.down();
    	} else if (movDIR == ClientMessageReader.MOVE_DIR.LEFT) {
    		return currPos.left();
    	} else {
    		return currPos.right();
    	}
    }
    
    
    
    int[][] mazeArray = { { 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
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
			  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3 } };
    
    public void go() {
		while(true){
			drawPanel.repaint();	
		}
	}
    
    class BoardPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics comp) {
			Graphics2D g = (Graphics2D) comp;

			g.setColor(Color.black);
			g.fillRect(0, 0, MAX_X, MAX_Y);

			

			for (int i = 0; i < MAZE_SIZE; i++) {  //creating the board layout
				for (int j = 0; j < MAZE_SIZE; j++) {
					if (mazeArray[i][j] == 1) {
						g.setColor(Color.blue);
						g.fillRect(j * PAC_SIZE, i * PAC_SIZE, PAC_SIZE,
								PAC_SIZE);
					} else if (mazeArray[i][j] == 2) {
						g.setColor(Color.yellow);
						g.fillOval(j * PAC_SIZE, i * PAC_SIZE, PAC_SIZE, PAC_SIZE);
					} else if (mazeArray[i][j] == 3) {
						g.setColor(Color.darkGray);
						g.fillOval(j * PAC_SIZE, i * PAC_SIZE, PAC_SIZE, PAC_SIZE);
					}
				}
			} //finish board layout
			//System.out.println(pacman.getX());
			//System.out.println(pacman.getY());
		}
	}
    
    public String toString() {
    	String[] result1 = new String[MAZE_SIZE];
    	for (int i = 0; i < MAZE_SIZE; i++) {  //creating the board layout
			for (int j = 0; j < MAZE_SIZE; j++) {
				result1[i] = Arrays.toString(mazeArray[i]);
			}
    	}
    	return Arrays.toString(result1);
    }
    
    public void drawBoard(String mazeString) {
    	String[] str = mazeString.split("], ");
    	for (int i = 0; i < str.length; i++) {
    		str[i] = str[i].replaceAll("\\[", "");
    		String[] str2 = str[i].split(", ");
    		for (int j = 0; j < MAZE_SIZE; j++) {
    			mazeArray[i][j] = Integer.parseInt(str2[j]);
    		}
    	}
    	drawPanel.repaint();
    }
    
    class ClientPos {
    		int i;
    		int j;
    		
    		ClientPos(int x, int y) {
    			i = x;
    			j = y;
    		}
    		
    		boolean up() {
    			if (i - 1 >= 0 &&
    					mazeArray[i - 1][j] != 1) {
    				mazeArray[i - 1][j] = mazeArray[i][j];
    				mazeArray[i][j] = 0;
    				i = i - 1;
    				return true;
    			}
    			return false;
    		}
    		
    		boolean down() {
    			if (i + 1 < MAZE_SIZE - 1 &&
    					mazeArray[i + 1][j] != 1) {
    				mazeArray[i + 1][j] = mazeArray[i][j];
    				mazeArray[i][j] = 0;
    				i += 1;
    				return true;
    			}
    			return false;
    		}
    		
    		boolean left() {
    			if (j - 1 >= 0 &&
    					mazeArray[i][j - 1] != 1) {
    				mazeArray[i][j - 1] = mazeArray[i][j];
    				mazeArray[i][j] = 0;
    				j -= 1;
    				return true;
    			}
    			return false;
    		}
    		
    		boolean right() {
    			if (j + 1 < MAZE_SIZE &&
    					mazeArray[i][j + 1] != 1) {
    				mazeArray[i][j + 1] = mazeArray[i][j];
    				mazeArray[i][j] = 0;
    				j += 1;
    				return true;
    			}
    			return false;
    		}
    }
    
	BoardPanel drawPanel;
	Map<ClientInfo, ClientPos> clientInfoToPos = new HashMap<ClientInfo, ClientPos>();
    
    static final int MAZE_SIZE = 13;
	static final int PAC_SIZE = 30;
    static final int MAX_X = 400; // widest the playing screen can be
	static final int MAX_Y = 420; // tallest the playing screen can be
}

/*package edu.cp.project;



public class Board extends JFrame implements KeyListener{
	*//**
	 * 
	 *//*
	private static final long serialVersionUID = 1L;
	

	

	
	
	
	JFrame frame;

	
	
	public static void main(String[] args) {
		Board board = new Board();
		board.go();
	}


	class PacMan {
		int x =10;
		int y = 6; //the x and y coordinates of the pacman

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
            pacman.setX(x-1); //UP
        }
        if (ckey==40){
        	pacman.setX(x+1); //Down
        }
        if (ckey==37){
            pacman.setY(y-1); //Right
        }
        if (ckey==39){
        	pacman.setY(y+1); //Left
        }
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("In keyReleased");
	}
}*/