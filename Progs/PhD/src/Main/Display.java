package edu.strippacking;

import java.util.ArrayList;

public class Display {
    public void displayJobs(ArrayList<Job> jobs) {
	for (Job j : jobs) {
	    System.out.println(j);
	}
    }
}