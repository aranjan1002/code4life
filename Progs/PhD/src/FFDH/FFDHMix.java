package edu.strippacking.FFDH;

import java.util.ArrayList;
import java.util.Collections;
import java.lang.Integer;

import edu.strippacking.Job;
import edu.strippacking.SchedulingAlgorithm;

public class FFDHMix implements SchedulingAlgorithm {
    // jobs - list of jobs
    // D - strip width / total Duration
    public double schedule(ArrayList<Job> jobs, double D) {
	Collections.sort(jobs);
	// new edu.strippacking.Display().displayJobs(jobs);
	for (Job j : jobs) {
	    placeJob(j, D);
	}

	return totalHeight;
    }

    private void placeJob(Job j, double D) {
	double d = j.d;
	// job can't be placed after D - placedUntil
	
	if (j.isPreemptable == false) {
	    d = placeNonPreemptableJob(d);
	} else {
	    d = placePreemptableJob(d);
	}
	
	if (d > 0) {
	    spaceAt.add(new Double(D - d));
	    totalHeight += j.p;
	}
    }
    
    private double placeNonPreemptableJob(double d) {
	for (int idx = 0; idx < spaceAt.size(); idx++) {
	    double s = spaceAt.get(idx).doubleValue();
	    if (s >= d) {
		spaceAt.set(idx, new Double(s - d));
		return 0;
	    }
	}
	return d;
    }

    private double placePreemptableJob(double d) {
	double placedUntil = 0;
	for (int idx = 0; idx < spaceAt.size(); idx++) {
	    double s = spaceAt.get(idx).doubleValue();
	    if (s > placedUntil) {
		if ((s - placedUntil) >= d) {
		    spaceAt.set(idx, new Double(s - d));
		    return 0;
		}
		else {
		    d = d - s + placedUntil;
		    spaceAt.set(idx, new Double(placedUntil));
		    placedUntil = s;
		}
	    }
	}
	return d;
    }

    double totalHeight = 0;
    
    ArrayList<Double> spaceAt = new ArrayList<Double>();
}