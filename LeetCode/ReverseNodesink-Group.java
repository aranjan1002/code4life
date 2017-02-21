/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class ReverseNodesink-Group {
    public ListNode reverseKGroup(ListNode head, int k) {
        if (k <= 1) {
            return head;
        }
        boolean foundHead = false;
        ListNode prevTail = null;
        ListNode currHead = head;
        
        while (currHead != null) {
            ListNode[] l = reverseK(currHead, k);
            if (foundHead == false) {
                foundHead = true;
                head = l[0];
            } else {
                prevTail.next = l[0];
            }
            prevTail = currHead;
            currHead = l[1];
        }
        
        return head;
    }
    
    public ListNode[] reverseK(ListNode head, int k) {
        ListNode[] result = new ListNode[2];
        
        if (isLengthAtLeastK(head, k) == false) {
            result[0] = head;
            result[1] = null;
            return result;
        }
        
        int cnt = 1;
        ListNode prev = null;
        ListNode curr = head;
        ListNode next = curr.next;
        
        while (cnt <= k) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
            cnt++;
        }
        
        result[0] = prev;
        result[1] = curr;
        
        return result;
    }
    
    public boolean isLengthAtLeastK(ListNode head, int k) {
        while (head != null && k > 0) {
            k--;
            head = head.next;
        }
        
        return k == 0;
    }
}