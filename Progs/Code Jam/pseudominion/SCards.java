class SCards {
    int[] sumOfScores = new int[80];
    Node head = null;
    
    public void insert(Card c) {
	Node temp = new Node(c.s);
	if(head == null) {
	    head = new Node(c.s);
	    sumOfScores[0] = c.s;
	}
	else if(head.value < temp.value)
	    {temp.next = head;
		head = temp;
		sumOfScores[0] = c.s;
		sumOfScores[1] = head.next.value;
	    }
	else {
	    Node node = head;
	    Node prevNode = null;
	    while(true) {
		if(node.value < c.s) {
		    prevNode.next = temp;
		    temp.next = node;
		    return;
		}
		prevNode = node;
		node = node.next;
		if(node == null) {
		    prevNode.next = temp;
		    return;
		}
	    }
	}
    }
	
    public int getScore(int index) {
	int sum = 0;
	Node node = head;
	for (int i=0; i<index; i++) {
	    if(node == null) return sum;
	    else {
		sum += node.value;
		node = node.next;
	    }
	}
	return sum;
    }	

    class Node {
	int value;
	Node next = null;

	Node(int x) {value = x;}
    }
}
