package edu.strippacking.NFDH;

import java.util.ArrayList;
import java.util.Collections;

public class NonPreemptiveNFDH {
    public double applyNFDHAndGetHeight(
					ArrayList<Rectangle> 
					rectangleList) {
	Collections.sort(rectangleList);
        double currX = Constants.W;
        // int currY = 0;
        double currH = 0;

        for (int i = 0; i < rectangleList.size(); i++) {
            Rectangle rect = rectangleList.get(i);
            if (rect.width <= Constants.W - currX) {
                // place rectangle at (currX, currY)
                currX += rect.width;
            } else {
                // place rectangle at the level above current level
                currX = rect.width;
                // currY = currH;
                currH += rect.height;
            } 
	    //System.out.println(rect + " placed. CurrX=" + currX +
	    //" CurrH=" + currH);
        }
        return currH;
    }
}
