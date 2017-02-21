/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class BinaryTreeRightSideView {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<Integer>();
        if (root == null) {
            return result;
        }
        LinkedList<TreeNode> q = new LinkedList<TreeNode>();
        TreeNode dummy = new TreeNode(0);
        q.offer(root);
        q.offer(dummy);
        TreeNode prev = null;
        while (q.isEmpty() == false) {
            TreeNode n = q.poll();
            if (n == dummy && prev != dummy) {
                result.add(prev.val);
                q.offer(dummy);
            } else {
                if (n.left != null) {
                    q.offer(n.left);
                }
                if (n.right != null) {
                    q.offer(n.right);
                }
            }
            prev = n;
        }
        
        return result;
    }
}