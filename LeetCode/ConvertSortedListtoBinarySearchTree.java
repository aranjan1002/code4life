/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; next = null; }
 * }
 */
/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class ConvertSortedListtoBinarySearchTree {
    public TreeNode sortedListToBST(ListNode head) {
        List<Integer> integer_array = new ArrayList<Integer>();
        
        while(head != null) {
            integer_array.add(new Integer(head.val));
            head = head.next;
        }
        
        int[] num = new int[integer_array.size()];
        for (int i = 0; i < num.length; i++) {
            num[i] = integer_array.get(i).intValue();
        }
        
        return sortedListToBST(num, 0, num.length - 1);
    }
    
    public TreeNode sortedListToBST(int[] num, int i, int j) {
        int mid_node_idx = (i + j) / 2;
        
        if (i > j || i < 0 || j >= num.length) {
            return null;
        } else if (i == j) {
            return new TreeNode(num[i]);
        }
        
        TreeNode node = new TreeNode(num[mid_node_idx]);
        node.left = sortedListToBST(num, i, mid_node_idx - 1);
        node.right = sortedListToBST(num, mid_node_idx + 1, j);
        return node;
    }
}