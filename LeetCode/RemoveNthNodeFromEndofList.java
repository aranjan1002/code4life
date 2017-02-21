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
public class RemoveNthNodeFromEndofList {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode ptr1 = head, ptr2 = head;
        if (head == null) {
            return null;
        }
        int cnt = 1;
        while (cnt < n) {
            ptr2 = ptr2.next;
            cnt++;
        }
        if (ptr2.next == null) {
            head = head.next;
            return head;
        }
        ListNode prev_node = null;
        while (ptr2.next != null) {
            prev_node = ptr1;
            ptr1 = ptr1.next;
            ptr2 = ptr2.next;
        }
        
        prev_node.next = ptr1.next;
        return head;
    }
}