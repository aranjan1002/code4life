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
public class PartitionList {
    public ListNode partition(ListNode head, int x) {
        ArrayList<ListNode> nodes_to_shift = new ArrayList<ListNode>();
        
        if (head == null) {
            return head;
        }
        
        ListNode temp_node = head;
        while (temp_node != null) {
            if (temp_node.val >= x) {
                nodes_to_shift.add(temp_node);
            }
            temp_node = temp_node.next;
        }
        
        for (int i = nodes_to_shift.size() - 1; i >= 0; i--) {
            temp_node = nodes_to_shift.get(i);
            shift(temp_node, x);
        }
        
        return head;
    }
    
    void shift(ListNode nodeToShift, int x) {
        ListNode next_node = nodeToShift.next;
        while (next_node != null) {
            if (next_node.val < x) {
                int temp = nodeToShift.val;
                nodeToShift.val = next_node.val;
                next_node.val = temp;
                nodeToShift = next_node;
                next_node = nodeToShift.next;
            } else {
                break;
            }
        }
    }
}