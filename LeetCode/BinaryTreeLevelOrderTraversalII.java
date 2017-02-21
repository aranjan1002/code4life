/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class BinaryTreeLevelOrderTraversalII {
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> curr_level = new ArrayList<Integer>();
        
        if (root == null) {
            return result;
        }
        
        q.push(root);
        q.push(dummy_node);
        
        while(q.isEmpty() == false) {
            TreeNode n = q.pop();
            if (n == dummy_node) {
                result.add(curr_level);
                curr_level =  new ArrayList<Integer>();
                if (q.isEmpty() == true) {
                    break;
                }
                q.push(n);
            } else {
                curr_level.add(new Integer(n.val));
                if (n.left != null) {
                    q.push(n.left);
                }
                if (n.right != null) {
                    q.push(n.right);
                }
            }
        }
        
        return reverse(result);
    }
    
    public List<List<Integer>> reverse(List<List<Integer>> node_list) {
        int head = 0;
        int tail = node_list.size() - 1;
        List<Integer> temp;
        while (head < tail) {
            temp = node_list.get(head);
            node_list.set(head, node_list.get(tail));
            node_list.set(tail, temp);
            head++;
            tail--;
        }
        
        return node_list;
    }
    
    public class Queue {
        void push(TreeNode n) {
            q.add(n);
        }
        
        TreeNode pop() {
            return q.get(idx++);
        }
        
        boolean isEmpty() {
            if (idx <= q.size() - 1) {
                return false;
            }
            return true;
        }
        
        List<TreeNode> q = new ArrayList<TreeNode>();
    }
    
    Queue q = new Queue();
    TreeNode dummy_node = new TreeNode(-1);
    int idx = 0;
}