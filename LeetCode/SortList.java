/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class SortList {
    ListNode merge(ListNode node1, ListNode node2) {
    	ListNode resultNode = new ListNode(0);
    	ListNode tempNode = resultNode;
    	
    	while (node1 != null && node2 != null) {
    		if (node1.val < node2.val) {
    			tempNode.next = node1;
    			node1 = node1.next;
    		} else {
    			tempNode.next = node2;
    			node2 = node2.next;
    		}
    		tempNode = tempNode.next;
    	}
    	while (node1 != null) {
    		tempNode.next = node1;
    		tempNode = tempNode.next;
    		node1 = node1.next;
    	}
    	while (node2 != null) {
    		tempNode.next = node2;
    		node2 = node2.next;
    		tempNode = tempNode.next;
    	}
    	return resultNode.next;
    }

    ListNode mergeSort(ListNode head, int low, int high) {
    	if (low < high) {
    	    //System.out.println(low + " " + high);
    		int mid = (low + high) / 2;
            ListNode node = split(head, low, mid);
            ListNode left = mergeSort(head, low, mid);
            ListNode right = mergeSort(node, mid + 1, high);
            return(merge(left, right));
    	}
    	return head;
    }
    
    ListNode split(ListNode head, int low, int mid) {
    	ListNode result = head;
    	while (low < mid) {
    		result = result.next;
    		low++;
    	}
    	ListNode prev = result;
        result = result.next;
        prev.next = null;
    	return result;
    }
    
    public ListNode sortList(ListNode head) {
        int len = 0;
    	ListNode temp = head;
    	while (temp != null) {
    	    temp = temp.next;
    		len++;
    	}
    	ListNode result = mergeSort(head, 1, len);
    	return result;
    }
}