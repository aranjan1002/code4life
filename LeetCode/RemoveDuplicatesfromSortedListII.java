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
public class RemoveDuplicatesfromSortedListII {
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) {
            return head;
        }
        List<Integer> dup_list = new ArrayList<Integer>();
        
        int prev_val = head.val;
        ListNode curr_node = head.next;
        while (curr_node != null) {
            if (curr_node.val == prev_val) {
                dup_list.add(new Integer(prev_val));
                while(curr_node != null && curr_node.val == prev_val) {
                    curr_node = curr_node.next;
                }
            }
            if (curr_node != null) {
                prev_val = curr_node.val;
                curr_node = curr_node.next;
            }
        }
        
        head = deleteFromList(head, dup_list);
        return head;
    }
    
    ListNode deleteFromList(ListNode head, List<Integer> dup_list) {
        if (head == null || dup_list.size() == 0) {
            return head;
        }
        ListNode org_head = null;
        boolean head_found = false;
        ListNode prev_node = null;
        int i = 0;
        int val = dup_list.get(i);
        while (i <= dup_list.size() - 1) {   
            if (head.val != val) {
                if (head_found == false) {
                    head_found = true;
                    org_head = head;
                    prev_node = head;
                } else {
                    prev_node = head;
                    head = head.next;
                }
            } else {
                while (head != null && head.val == val) {
                    head = head.next;
                }
                if (prev_node != null) {
                    prev_node.next = head;
                }
                i++;
                if (i <= dup_list.size() - 1) {
                    val = dup_list.get(i);
                }
            }
        }
        if (org_head == null) {
            org_head = head;
        }
        return org_head;
    }
}