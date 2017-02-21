/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class KthSmallestElementinaBST {
    int curr_idx;
    int val;
    int k = 0;
    public int kthSmallest(TreeNode root, int k) {
        this.k = k;
        kthSmallest(root);
        return val;
    }
    
    public void kthSmallest(TreeNode root) {
        if (root != null) {
            kthSmallest(root.left);
            curr_idx++;
            if (curr_idx == k) {
                val = root.val;
            } else {
                kthSmallest(root.right);
            }
        }
    }
}