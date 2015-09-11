package edu.strippacking;

import java.lang.Comparable;

public class Job implements Comparable {

    Job(double d, double p) {
	if (d < 0 || p < 0 || d > Constants.D) {
	    throw new RuntimeException("Invalid rectangle");
	}
	duration = d;
	power = p;
    }

    Job(double d, double p, boolean isPreemptive) {
	this(d, p);
	this.isPreemptive = isPreemptive;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(power)
	    .append("x")
	    .append(duration)
	    .append("(")
	    .append(isPreemptive)
	    .append(")");
	return sb.toString();
    }

    public void setPreemptive(boolean isPreemptive) {
	this.isPreemptive = isPreemptive;
    }

    // To ensure descending order of sorting jobs returns
    // 1 if power of this rectangle < power of o
    // 0 if powers are equal
    // -1 if power of this rectangle > power of o
    public int compareTo(Object o) {
	Job r = (Job) o;
	if (power < r.power) {
	    return 1;
	} else if (power == r.power) {
	    return 0;
	} else {
	    return -1;
	}
    }

    public double getPower() {
	return power;
    }

    public double getDuration() {
	return duration;
    }

    double power, duration;
    boolean isPreemptive;
}