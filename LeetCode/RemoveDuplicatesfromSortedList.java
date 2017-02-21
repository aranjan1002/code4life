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
public class RemoveDuplicatesfromSortedList {
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) {
            return head;
        }
        
        ListNode curr_node = head;
        while (curr_node != null) {
            int curr_val = curr_node.val;
            ListNode prev_node = curr_node;
            curr_node = curr_node.next;
            
            while (curr_node != null && curr_node.val == curr_val) {
                curr_node = curr_node.next;
            }
            prev_node.next = curr_node;
        }
        
        return head;
    }
}