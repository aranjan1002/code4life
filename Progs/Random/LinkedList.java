class LinkedList {
    LinkedList() {}
    
    void add(int n) {
        Node node = new Node(n);
        if (head == null) {
            head = node;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = node;
        }
    }
    
    boolean remove(int n) {
        Node temp = head;
        if (temp == null) {
            return false;
        }
        if(temp.value == n) {
            head = head.next;
            return true;
        }
        while(temp.next != null) {
	    if (temp.next.value == n) {
		temp.next = temp.next.next;
		return true;
	    }
	    else {
		temp = temp.next;
	    }
        }
        return false;
    }
    
    void print() {
        Node temp = head;
        while (temp != null) {
            System.out.println(temp.value);
            temp = temp.next;
        }
    }
    
    void reverse() {
	if (head == null || head.next == null) {
	    return;
	}
        head = reverse(head);
    }
    
    Node reverse(Node h) {
	if (head == null) {
	    return null;
	}
	return reverse(null, head);
    }

    Node reverse(Node prev, Node h) {
	Node temp = h.next;
	h.next = prev;
	//head = h;
	//print();
	//System.out.println();
	if (temp == null) {
	    return h;
	} else {
	    return reverse(h, temp);
	}
    }
                
    Node head;
    
    class Node {
        int value;
        Node next = null;
        
        Node(int v) {
            value = v;
        }
    }
}