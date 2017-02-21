/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class ConstructBinaryTreefromInorderandPostorderTraversal {
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        return buildTree(inorder, 0, inorder.length - 1,
                         postorder, 0, postorder.length - 1);
    }
    
    public TreeNode buildTree(int[] inorder, int i1, int j1, int[] postorder, int i2, int j2) {
        if (j2 == -1) {
            return null;
        }
        
        if (i2 > j2) {
            return null;
        }
        
        if (i1 == j1) {
            return new TreeNode(inorder[i1]);
        }
        
        int inorder_node_idx = find(inorder, postorder[j2]);
        
        int postorder_last_left_idx = i2 + (inorder_node_idx - i1) - 1;
        
        TreeNode node = new TreeNode(inorder[inorder_node_idx]);
        
        node.left = buildTree(inorder, i1, inorder_node_idx - 1, postorder, i2, postorder_last_left_idx);
        node.right = buildTree(inorder, inorder_node_idx + 1, j1, postorder, postorder_last_left_idx + 1, j2 - 1);
        
        return node;
    }
    
    int find(int[] num, int to_find) {
        for (int i = 0; i < num.length; i++) {
            if (num[i] == to_find) {
                return i;
            }
        }
        return -1;
    }
}