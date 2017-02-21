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
public class IntersectionofTwoLinkedLists {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        
        int countA = 0;
        int countB = 0;
        ListNode tempA = headA;
        ListNode tempB = headB;
        
        while (tempA != null) {
            countA++;
            tempA = tempA.next;
        }
        while (tempB != null) {
            countB++;
            tempB = tempB.next;
        }
        
        int diff = Math.abs(countA - countB);
        tempA = headA;
        tempB = headB;
        
        if (countA > countB) {
            while(diff > 0) {
                tempA = tempA.next;
                diff--;
            }
        } else if (countB > countA) {
            while (diff > 0) {
                tempB = tempB.next;
                diff--;
            }
        }
        ListNode intersecting_node = null;
        while (tempA != null && tempB != null) {
            if (tempA == tempB) {
                intersecting_node = tempA;
                break;
            }
            tempA = tempA.next;
            tempB = tempB.next;
        }
        
        return intersecting_node;
    }
}