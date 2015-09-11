package edu.strippacking.NFDH;

import java.util.Collections;
import java.util.ArrayList;

public class MixedNFDH {
    public double getHeightAfterPacking(ArrayList<Rectangle> 
					rectangleList) {
        double currX = Constants.W;
        double currY = 0;
        double currH = 0;

	Collections.sort(rectangleList);

        for (int i = 0; i < rectangleList.size(); i++) {
            Rectangle rect = rectangleList.get(i);
            if (rect.width <= Constants.W - currX) {
                // place rectangle at (currX, currY)
                //System.out.print(rect);
                //System.out.println(" placed at " + currX + " " + currY);
                currX += rect.width;
            } else if (currX == Constants.W) {
                // place rectangle at the level above current level
                currX = rect.width;
                // currY = currH;
                currH += rect.height;
            } else if (rect.isPreemptive == true) {
                // place rectangle at (currX, currY), split and place at
                // (0, currH)
                currX = rect.width - (Constants.W - currX);
                // currY = currH;
                currH += rect.height;
            } else {
		// place rectangle above the current level
		currX = rect.width;
		currH += rect.height;
	    }
        }
        return currH;
    }
}