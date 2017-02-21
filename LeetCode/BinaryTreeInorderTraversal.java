/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class BinaryTreeInorderTraversal {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<Integer>();
        if (root == null) return result;
        Stack<TreeNode> s = new Stack<TreeNode>();
        HashSet<TreeNode> set = new HashSet<TreeNode>();
        s.push(root);
        while (s.isEmpty() == false) {
            TreeNode n = s.peek();
            if (n.left == null || set.contains(n.left) == true) {
                s.pop();
                result.add(n.val);
                set.add(n);
                if (n.right != null) {
                    s.push(n.right);
                }
            } else {
                s.push(n.left);
            }
        }
        
        return result;
    }
}