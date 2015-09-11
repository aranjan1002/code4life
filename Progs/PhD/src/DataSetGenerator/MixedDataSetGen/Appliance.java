package edu.strippacking.DataSetGenerator;

import java.lang.*;
import java.util.Random;

public class Appliance {
    /*
      line is expected to have values in the following format:
      string,double,double-double,,integer,boolean
     */
    Appliance(String line) {
	String[] split = line.split(",");
	name = split[0];
	power = Double.parseDouble(split[1]);
	readDuration(split[2]);
	readCount(split[4]);
	isPreemptable = Boolean.parseBoolean(split[5]);
    }

    public String toString() {
	String str = new String(name + " " +
				power + " " +
				durationRange[0] + " " +
				durationRange[1] + " " +
				count[0] + " " +
				count[1] + " " +
				isPreemptable);
	return str;
    }

    public String[] getSample(int reference) {
	return getSample(null, reference);
    }

    public String[] getSample(String stripWidth, int reference) {
	int cnt = getSampleCount();
	String[] sample = new String[cnt];
	for (int i = 0; i < cnt; i++) {
	    String s = new String();
	    if (reference != 0) {
		s = s + Integer.toString(reference);
		reference++;
	    }
	    s += "," + power;
	    s += "," + getSampleDuration();
	    s += ",";
	    if (stripWidth != null) {
		s += stripWidth;
		stripWidth = null;
	    }
	    s += "," + Boolean.toString(isPreemptable);
	    sample[i] = s;
	}
	return sample;
    }

    private void readDuration(String line) {
	String[] split = line.split("-");
	durationRange[0] = Double.parseDouble(split[0]);
	if (split.length == 2) {
	    durationRange[1] = Double.parseDouble(split[1]);
	} else {
	    durationRange[1] = durationRange[0];
	}
    }

    private void readCount(String line) {
	String[] split = line.split("-");
        count[0] = Integer.parseInt(split[0]);
        if (split.length == 2) {
            count[1] = Integer.parseInt(split[1]);
        } else {
            count[1] = count[0];
        }
    }
    
    private int getSampleCount() {
	if (count[0] == count[1]) {
	    return count[0];
	} else {
	    int diff = count[1] - count[0];
	    int r = rand.nextInt(diff) + 1;
	    return count[0] + r;
	}
    }

    private String getSampleDuration() {
	if (durationRange[0] == durationRange[1]) {
	    return Double.toString(durationRange[0]);
	} else {
	    double diff = durationRange[1] - durationRange[0];
	    int diff_int = new Double(diff).intValue();
	    int r = rand.nextInt(diff_int) + 1;
	    return new Double(durationRange[0] + r).toString();
	}
    }
    
    String name;
    double power; 
    int[] count = new int[2];
    double[] durationRange = new double[2];
    boolean isPreemptable;
    Random rand = new Random();
}