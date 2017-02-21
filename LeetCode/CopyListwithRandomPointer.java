/**
 * Definition for singly-linked list with a random pointer.
 * class RandomListNode {
 *     int label;
 *     RandomListNode next, random;
 *     RandomListNode(int x) { this.label = x; }
 * };
 */
public class CopyListwithRandomPointer {
    public RandomListNode copyRandomList(RandomListNode head) {
        if (head == null) {
            return null;
        }
        RandomListNode node = null;
        if (visitedNodeMap.containsKey(head) == false) {
            node = new RandomListNode(head.label);
            visitedNodeMap.put(head, node);
            node.next = copyRandomList(head.next);
            node.random = copyRandomList(head.random);
        } else {
            node = visitedNodeMap.get(head);
        }
        
        return node;
    }
    
    Map<RandomListNode, RandomListNode> visitedNodeMap = new HashMap<RandomListNode, RandomListNode>(); 
}