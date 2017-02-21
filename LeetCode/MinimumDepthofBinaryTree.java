/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class MinimumDepthofBinaryTree {
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            return minDepth2(root);
        }
    }
    
    public int minDepth2(TreeNode root) {
        if (root == null) {
            return Integer.MAX_VALUE;
        } else if (root.left == null && root.right == null) {
            return 1;
        } else {
            return 1 + Math.min(minDepth2(root.left), minDepth2(root.right));
        }
    }
}