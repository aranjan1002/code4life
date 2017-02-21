/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class BinaryTreePostorderTraversal {
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<Integer>();
        if (root == null) {
            return result;
        }
        
        Stack<TreeNode> s = new Stack<TreeNode>();
        s.push(root);
        TreeNode prev = null;
        
        while (s.isEmpty() == false) {
            TreeNode n = s.peek();
            if ((n.right != null && prev == n.right) ||
                (n.right == null && prev == n.left) ||
                (n.right == null && n.left == null)) {
                    prev = n;
                    result.add(n.val);
                    s.pop();
                    continue;
                }
            if (n.right != null) {
                s.push(n.right);
            }
            if (n.left != null) {
                s.push(n.left);
            }
        }
        
        return result;
    }
}