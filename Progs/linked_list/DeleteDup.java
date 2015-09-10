import java.util.*;

class DeleteDup {
    public static void main(String[] args) {
	LinkedList list = new LinkedList();
	list.insert(1);
	list.insert(2);
	list.insert(23);
	list.insert(2);
	list.insert(235);
	list.insert(1);
        //list.head = new DeleteDup().deleteDupWithoutDS(list.head);
	LinkedList.Node n = new DeleteDup().findNthToLast(list.head, 10);
	System.out.println(n != null? n.value : null);
	//list.display();
    }

    public void deleteDup(LinkedList list) {
	HashMap<Integer, String> map = new HashMap<Integer, String>();
	LinkedList.Node temp = list.head;
	map.put(temp.value, null);
	if(temp == null) return;
	while(temp!=null && temp.next != null) {
	    list.display();
	    System.out.println("Done");
	    if(map.containsKey(temp.next.value)) {
		temp.next = temp.next.next;
	    }
	    else {
		map.put(temp.next.value, null);
	    }
	    temp = temp.next;
	}
    }
    
    public LinkedList.Node deleteDupWithoutDS(LinkedList.Node head) {
	if(head == null) {
	    return null;
	}
	
	LinkedList.Node prevNode = null, ptr1 = head, ptr2 = head.next;
	while(ptr2 != null) {
	    ptr1 = head;
	    while(ptr1 != ptr2) {
		if(ptr1.value == ptr2.value) {
		    if(ptr1 == head) {
			head = ptr1.next;
		    }
		    else {
			prevNode.next = ptr1.next;
		    }
		}
		else prevNode = ptr1;
		ptr1 = ptr1.next;
	    }
	    ptr2 = ptr2.next;
	}
	return head;
    }

    public LinkedList.Node findNthToLast(LinkedList.Node head, int n) {
	LinkedList.Node ptr1 = head;
	int cnt = 1;
	while(cnt <= n - 1) {
	    if(ptr1 == null) return null;
	    ptr1 = ptr1.next;
	    cnt++;
	}
	LinkedList.Node ptr2 = head;
	while(ptr1.next != null) {
	    ptr1 =  ptr1.next;
	    ptr2 = ptr2.next;
	}
	return ptr2;
    }

}