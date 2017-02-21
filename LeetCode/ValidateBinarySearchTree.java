/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class ValidateBinarySearchTree {
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        } else {
            return (isValidBST(root.left, Long.MIN_VALUE, root.val) && isValidBST(root.right, root.val, Long.MAX_VALUE));
        }
    }
    
    public boolean isValidBST(TreeNode root, long left_value, long right_value) {
        if (root == null) {
            return true;
        } else {
            if (root.val <= left_value || root.val >= right_value) {
                return false;
            }
            return (isValidBST(root.left, left_value, root.val) && isValidBST(root.right, root.val, right_value));
        }
    }
}