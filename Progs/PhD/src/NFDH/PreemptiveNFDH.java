package edu.strippacking.NFDH;

import java.util.Collections;
import java.util.ArrayList;

public class PreemptiveNFDH {
    public double getHeightAfterPacking(ArrayList<Rectangle> 
					rectangleList) {
	
	double currX = Constants.W; 
	double currH = 0; 
	
	Collections.sort(rectangleList);
	//System.out.println("Starting preemptive strip packing");
	//long start = System.currentTimeMillis();
	for (int i = 0; i < rectangleList.size(); i++) {
	    Rectangle rect = rectangleList.get(i);
	    // System.out.println(rect.toString());
	    if (rect.width <= Constants.W - currX) {
		// place rectangle at (currX, currY)
		//System.out.print(rect);
		//System.out.println(" placed at " + currX + " " + currH);
		currX += rect.width;
	    } else if (currX == Constants.W) {
		// place rectangle at the level above current level
		currX = rect.width;
		// currY = currH;
		currH += rect.height;
	    } else {
		// place rectangle at (currX, currY), split and place at
		// (0, currH)
		currX = rect.width - (Constants.W - currX);
		// currY = currH;
		currH += rect.height;
	    }
	}
	//long end = System.currentTimeMillis();
	//System.out.println("Time taken: " +
	//		   (end - start) +
	//		   "ms.");
	return currH;
    }
}