/*
  This class implements the PreemptiveBLDH algorithm. It uses NodeList and Node 
  classes to maintain the "skyline" in the form a linked list. For each job,
  it makes a temporary sorted list (tempList) of nodes where the job is to be
  scheduled by removing (minimum) nodes from the sorted list (sortedList) 
  reprsenting the skyline. It then inserts the tempList into sortedList in 
  time linear to size of sortedList.
 */

package edu.strippacking.BLDH;

import java.util.ArrayList;
import java.util.Collections;

import edu.strippacking.Job;
import edu.strippacking.Constants;

public class PreemptiveBLDH {
    public double getMaxPower(ArrayList<Job> jobList) {
	sortedList = new NodeList(new Node(Constants.getD(), 0));
	tempList = new NodeList();
	Collections.sort(jobList);
	for (int i = 0; i < jobList.size(); i++) {
	    Job j = jobList.get(i);
	    Node curr_node = sortedList.head;	
	    double j_d = j.getDuration();
	    double j_p = j.getPower();
	    //sortedList.displayList();
	    while (j_d > 0) {
		// if code reaches here sortedList should have at least
		// one node
		if (curr_node.duration > j_d) {
		    // split node into two and insert into tempList
		    // this should happen only once per job
		    tempList.insert(new Node(curr_node.duration - 
					     j_d,
					     curr_node.power));
		    tempList.insertAtTail(new Node(j_d,
						   curr_node.power + j_p));
		} else {
		    tempList.insertAtTail(new Node(curr_node.duration,
						    curr_node.power + j_p));
		}
		sortedList.removeAtHead();
		j_d -= curr_node.duration;
		curr_node = curr_node.next;
		sortedList.head = curr_node;
	    }
	    // tempList should not be empty at this point
	    insertTempListIntoSortedList();
	}
	return sortedList.getMaxPower();
    }

    private void insertTempListIntoSortedList() {
	Node tempList_head = tempList.head;
	Node sortedList_head = sortedList.head;
	Node curr_node_to_insert = tempList_head;
	Node temp_node;
	if (sortedList_head == null) {
	    // tempList becomes sortedList
	    sortedList.clone(tempList);
	} else {
	    if (sortedList_head.power > tempList_head.power) {
		// make tempList's head as sortedList's head
		temp_node = tempList_head.next;
		sortedList.insertAtHead(tempList_head);
		curr_node_to_insert = temp_node;
	    }

	    Node curr_node = sortedList.head.next;
	    Node prev_node = sortedList.head;

	    while (curr_node_to_insert != null) {
		temp_node = curr_node_to_insert.next;
		if (curr_node == null) {
		    // insert in the tail
		    sortedList.insertAtTail(curr_node_to_insert);
		} else if (curr_node.power > curr_node_to_insert.power) {
		    // insert in the middle
		    sortedList.insertAfter(prev_node, 
					   curr_node_to_insert);
		} else {
		    prev_node = curr_node;
		    curr_node = curr_node.next;
		    continue;
		}
		prev_node = curr_node_to_insert;
		curr_node_to_insert = temp_node;
		curr_node = prev_node.next;
	    }
	}
	// tempList should now be made empty
	tempList.clear();
    }
    
    //private static final double D = 100;

    NodeList sortedList;
    NodeList tempList;
}