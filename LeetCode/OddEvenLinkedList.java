/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class OddEvenLinkedList {
    public ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode oddList = head;
        ListNode evenList = head.next;
        ListNode temp1 = oddList, temp2 = evenList;
        while (temp1.next != null && temp2.next != null) {
            temp1.next = temp2.next;
            temp1 = temp1.next;
            temp2.next = temp1 != null ? temp1.next : null;
            temp2 = temp2.next;
        }
        temp1.next = evenList;
        return oddList;
    }
}