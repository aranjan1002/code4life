package edu.cp.project;

// Pacman by X5K MaStEr X5K and John
// started September 10, 2001

// we used some code from:
// Warp
// By Karl Hornell, June 10, 1996
// to get us started!  Thanks Karl!


import java.awt.*;
import java.awt.image.*;
import java.applet.AudioClip;
import java.net.*;
import java.awt.Font;

import java.util.Random;

public final class Board2 extends java.applet.Applet implements Runnable {

	Graphics gBuf;	// used for double-buffered graphics
	Image imgBuf;	// also used for double-buffered graphics
	
	
	static final int PACMAN_SIZE = 40;
	
	Thread updateThread;	// thread in which the game will run
	long startTime;			// used to keep track of timing and to prevent applet from running too fast

	int x = 0;  // x position of pacman
	int dx[] = {0,0}; // amount x position will change
	int y = 0; 	// y position of pacman
	int dy[] = {0,0}; // amount y will change

	int mouthStartAngle = 180;	// direction that pacman is pointing

	// This array describes each "cell" of the screen
	// Cells labeled 0 are open and those with a 1 are
	// walls.
	int[][] mazeArray =
		{ 	{2,2,2,2,2,2,2,2,2,2,2,2,2},
			{2,1,1,1,1,1,1,1,1,1,1,1,2},
			{2,1,2,2,2,2,2,2,2,2,2,2,2},
			{2,1,2,1,1,1,1,1,2,1,2,1,2},
			{2,1,2,1,2,2,2,2,2,1,2,1,2},
			{2,1,2,1,2,1,1,1,2,1,2,1,2},
			{2,1,2,1,2,2,2,2,2,1,2,1,2},
			{2,2,2,2,2,1,1,1,1,1,2,1,2},
			{2,1,2,1,2,2,2,2,2,2,2,1,2},
			{2,1,2,1,1,1,1,1,1,1,1,1,2},
			{2,1,2,2,2,2,2,2,2,2,2,2,2},
			{2,1,1,1,1,1,1,1,1,1,1,1,2},
			{2,2,2,2,2,2,2,2,2,2,2,2,2}
		};

	final static int MAZE_SIZE = 13;
	final static int SPEED = 10;
	static final int MAX_X = PACMAN_SIZE * MAZE_SIZE;  // widest the playing screen can be
	static final int MAX_Y = PACMAN_SIZE * MAZE_SIZE;  // tallest the playing screen can be
	
public void getMainGraphics() // Load and process the most common graphics
{
	MediaTracker tracker;
	int i = 0;

	tracker = new MediaTracker(this);

	// this code doesn't load any graphics yet!

	try
	{
		tracker.waitForAll();
	}
	catch (InterruptedException e)
	{
	}

}
public void init()
{
	// Make the applet window the size we want it to be
	resize(MAX_X, MAX_Y);

	// Load the images we will use from the web
	getMainGraphics();

	// Garbage collection call.  Not really needed.
	System.gc();

	// Make a black background
	setBackground(Color.black);

	// Set up double-buffered graphics.
	// This allows us to draw without flickering.
	imgBuf = createImage(MAX_X, MAX_Y);
	gBuf = imgBuf.getGraphics();

	// try to grab the keyboard focus.
	requestFocus();
}
public boolean keyDown(java.awt.Event e, int key)
{

	// This method handles key presses.
	// For now all the statements are placeholders.

	// it is nice to have a print statement here.  
	// it can be quickly uncommented and the output
	// used to get keycodes since I am too lazy to
	// look them up.

	//System.out.println(key);

	if (key == 1006) // left arrow
	{
		if (dx[0] == 0 && dy[0] == 0)
		{
			dx[0] = -SPEED;
			dx[1] = -SPEED;
			dy[0] = 0;
			dy[1] = 0;
		}
		else
		{
			dx[1] = -SPEED;
			dy[1] = 0;
		}
		return false;
	}
	if (key == 1007) // right arrow
	{
		if (dx[0] == 0 && dy[0] == 0)
		{
			dx[0] = SPEED;
			dx[1] = SPEED;
			dy[0] = 0;
			dy[1] = 0;
		}
		else
		{
			dx[1] = SPEED;
			dy[1] = 0;
		}
		return false;
	}

	if (key == 1004) // up arrow
	{
		if (dx[0] == 0 && dy[0] == 0)
		{
			dx[0] = 0;
			dx[1] = 0;
			dy[0] = -SPEED;
			dy[1] = -SPEED;
		}
		else
		{
			dx[1] = 0;
			dy[1] = -SPEED;
		}
		
		return false;
	}
	if (key == 1005) // down arrow
	{
		if (dx[0] == 0 && dy[0] == 0)
		{
			dx[0] = 0;
			dx[1] = 0;
			dy[0] = SPEED;
			dy[1] = SPEED;
		}
		else
		{
			dx[1] = 0;
			dy[1] = SPEED;
		}
		return false;
	}

	if (key == 32) // space bar
	{

		return false;
	}

	if (key == 112) // 'P' key
	{

		return false;
	}

	return false;
}
public boolean keyUp(java.awt.Event e, int key)
{
	// This method is called when a key is released.

	// right now it just has place holders.
	
	
	if (key == 1006 || key == 1007)  // left or right key released
	{
		return false;
	}

	if (key == 1004 || key == 1005) // up or down key released.
	{
		return false;
	}

	if (key == 32)  // space bar released
	{
		
		return false;
	}
	return false;
}
// To ensure Java 1.1 compatibility, request focus on mouseDown
public boolean mouseDown(java.awt.Event e, int x, int y)
{
    requestFocus();
    return false;
}
public void paint(Graphics g) // Draw the control panel and stuff
{
	// Since there are no borders or anything
	// static to draw yet we only need to call
	// the update method.
	update(g);
}
/* (non-Javadoc)
 * @see java.lang.Runnable#run()
 */
public void run()
{
	// This is the most important method.  It loops over and
	// over again as the game is running.  It makes the calls
	// that move things and then draw them.

	int mouthOpenAngle = 20;
	int dMouthOpenAngle = 5;
	int maxMouthOpenAngle = 100;
	int minMouthOpenAngle = 1;

	int nextX = 0;
	int nextY = 0;

	int row;
	int col;
	boolean hitWall = false;

	int i;

	while (updateThread != null)
	{
		try
		{
			// this code slows the applet down if it is on a really fast machine
			long sleepTime = Math.max(startTime - System.currentTimeMillis(), 20);
			updateThread.sleep(sleepTime);
		}
		catch (InterruptedException e)
		{
		}
		startTime = System.currentTimeMillis() + 40;

		// DRAW STUFF HERE:

		// clear what we drew last time.
		gBuf.clearRect(0, 0, MAX_X, MAX_Y);

		// Eat a pellet if it is there
		int curCol = (x + PACMAN_SIZE/2)/PACMAN_SIZE;
		int curRow = (y + PACMAN_SIZE/2)/PACMAN_SIZE;

		if (mazeArray[curRow][curCol] == 2)
		{
			mazeArray[curRow][curCol] = 0;
		}
		
		for (i = 1; i >= 0; i--)
		{
			nextX = x + dx[i];
			nextY = y + dy[i];
			hitWall = false;

			// Process each "cell" of the maze and paint it correctly
			for (int xCorner = 0; xCorner < PACMAN_SIZE; xCorner += PACMAN_SIZE - 1)
			{
				for (int yCorner = 0; yCorner < PACMAN_SIZE; yCorner += PACMAN_SIZE - 1)
				{
					col = (nextX + xCorner) / PACMAN_SIZE;
					row = (nextY + yCorner) / PACMAN_SIZE;
					if (row < MAZE_SIZE && col < MAZE_SIZE)
					{
						if (mazeArray[row][col] == 1)
						{
							hitWall = true;
							break;
						}
					}
				}
			}
			if (!hitWall)
			{
				if (i == 1)
				{
					dx[0] = dx[1];
					dy[0] = dy[1];
				}
				break;
			}

		}

		if (dx[0] > 0)
		{
			mouthStartAngle = 0;
		}
		else if (dx[0] < 0)
		{
			mouthStartAngle = 180;
		}
		else if (dy[0] > 0)
		{
			mouthStartAngle = 270;
		}
		else if (dy[0] < 0)
		{
			mouthStartAngle = 90;
		}
		
		//Move PacMan
		if (!hitWall)
		{
			x = nextX;
			y = nextY;
		}
		else
		{
			dx[0] = 0;
			dx[1] = 0;
			dy[0] = 0;
			dy[1] = 0;
		}

		// Don't let him go off the sides of the screen
		if (x > MAX_X - PACMAN_SIZE)
		{
			x = MAX_X - PACMAN_SIZE;
			dx[0] = 0;
			dx[1] = 0;
		}
		if (x < 0)
		{
			x = 0;
			dx[0] = 0;
			dx[1] = 0;
		}

		// Don't let him go off the top or bottom of the screen
		if (y > MAX_Y - PACMAN_SIZE)
		{
			y = MAX_Y - PACMAN_SIZE;
			dy[i] = 0;
			dy[1] = 0;
		}
		if (y < 0)
		{
			y = 0;
			dy[i] = 0;
			dy[1] = 0;
		}

	
		// Make the mouth chomp
		mouthOpenAngle = mouthOpenAngle + dMouthOpenAngle;

		if (mouthOpenAngle > maxMouthOpenAngle)
		{
			mouthOpenAngle = maxMouthOpenAngle;
			dMouthOpenAngle = -10;
		}
		if (mouthOpenAngle < minMouthOpenAngle)
		{
			mouthOpenAngle = minMouthOpenAngle;
			dMouthOpenAngle = 5;
		}

		// set the drawing color
		gBuf.setColor(Color.yellow);

		// draw a PacMan
		gBuf.fillArc(
			x,
			y,
			PACMAN_SIZE,
			PACMAN_SIZE,
			mouthStartAngle + mouthOpenAngle / 2,
			360 - mouthOpenAngle);

		// draw maze

		gBuf.setColor(Color.cyan);

		for (row = 0; row < MAZE_SIZE; row++)
		{
			for (col = 0; col < MAZE_SIZE; col++)
			{
				if (mazeArray[row][col] == 1)
				{
					gBuf.setColor(Color.cyan);
					gBuf.fillRect(col * PACMAN_SIZE, row * PACMAN_SIZE, PACMAN_SIZE, PACMAN_SIZE);
				}
				else if (mazeArray[row][col] == 2)
				{
					gBuf.setColor(Color.white);
					gBuf.fillArc(col * PACMAN_SIZE+PACMAN_SIZE/2 - 5, row * PACMAN_SIZE+PACMAN_SIZE/2 -5, 10,10,0,360);
				}
			}
		}

		// set the drawing color
		gBuf.setColor(Color.yellow);

		// draw a PacMan
		gBuf.fillArc(
			x,
			y,
			PACMAN_SIZE,
			PACMAN_SIZE,
			mouthStartAngle + mouthOpenAngle / 2,
			360 - mouthOpenAngle);

		
		// repaint() will call paint(Graphics) which will call update(Graphics)
		repaint();
	}
}
// This method is called when the applet is run.
// It initiallizes the thread and gets things going.
public void start()
{
    if (updateThread == null)
    {
        updateThread = new Thread(this, "Game");
        updateThread.start();
        startTime = System.currentTimeMillis();
    }
}
// This method is called when the applet is stopped. 
public void stop()
{
	updateThread = null;
}
public void update(Graphics g)
{
	// draw the offscreen buffer to the screen!
	// This buffer was drawn on by the run() method
	// and by any methods run() might have called.
	g.drawImage(imgBuf, 0, 0, this);

}
}