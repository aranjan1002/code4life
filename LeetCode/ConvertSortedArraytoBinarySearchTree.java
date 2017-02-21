/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class ConvertSortedArraytoBinarySearchTree {
    public TreeNode sortedArrayToBST(int[] num) {
        return sortedArrayToBST(num, 0, num.length - 1);
    }
    
    public TreeNode sortedArrayToBST(int[] num, int i, int j) {
        int mid_node_idx = (i + j) / 2;
        
        if (i > j || i < 0 || j >= num.length) {
            return null;
        } else if (i == j) {
            return new TreeNode(num[i]);
        }
        
        TreeNode node = new TreeNode(num[mid_node_idx]);
        node.left = sortedArrayToBST(num, i, mid_node_idx - 1);
        node.right = sortedArrayToBST(num, mid_node_idx + 1, j);
        return node;
    }
}