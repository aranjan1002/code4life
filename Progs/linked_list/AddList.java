class AddList {
    public static void main(String[] args) {
	LinkedList a = new LinkedList();
	a.insert(2);
	a.insert(4);
	a.insert(1);
	a.insert(9);
	a.insert(3);
	LinkedList b = new LinkedList();
	b.insert(3);
	b.insert(9);
	b.insert(1);
	b.insert(4);
	b.insert(1);
	
	new AddList().add(a,b).display();
    }

    public LinkedList add(LinkedList a, LinkedList b) {
	LinkedList result = new LinkedList();
	LinkedList.Node t1 = a.head,  t2 = b.head;
	int x, y, sum, carry = 0;
	while(t1 != null || t2!=null || carry != 0) {
	    if(t1 != null) {
		x = t1.value;
		t1 = t1.next;
	    }
	    else x = 0;
	    if(t2 != null) {
		y = t2.value;
		t2 = t2.next;
	    }
	    else y = 0;
	    sum = x + y + carry;
	    result.insert(sum%10);
	    carry = sum/10;
	}
	return result;
    }
}