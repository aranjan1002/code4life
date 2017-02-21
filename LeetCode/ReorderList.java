/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class ReorderList {
    public void reorderList(ListNode head) {
        int count = 1;
        if (head == null) {
            return;
        }
        ListNode temp_head = head;
        while(temp_head.next != null) {
            temp_head = temp_head.next;
            count++;
        }
        
        Stack s = new Stack();
        Queue q = new Queue();
        temp_head = head;
        for (int i = 0; i < count/2; i++) {
            q.push(temp_head);
            temp_head = temp_head.next;
        }
        
        while (temp_head != null) {
            s.push(temp_head);
            temp_head = temp_head.next;
        }
        ListNode prev_node = null;
        while (s.isEmpty() == false && q.isEmpty() == false) {
            ListNode node1 = q.pop();
            ListNode node2 = s.pop();
            
            node1.next = node2;
            if (prev_node != null) {
                prev_node.next = node1;
            }
            prev_node = node2;
        }
        
        ListNode leftover_node = null;
        if (q.isEmpty() == false) {
            leftover_node = q.pop();
        }
        if (s.isEmpty() == false) {
            leftover_node = s.pop();
        }
        
        if (prev_node != null) {
            prev_node.next = leftover_node;
            if (leftover_node != null) {
                leftover_node.next = null;
            }
        }
    }
    
    class Stack {
        void push(ListNode node) {
            list.add(node);
            idx++;
        }
        
        boolean isEmpty() {
            if (idx == 0) {
                return true;
            }   
            return false;
        }
        
        ListNode pop() {
            return list.get(--idx);
        }
        
        List<ListNode> list = new ArrayList<ListNode>();
        int idx = 0;
    }
    
    class Queue {
        void push(ListNode node) {
            list.add(node);
        }
        
        boolean isEmpty() {
            if (list.size() == 0 || idx == list.size() - 1) {
                return true;
            }
            return false;
        }
        
        ListNode pop() {
            return list.get(++idx);
        }
        
        List<ListNode> list = new ArrayList<ListNode>();
        int idx = -1;
    }
}