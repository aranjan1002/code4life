/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class RecoverBinarySearchTree {
    public void recoverTree(TreeNode root) {
        recoverTree2(root);
        if (a != null) {
        int temp = a.val;
        a.val = b.val;
        b.val = temp;
        }
    }
    
    public void recoverTree2(TreeNode root) {
        if (root != null) {
        recoverTree2(root.left);
        if (previousNode != null) {
            if (previousNode.val > root.val) {
                if (b == null) {
                    b = previousNode;
                    a = root;
                } else {
                    a = root;
                }
            }
        } 
        previousNode = root;
        recoverTree2(root.right);
        }
    }
    
    TreeNode previousNode = null;
    TreeNode a = null;
    TreeNode b = null;
}