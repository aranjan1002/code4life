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
public class ReverseLinkedListII {
    public ListNode reverseBetween(ListNode head, int m, int n) {
        ListNode org_head = head;
        ListNode prev_to_head = null, head_of_sublist = null, tail_of_sublist = null, next_to_tail = null;
        
        int i = 1;
        while(i < m - 1) {
            head = head.next;
            i++;
        }
        
        if (m == 1) {
            head_of_sublist = head;
        } else {
            prev_to_head = head;
            head_of_sublist = head.next;
        }
        while (i < n) {
            head = head.next;
            i++;
        }
        
        tail_of_sublist = head;
        next_to_tail = head.next;
        
        tail_of_sublist.next = null;
        reverse(head_of_sublist);
        if (prev_to_head != null) {
            prev_to_head.next = tail_of_sublist;
        }
        head_of_sublist.next = next_to_tail;
        
        if (m != 1) {
            return org_head;
        } else {
            return tail_of_sublist;
        }
    }
    
    public void reverse(ListNode head) {
        ListNode node1 = null;
        ListNode node2 = head;
        ListNode next_node = null;
        
        while (node2 != null) {
            next_node = node2.next;
            node2.next = node1;
            node1 = node2;
            node2 = next_node;
        }
    }
}