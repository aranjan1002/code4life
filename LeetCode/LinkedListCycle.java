/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class LinkedListCycle {
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode slowPtr = head;
        ListNode fastPtr = head;
        
        while (slowPtr != null && fastPtr != null) {
            if (slowPtr.next == null) {
                return false;
            }
            slowPtr = slowPtr.next;
            if (fastPtr.next == null || fastPtr.next.next == null) {
                return false;
            }
            fastPtr = fastPtr.next.next;
            
            if (slowPtr == fastPtr) {
                return true;
            }
        }
        return false;
    }
}