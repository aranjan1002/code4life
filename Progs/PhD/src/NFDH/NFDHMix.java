package edu.strippacking.NFDH;

import java.util.Collections;
import java.util.ArrayList;

import edu.strippacking.Job;
import edu.strippacking.SchedulingAlgorithm;

public class NFDHMix implements SchedulingAlgorithm {
    public double schedule(ArrayList<Job> jobs,
			   double D) {
	double currX = D;
        double currY = 0;
        double currH = 0;

	Collections.sort(jobs);

        for (int i = 0; i < jobs.size(); i++) {
            Job j = jobs.get(i);
            if (j.d <= D - currX) {
                // place jangle at (currX, currY)
                //System.out.print(j);
                //System.out.println(" placed at " + currX + " " + currY);
                currX += j.d;
            } else if (currX == D) {
                // place jangle at the level above current level
                currX = j.d;
                // currY = currH;
                currH += j.p;
            } else if (j.isPreemptable == true) {
                // place jangle at (currX, currY), split and place at
                // (0, currH)
                currX = j.d - (D - currX);
                // currY = currH;
                currH += j.p;
            } else {
		// place jangle above the current level
		currX = j.d;
		currH += j.p;
	    }
        }
        return currH;
    }
}