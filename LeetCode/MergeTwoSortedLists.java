/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class MergeTwoSortedLists {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        
        ListNode t = new ListNode(-1);
        ListNode head = t;
        while(l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                t.next = l1;
                l1 = l1.next;
            } else {
                t.next = l2;
                l2 = l2.next;
            }
            t = t.next;
        }
        while (l1 != null) {
            t.next = l1;
            l1 = l1.next;
            t = t.next;
        }
        while (l2 != null) {
            t.next = l2;
            l2 = l2.next;
            t = t.next;
        }
        return head.next;
    }
}