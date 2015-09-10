package edu.strippacking.NFDH;

import java.lang.Comparable;

public class Rectangle implements Comparable {
    double width, height;
    boolean isPreemptive = false;

    Rectangle(double w, double h) {
	if (w < 0 || h < 0 || w > Constants.W) {
	    throw new RuntimeException("Invalid rectangle");
	}
	width = w;
	height = h;
    }

    Rectangle(double w, double h, boolean isPreemptive) {
	this(w, h);
	this.isPreemptive = isPreemptive;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(", Width-")
	    .append(width)
	    .append(", Height-")
	    .append(height)
	    .append(", Preemptive-")
	    .append(isPreemptive);
	return sb.toString();
    }

    public void setPreemptive(boolean isPreemptive) {
	this.isPreemptive = isPreemptive;
    }

    // To ensure descending order of sorting rectangles returns
    // 1 if height of this rectangle < height of o
    // 0 if heights are equal
    // -1 if height of this rectangle > height of o
    public int compareTo(Object o) {
	Rectangle r = (Rectangle) o;
	if (height < r.height) {
	    return 1;
	} else if (height == r.height) {
	    return 0;
	} else {
	    return -1;
	}
    }
}