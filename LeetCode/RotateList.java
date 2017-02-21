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
public class RotateList {
    public ListNode rotateRight(ListNode head, int n) {
        if (head == null || head.next == null) {
            return head;
        }
        
        int len = count(head);
        n = n % len;
        
        for (int i = 1; i <= n; i++) {
            ListNode curr_node = head;
            int prev_value = head.val;
            
            while(curr_node.next != null) {
                curr_node = curr_node.next;
                int temp_value = curr_node.val;
                curr_node.val = prev_value;
                prev_value = temp_value;
            }
            
            head.val = prev_value;
        }
        
        return head;
    }
    
    int count (ListNode head) {
        int cnt = 0;
        while (head != null) {
            head = head.next;
            cnt++;
        }
        
        return cnt;
    }
}