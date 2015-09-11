class LinkedList {
    class Node {
	int value;
	Node next;
	
	Node(int x) {value = x;}
    }

    Node head;
    void insert(int value) {
	if(head == null) {
	    head = new Node(value);
	}
	else {
	    Node temp = head;
	    while(temp.next != null) {
		temp = temp.next;
	    }
	    temp.next = new Node(value);
	}
    }

    void delete(int index) {
	if(index == 0 && head != null) head = head.next;
	else {
	    Node temp = head;
	    int cnt = 0;
	    while(cnt < index - 1 && temp!=null) {
		cnt++;
		temp = temp.next;
	    }
	    if(temp != null && temp.next != null) {
		temp.next = temp.next.next;
	    }
	}
    }

    void display() {
	Node temp = head;
	while(temp != null) {
	    System.out.println(temp.value);
	    temp = temp.next;
	}
    }
}