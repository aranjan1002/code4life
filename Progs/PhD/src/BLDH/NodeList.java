/*
   This class maintains the "skyline" in Preemptive BLDH packing.
   The skyline is represented by a linked list of nodes where each node is
   defined by its power (height) and duration (width).
*/

package edu.strippacking.BLDH;

class NodeList {
    NodeList(){}
    
    NodeList(Node h) {
	head = h;
    }
    
    void add(Node n) {
	if (head == null) {
	    head = n;
	    tail = n;
	} else {
	    Node curr_node = head;
	    while (curr_node.next != null) {
		curr_node = curr_node.next;
	    }
	    curr_node.next = n;
	}
    }

    void insertAtHead(Node n) {
	n.next = head;
	head = n;
	if (tail == null) {
	    tail = head;
	}
    }

    // linearly searches the list and inserts in the appropriate position
    void insert(Node n) {
	if (head == null || head.power > n.power) {
	    insertAtHead(n);
	} else {
	    Node curr_node = head;
	    while (curr_node != null) {
		if (curr_node.next == null) {
		    insertAtTail(n);
		} else if(curr_node.next.power > n.power) {
		    insertAfter(curr_node, n);
		    break;
		}
		curr_node = curr_node.next;
	    }
	}
    }

	

    // insert node_to_insert between n1 and n2
    void insertAfter(Node n1, Node node_to_insert) {
	Node temp_node = n1.next;
	n1.next = node_to_insert;
	node_to_insert.next = temp_node;
	if (temp_node == null) {
	    tail = node_to_insert;
	}
    }

    // assumes node_to_insert are not nulls
    void insertAtTail(Node node_to_insert) {
	if (head == null) {
	    head = node_to_insert;
	    tail = node_to_insert;
	} else {
	    tail.next = node_to_insert;
	    node_to_insert.next = null;
	    tail = node_to_insert;
	}
    }

    double getMaxPower() {
	double max_power = 0;
	Node curr_node = head;
	while (curr_node != null) {
	    double curr_power = curr_node.power;
	    if (curr_power > max_power) {
		max_power = curr_power;
	    }
	    curr_node = curr_node.next;
	}
	return max_power;
    }

    void displayList() {
	Node curr_node = head;
	double tot_duration = 0;
        while (curr_node != null) {
            System.out.print(curr_node + ", ");
	    tot_duration += curr_node.duration;
	    curr_node = curr_node.next;
        }
	System.out.print("Tail: " + tail + " Duration: " + tot_duration);
	System.out.println();
    }

    // head becomes the node after current head
    void removeAtHead() {
	if (head != null) {
	    head = head.next;
	}
    }

    void clone(NodeList nl) {
	if (nl != null) {
	    head = nl.head;
	    tail = nl.tail;
	}
    }

    void clear() {
	head = null;
	tail = null;
    }

    //Node getHead() {
    //return head;
    //}

    Node head = null;
    Node tail = null;
}