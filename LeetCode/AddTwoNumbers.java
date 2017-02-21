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
public class AddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        SumNode s = new SumNode();
        ListNode r = new ListNode(0);
        ListNode dummy_head = r;
        
        while(l1 != null && l2 != null) {
            s = sum(l1, l2, s.carry);
            r.next = new ListNode(s.sum);
            r = r.next;
            l1 = l1.next;
            l2 = l2.next;
        }
        
        if (l1 == null) {
            l1 = l2;
        }
        
        while (l1 != null) {
            s = sum(l1, s.carry);
            r.next = new ListNode(s.sum);
            r = r.next;
            l1 = l1.next;
        }
        
        if (s.carry != 0) {
            r.next = new ListNode(s.carry);
        }
        
        return dummy_head.next;
    }
    
    public SumNode sum(ListNode a, ListNode b, int carry) {
        int s = a.val + b.val + carry;
        return new SumNode(s%10, s/10);
    }
    
    public SumNode sum(ListNode a, int carry) {
        int s = a.val + carry;
        return new SumNode(s%10, s/10);
    }
    
    public class SumNode {
     int carry = 0;
     int sum;
     SumNode(int s, int c) {
         sum = s;
         carry = c;
     }
     SumNode() {}
    }
}