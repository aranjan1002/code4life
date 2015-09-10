public class All {
    public static void main(String[] args) {

    }

    void deleteDups2(LinkedListNode head) {
	if (head == null) {
	    return;
	}
    
	LinkedListNode runnerNode = null;
	LinkedListNode currNode = head;
    
	while(currNode.next != null) {
	    int currVal = currNode.val;
	    runnerNode = currNode;

	    while (runnerNode.next != null) {
		if (runnerNode.next.val == currVal) {
		    runnerNode.next = runnerNode.next.next;
		} else {
		    runnerNode = runnerNode.next;
		}
	    }
        
	    currNode = currNode.next;
	}
    }

    LinkedListNode nthToLast(LinkedListNode head, int n) {
	LinkedListNode ptr1 = head, ptr2 = head;
    
	int cnt = 1;
	while (cnt < n) {
	    ptr2 = ptr2.next;
	    cnt++;
	}
    
	while (ptr2.next != null) {
	    ptr1 = ptr1.next;
	    ptr2 = ptr2.next;
	}
    
	return ptr1;
    }

    public static boolean deleteNode(LinkedListNode n) {
	if (n == null || n.next == null) {
	    return false;
	}
    
	LinkedListNode next = n.next;
	n.val = next.val;
	n.next = next.next;
	return true;
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
	ListNode result = new ListNode(1);
	ListNode currNode = null, nextNode = result;
     
	if (l1 == null) {
	    return l2;
	}
	if (l2 == null) {
	    return l1;
	}
	boolean firstNode = false;
     
	int carry = 0;
	while (l1 != null || l2 != null || carry > 0) {
	    currNode = nextNode;
	    nextNode = null;
	    int a = 0, b = 0;
	    if (l1 != null) {
		a = l1.val;
		l1 = l1.next;
	    }
	    if (l2 != null) {
		b = l2.val;
		l2 = l2.next;
	    }
	    int sum = a + b + carry;
	    currNode.val = sum % 10;
	    nextNode = new ListNode(0);
	    currNode.next = nextNode;
	    carry = sum / 10;
	}     
     
	currNode.next = null;                  
     
	return result;
    }

    LinkedListNode addLists(LinkedListNode l1, LinkedListNode l2,
			    int carry) {
 
	LinkedListNode result = new LinkedListNode();
	LinkedListNode currNode = null, nextNode = result;
     
	if (l1 == null) {
	    return l2;
	}
	if (l2 == null) {
	    return l1;
	}
     
	carry = 0;
	while (l1 != null || l2 != null || carry > 0) {
	    currNode = nextNode;
	    nextNode = null;
	    int a = 0, b = 0;
	    if (l1 != null) {
		a = l1.val;
		l1 = l1.next;
	    }
	    if (l2 != null) {
		b = l2.val;
		l2 = l2.next;
	    }
	    int sum = a + b + carry;
	    currNode.val = sum % 10;
	    nextNode = new LinkedListNode();
	    prevNode.next = nextNode;
	    carry = sum / 10;
	}     
     
	prevNode.next = null;                  
     
	return result;
    }

    LinkedListNode FindBeginning(LinkedListNode head) {
	if (head == null || head.next == null) {
	    return null;
	}

	LinkedListNode slow_ptr = head;
	LinkedListNode fast_ptr = head.next.next;
    
	while (slow_ptr != fast_ptr && fast_ptr != null) {
	    slow_ptr = slow_ptr.next;
	    if (fast_ptr.next != null) {
		fast_ptr = fast_ptr.next.next;
	    } else {
		return null;
	    }
	}
	int loop_length = 1;
	if (fast_ptr != null) {
	    fast_ptr = fast_ptr.next;
	    while (fast_ptr != slow_ptr) {
		fast_ptr = fast_ptr.next;
		loop_length++;
	    }
	} else {
	    return null;
	}
    
	slow_ptr = head;
	fast_ptr = head;
	int i = 1;
	while (i <= loop_length) {
	    fast_ptr = fast_ptr.next;
	    i++;
	}
    
	while (slow_ptr != fast_ptr) {
	    slow_ptr = slow_ptr.next;
	    fast_ptr = fast_ptr.next;
	}
    
	return slow_ptr;
    }
}