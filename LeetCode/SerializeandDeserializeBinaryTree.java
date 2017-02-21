/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if (root == null) {
            return "null";
        }
        
        StringBuilder result = new StringBuilder();
        LinkedList<TreeNode> q = new LinkedList<TreeNode>();
        TreeNode dummyNode = new TreeNode(0);
        q.push(root);
        
        while(q.isEmpty() == false) {
            TreeNode n = q.poll();
            if (n == dummyNode) {
                result.append(",null");
            } else {
                result.append(",");
                result.append(Integer.toString(n.val));
                if (n.left != null) {
                    q.offer(n.left);
                } else {
                    q.offer(dummyNode);
                }
                if (n.right != null) {
                    q.offer(n.right);
                } else {
                    q.offer(dummyNode);
                }
            }
        }
        
        return result.toString();
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if (data == null || data.equals("null")) {
            return null;
        }
        
        String[] list = data.split(",");
        LinkedList<TreeNode> q = new LinkedList<TreeNode>();
        TreeNode root = new TreeNode(Integer.parseInt(list[1]));
        q.push(root);
        for (int i = 2; i <= list.length - 1; i++) {
            TreeNode node = q.poll();
            if (list[i].equals("null")) {
                node.left = null;
            } else {
                TreeNode left = new TreeNode(Integer.parseInt(list[i]));
                node.left = left;
                q.offer(left);
            }
            
            if (i + 1 <= list.length - 1) {
                if (list[i + 1].equals("null")) {
                    node.right = null;
                } else {
                    TreeNode right = new TreeNode(Integer.parseInt(list[i + 1]));
                    node.right = right;
                    q.offer(right);
                }
                i++;
            }
        }
        
        return root;
    }
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.deserialize(codec.serialize(root));