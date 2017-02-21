/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class SwapNodesinPairs {
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode result = head.next;
        ListNode node1 = head;
        ListNode node2 = head.next;
        ListNode node3 = null, node4 = null;
        
        if (node2.next != null) {
            node3 = node2.next;
            if (node3.next != null) {
                node4 = node3.next;
            }
        }
        
        while(node1 != null && node2 != null) {
            node2.next = node1;
            if (node4 != null) {
                node1.next = node4;
            } else {
                node1.next = node3;
                break;
            }
            
            node1 = node3;
            node2 = node4;
            
            if(node2 != null) {
                node3 = node2.next;
                if (node3 != null) {
                    node4 = node3.next;
                } else {
                    node4 = null;
                }
            } 
        }
        
        return result;
    }
}