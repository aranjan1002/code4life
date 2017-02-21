/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class PalindromeLinkedList {
    public boolean isPalindrome(ListNode head) {
        if (head == null) return true;
        ListNode mid_node = getMidNode(head);
        mid_node = reverseList(mid_node);
        while (mid_node != null) {
            if (head.val != mid_node.val) {
                return false;
            }
            head = head.next;
            mid_node = mid_node.next;
        }
        if (mid_node != null) {
            return false;
        }
        
        return true;
    }
    
    ListNode getMidNode(ListNode head) {
        if (head.next == null) return head;
        int len = 0;
        ListNode temp = head;
        while (temp != null) {
            temp = temp.next;
            len++;
        }
        int mid_idx = len / 2;
        int cnt = 0;
        temp = head;
        while (cnt < mid_idx) {
            temp = temp.next;
            cnt++;
        }
        if (len % 2 != 0) {
            temp = temp.next;
        }
        return temp;
    }
    
    ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        ListNode next = curr;
        while (curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }
}