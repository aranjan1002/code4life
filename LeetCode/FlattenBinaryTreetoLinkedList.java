/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class FlattenBinaryTreetoLinkedList {
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        flatten2(root);
    }
    
    public TreeNode flatten2(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode temp_node = root.right;
        root.right = flatten2(root.left);
        root.left = null;
        TreeNode rightmost_node = root;
        while (rightmost_node.right != null) {
            rightmost_node = rightmost_node.right;
        }
        rightmost_node.right = flatten2(temp_node);
        return root;
    }
}