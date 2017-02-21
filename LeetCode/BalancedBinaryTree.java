/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class BalancedBinaryTree {
    public boolean isBalanced(TreeNode root) {
        updateDepth(root);
        if (maxDepthDiff > 1) {
            return false;
        }
        return true;
    }
    
    int updateDepth(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return 1;
        } else {
            int left_depth = updateDepth(root.left);
            int right_depth = updateDepth(root.right);
            int max_depth = max(left_depth, right_depth);
            int diff = Math.abs(left_depth - right_depth);
            if (diff > maxDepthDiff) {
                maxDepthDiff = diff;
            }
            return (max_depth + 1);
        }
    }
    
    int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
    }
    
    int maxDepthDiff = 0;
}