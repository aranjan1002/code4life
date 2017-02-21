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
public class InsertionSortList {
    public ListNode insertionSortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode curr_node = head.next;
        while (curr_node != null) {
            insert(curr_node, head);
            curr_node = curr_node.next;
        }
        
        return head;
    }
    
    public void insert(ListNode node_to_insert, ListNode head) {
        ListNode curr_node = head;
        int val = node_to_insert.val;
        
        while (curr_node.val < val) {
            curr_node = curr_node.next;
        } 
        while (curr_node != node_to_insert) {
            int temp_val = curr_node.val;
            curr_node.val = val;
            val = temp_val;
            curr_node = curr_node.next;
        }
        curr_node.val = val;
    }
}