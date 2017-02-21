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
public class LinkedListCycleII {
    public ListNode detectCycle(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode slow_ptr = head, fast_ptr = head;
        // Find out if a cycle exists
        do {
            slow_ptr = slow_ptr.next;
            if (fast_ptr != null && fast_ptr.next != null) {
                fast_ptr = fast_ptr.next.next; 
            } else {
                return null;
            }
        } while (slow_ptr != fast_ptr);
        
        int len = 0;
        // Find out the length of the cycle
        do {
            slow_ptr = slow_ptr.next;
            len++;
        } while (slow_ptr != fast_ptr);
        
        // Move fast_ptr at a distance of length of cycle from head
        fast_ptr = head;
        while (len > 0) {
            fast_ptr = fast_ptr.next;
            len--;
        }
        // Return the point where the two pointers meet when then traverse at same speed
        slow_ptr = head;
        while (slow_ptr != fast_ptr) {
            slow_ptr = slow_ptr.next;
            fast_ptr = fast_ptr.next;
        }
        return slow_ptr;
    }
}