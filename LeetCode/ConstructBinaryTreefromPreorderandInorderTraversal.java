/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class ConstructBinaryTreefromPreorderandInorderTraversal {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length == 0) {
            return null;
        }
        return buildTree(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);   
    }
    
    public TreeNode buildTree(int[] preorder, int i1, int j1, int[] inorder, int i2, int j2) {
        if (i1 > j1) {
            return null;
        }

        int node_val = preorder[i1];
        int node_inorder_idx = find(inorder, node_val);

        int preorder_last_left_idx = i1 + node_inorder_idx - i2;

        TreeNode new_node = new TreeNode(node_val);

        if (inorder[i2] != node_val) {
            new_node.left = buildTree(preorder, i1 + 1, preorder_last_left_idx, inorder, i2, node_inorder_idx - 1);
        }
        if (inorder[j2] != node_val) {
            new_node.right = buildTree(preorder, preorder_last_left_idx + 1, j1, inorder, node_inorder_idx + 1, j2);
        }

        return new_node;
    }
    
    int find(int[] num, int val) {
        for (int i = 0; i < num.length; i++) {
            if (num[i] == val) {
                return i;
            }
        }
        return -1;
    }
}