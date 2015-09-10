package edu.strippacking;

public class Job implements Comparable {
    public Job(double p, double d) {
	this.p = p;
	this.d = d;
    }

    public String toString() {
	return ("p, d, preemptable = " + p + " " + d + " " + isPreemptable);
    }

    public int compareTo(Object o) {
        Job j = (Job) o;
        if (p < j.p) {
            return 1;
        } else if (p == j.p) {
            return 0;
        } else {
            return -1;
        }
    }


    public double p, d; // power, duration
    public boolean isPreemptable = false; 
}