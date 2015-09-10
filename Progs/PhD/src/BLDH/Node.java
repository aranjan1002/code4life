/* 
   This class represents one segment of skyline is preemptive BLDH packing.
   The segment is defined by its power (height) and duration (width).
 */

package edu.strippacking.BLDH;

import edu.strippacking.Job;

class Node {
    Node(double d, double p) {
	power = p;
	duration = d;
	next = null;
    }

    public String toString() {
	return (power + "x" + duration);
    }

    double duration, power;
    Node next;  // this value is set in class NodeList
}
