package edu.strippacking.NFDH;

import java.util.ArrayList;
import java.util.Collections;

import edu.strippacking.Job;
import edu.strippacking.SchedulingAlgorithm;

public class NFDH implements SchedulingAlgorithm {
    public double schedule(ArrayList<Job> rectangleList,
			   double D) {
        Collections.sort(rectangleList);
        double currX = D;
        // int currY = 0;
        double currH = 0;

        for (int i = 0; i < rectangleList.size(); i++) {
            Job rect = rectangleList.get(i);
            if (rect.d <= D - currX) {
                // place rectangle at (currX, currY)
                currX += rect.d;
            } else {
                // place rectangle at the level above current level
                currX = rect.d;
                // currY = currH;
                currH += rect.p;
            }
            //System.out.println(rect + " placed. CurrX=" + currX +
            //" CurrH=" + currH);
        }
        return currH;
    }
}