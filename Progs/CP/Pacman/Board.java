package edu.cp.project.client;

import java.util.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.*;

class Board extends JFrame implements Runnable {
	private static final long serialVersionUID = -864005501773990684L;
	JFrame frame;
	ArrayList<COWClient> pacMenList = new ArrayList<COWClient>();
	ArrayList<COWClient> ghostList = new ArrayList<COWClient>();
	BufferedImage pacL = null;
	BufferedImage pacR = null;
	BufferedImage pacU = null;
	BufferedImage pacD = null;
	BufferedImage pacS = null;
	BufferedImage wallimg = null;
	BufferedImage ghostimg = null;

	public Board(JFrame frame, String clientsPos) {
		this.frame = frame;
		try {
			pacL = ImageIO.read(new File("PacL.gif"));
			pacR = ImageIO.read(new File("PacR.gif"));
			pacU = ImageIO.read(new File("PacU.gif"));
			pacD = ImageIO.read(new File("PacD.gif"));
			pacS = ImageIO.read(new File("PacS.gif"));
			wallimg = ImageIO.read(new File("wall.jpg"));
			ghostimg = ImageIO.read(new File("ghost.gif"));
		} catch (IOException e) {
		}
		String[] str = clientsPos.split(" ");

		int i = 0;
		while (str[i].equals("G") == false) {
			System.out.println(str[i]);
			COWClient cl = new COWClient(PAC_SIZE, PAC_SIZE);
			changeClientsState(cl, str, i);
			pacMenList.add(cl);
			i += 3;
		}
		i++;
		while (i != str.length) {
			COWClient cl = new COWClient(11 * PAC_SIZE, 11 * PAC_SIZE);
			changeClientsState(cl, str, i);
			ghostList.add(cl);
			i += 3;
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		drawPanel = new BoardPanel();

		frame.getContentPane().add(drawPanel);
		frame.setSize(MAX_X, MAX_Y);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	int[][] mazeArray = { { 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1 },
			{ 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1 },
			{ 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1 } };
	public void run() {
		while (true) {
			if (pacMenList.size() > 0 && ghostList.size() > 0) {
				
				COWClient.ClientPosition pc; //c is immutable
				pc = pacMenList.get(0).getClientPosition();
				int px = pc.getX();
				int py = pc.getY();
				
				for (COWClient ghost : ghostList) {

					COWClient.ClientPosition gc;
					gc = ghost.getClientPosition();
					int gx = gc.getX();
					int gy = gc.getY();

					if (gx / PAC_SIZE == px / PAC_SIZE) {
						if (gy / PAC_SIZE == py / PAC_SIZE) {
							gameState = 2;
						}
					}
				}
			}
			boolean pelletsExist = false;
			for (int i = 0; i < 13; ++i) {
				for (int j = 0; j < 13; ++j) {
					if (mazeArray[i][j] == 0)
						pelletsExist = true;
				}
			}
			if (!pelletsExist) {
				gameState = 1;
			}
			if (gameState != 0) {
				break;
			}
			synchronized(this) {
			    drawPanel.repaint();
			}
		}
		if (gameState == 1) {
			JOptionPane.showMessageDialog(frame, "GAME OVER Pac-Man wins!!");
		} else if (gameState == 2) {
			JOptionPane.showMessageDialog(frame, "GAME OVER Ghosts wins!!");
		}
	}

	class BoardPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics comp) {
			Graphics2D g = (Graphics2D) comp;

			g.setColor(Color.black);

			g.fillRect(0, 0, MAX_X, MAX_Y);

			for (int i = 0; i < MAZE_SIZE; i++) { // creating the board layout
				for (int j = 0; j < MAZE_SIZE; j++) {
					if (mazeArray[i][j] == 1) {
						g.drawImage(wallimg, j * PAC_SIZE, i * PAC_SIZE,
								PAC_SIZE, PAC_SIZE, this);
					} else if (mazeArray[i][j] == 0) {
						g.setColor(Color.MAGENTA);
						g.fillOval(j * PAC_SIZE + 13, i * PAC_SIZE + 13, 3, 3);
					}
				}
			} // finish board layout

			for (COWClient pacMen : pacMenList) {
				try {
					eatPelletAndDrawClient(pacMen, g);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			for (COWClient ghost : ghostList) {
				// g.setColor(Color.gray);
				drawClient(ghost, g, "G");
			}
		}

		void eatPelletAndDrawClient(COWClient pacMen, Graphics2D g)
				throws MalformedURLException {
			COWClient.ClientPosition tmpc;
			tmpc = pacMen.getClientPosition();
			int tmpx = tmpc.getX();
			int tmpy = tmpc.getY();
			if ((tmpx % PAC_SIZE == 0) && (tmpy % PAC_SIZE == 0)) {
				int i = tmpx / PAC_SIZE;
				int j = tmpy / PAC_SIZE;
				// System.out.println(i + " " + j);
				if (mazeArray[j][i] == 0) {
					mazeArray[j][i] = -1; // eats pellet
				}
			}
			drawClient(pacMen, g, "P");
		}

		void drawClient(COWClient cl, Graphics2D g, String type) {
			COWClient.ClientPosition tmpc;
			tmpc = cl.getClientPosition();
			int tmpx = tmpc.getX();
			int tmpy = tmpc.getY();
			char curDir = cl.getCurrDir();
			char newDir = cl.getNewDir();
			System.out.println(cl.toString());

			if ((tmpx % PAC_SIZE == 0) && (tmpy % PAC_SIZE == 0)
					) {
				int i = tmpx / PAC_SIZE;
				int j = tmpy / PAC_SIZE;
				// System.out.println(i + " " + j);

				if (newDir == 'u') {
					if (mazeArray[(j + 12) % 13][i] == 1) {
						if (curDir == 'l' && mazeArray[j][(i + 12) % 13] != 1) {

						} else if (curDir == 'r'
								&& mazeArray[j][(i + 1) % 13] != 1) {

						} else if (curDir == 'd'
								&& mazeArray[(j + 1) % 13][i] != 1) {

						} else {
							curDir = 's';
						}
					} else {
						curDir = newDir;
						cl.setCurrDir(newDir);
					}
				} else if (newDir == 'd') {
					if (mazeArray[(j + 1) % 13][i] == 1) {
						if (curDir == 'l' && mazeArray[j][(i + 12) % 13] != 1) {

						} else if (curDir == 'r'
								&& mazeArray[j][(i + 1) % 13] != 1) {

						} else if (curDir == 'u'
								&& mazeArray[(j + 12) % 13][i] != 1) {

						} else {
							curDir = 's';
						}
					} else {
						curDir = newDir;
						cl.setCurrDir(newDir);
					}
				} else if (newDir == 'l') {
					if (mazeArray[j][(i + 12) % 13] == 1) {
						if (curDir == 'u' && mazeArray[(j + 12) % 13][i] != 1) {

						} else if (curDir == 'r'
								&& mazeArray[j][(i + 1) % 13] != 1) {

						} else if (curDir == 'd'
								&& mazeArray[(j + 1) % 13][i] != 1) {

						} else {
							curDir = 's';
						}
					} else {
						curDir = newDir;
						cl.setCurrDir(newDir);
					}
				} else if (newDir == 'r') {
					if (mazeArray[j][(i + 1) % 13] == 1) {
						if (curDir == 'u' && mazeArray[(j + 12) % 13][i] != 1) {

						} else if (curDir == 'l'
								&& mazeArray[j][(i + 12) % 13] != 1) {

						} else if (curDir == 'd'
								&& mazeArray[(j + 1) % 13][i] != 1) {

						} else {
							curDir = 's';
						}
					} else {
						curDir = newDir;
						cl.setCurrDir(newDir);
					}
				}
			}

			if (curDir == 's') // movement not yet started. pacman in initial
								// position
			{
			    //cl.setCurrDir(newDir);
				// g.fillOval(tmpx+4, tmpy+4, PAC_SIZE-8, PAC_SIZE-8);
				if (type.equals("P"))
					g.drawImage(pacS, tmpx, tmpy, PAC_SIZE, PAC_SIZE, this);
				else
					g.drawImage(ghostimg, tmpx, tmpy, PAC_SIZE, PAC_SIZE, this);
			} else if (curDir == 'u') {
				if (tmpy == 0 - PAC_SIZE / 2) {
					tmpy = 12 * PAC_SIZE + PAC_SIZE / 2;
				}
				tmpy -= SPEED;
				// g.fillOval(tmpx ,(--tmpy) , PAC_SIZE-8, PAC_SIZE-8);
				if (type.equals("P"))
					g.drawImage(pacU, tmpx, (--tmpy), PAC_SIZE, PAC_SIZE, this);
				else
					g.drawImage(ghostimg, tmpx, (--tmpy), PAC_SIZE, PAC_SIZE,
							this);
			} else if (curDir == 'd') {
				if (tmpy == 12 * PAC_SIZE + PAC_SIZE / 2) {
					tmpy = 0 - PAC_SIZE / 2;
				}
				tmpy += SPEED;
				// g.fillOval(tmpx ,(++tmpy) , PAC_SIZE-8, PAC_SIZE-8);
				if (type.equals("P"))
					g.drawImage(pacD, tmpx, (++tmpy), PAC_SIZE, PAC_SIZE, this);
				else
					g.drawImage(ghostimg, tmpx, (++tmpy), PAC_SIZE, PAC_SIZE,
							this);
			} else if (curDir == 'l') {
				if (tmpx == 0 - PAC_SIZE / 2) {
					tmpx = 12 * PAC_SIZE + PAC_SIZE / 2;
				}
				tmpx -= SPEED;
				// g.fillOval((--tmpx) ,tmpy , PAC_SIZE-8, PAC_SIZE-8);
				if (type.equals("P"))
					g.drawImage(pacL, (--tmpx), tmpy, PAC_SIZE, PAC_SIZE, this);
				else
					g.drawImage(ghostimg, (--tmpx), tmpy, PAC_SIZE, PAC_SIZE,
							this);
			} else if (curDir == 'r') {
				if (tmpx == 12 * PAC_SIZE + PAC_SIZE / 2) {
					tmpx = -PAC_SIZE / 2;
				}
				tmpx += SPEED;
				if (type.equals("P"))
					g.drawImage(pacR, (++tmpx), tmpy, PAC_SIZE, PAC_SIZE, this);
				else
					g.drawImage(ghostimg, (++tmpx), tmpy, PAC_SIZE, PAC_SIZE,
							this);
			}

			cl.setXY(tmpx, tmpy);

			try {
				Thread.sleep(SLEEPTIME);
			} catch (InterruptedException e) {
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
			i += 3;
		}
		i++;
		cnt = 0;
		while (i < str.length) {
			changeClientsState(ghostList.get(cnt++), str, i);
			i += 3;
		}

		//drawPanel.repaint();
	}

	synchronized public void changeClientsState(COWClient cl, String[] str, int idx) {
		if (str[idx].equals("*") == false) {
			cl.setXY(Integer.parseInt(str[idx]), Integer.parseInt(str[idx + 1]));
			cl.setNewDir(str[idx + 2].charAt(0));
		}
	}

	class COWClient {

		//Immutable class
		class ClientPosition {
			final int x ;// = 12 * PAC_SIZE;
			final int y ;// = 12 * PAC_SIZE;

			ClientPosition(int x, int y) {
				this.x = x;
				this.y = y;
			}
			
			int getX(){ return x;}
			int getY(){ return y;}

		}

		protected ClientPosition c;
		char newDir = 's';
		char currDir = 's';

		public COWClient(int x, int y) {
			c = new ClientPosition(x, y);
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

		synchronized void setXY(int x, int y){
			c = new ClientPosition(x,y);
		}
		
		synchronized ClientPosition getClientPosition(){
			return c;
		}

	    public String toString() {
		return (c.getX() + " " +
				   c.getY() + " " +
				   currDir + " " +
				   newDir);
	    }

	}

	BoardPanel drawPanel;

	static final int MAZE_SIZE = 13;
	static final int PAC_SIZE = 30;
	static final int MAX_X = 400; // widest the playing screen can be
	static final int MAX_Y = 420; // tallest the playing screen can be
	static final int SPEED = 4;
	public static final int SLEEPTIME = 12;

	public int getPacmanClientX(int myClientIdx) {
		return pacMenList.get(myClientIdx).c.getX();
	}

	public int getPacmanClientY(int myClientIdx) {
		return pacMenList.get(myClientIdx).c.getY();
	}

	public int getGhostClientX(int myClientIdx) {
		return ghostList.get(myClientIdx).c.getX();
	}

	public int getGhostClientY(int myClientIdx) {
		return ghostList.get(myClientIdx).c.getY();
	}

	// 0 - game is going on, 1 - pacman won, 2 - ghost won
	// volatile to avoid data race
	// it is updated in the run() function of this thread and
	// in the main thread in the function listenToServer
	public volatile int gameState = 0;

}