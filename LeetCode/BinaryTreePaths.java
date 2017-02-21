/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class BinaryTreePaths {
    public List<String> binaryTreePaths(TreeNode root) {
        ArrayList<String> result = new ArrayList<String>();
        dfs(root, result, new String());
        return result;
    }
    
    void dfs(TreeNode root, ArrayList<String> result, String curr_path) {
        if (root == null) {
            return;
        }
        curr_path = curr_path + Integer.toString(root.val);
        if (isLeaf(root) == true) {
            result.add(curr_path);
        } else {
            curr_path = curr_path + "->";
            dfs(root.left, result, curr_path);
            dfs(root.right, result, curr_path);
        }
    }
    
    boolean isLeaf(TreeNode root) {
        return (root.left == null && root.right == null);
    }
}