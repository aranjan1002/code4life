import java.util.*;

public class Solution {
    public static void main(String[] args) {
	new Solution().test();
    }

    public void test() {
	RandomListNode node1 = new RandomListNode(-1);
	RandomListNode node2 = new RandomListNode(-2);

	node1.next = node2;
	node1.random = node2;
	node2.random = node1;
	// node2.next = node1;

	copyRandomList(node1);
    }

    public RandomListNode copyRandomList(RandomListNode head) {
        if (head == null) {
            return null;
        }
        RandomListNode node = null;
	System.out.println(visitedNodeMap);
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
    
class RandomListNode {
    int label;
    RandomListNode next, random;
    RandomListNode(int x) { this.label = x; }
};
    
    Map<RandomListNode, RandomListNode> visitedNodeMap = new HashMap<RandomListNode, RandomListNode>(); 
}