/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class SumRoottoLeafNumbers {
    public int sumNumbers(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            return sumNumbers(root, 0);
        }
    }
    
    public int sumNumbers(TreeNode root, int curr_sum) {
        int left_sum = 0, right_sum = 0;
        if (root.left != null) {
            left_sum = sumNumbers(root.left, curr_sum * 10 + root.val);
        }
        if (root.right != null) {
            right_sum = sumNumbers(root.right, curr_sum * 10 + root.val);
        }
        
        if (root.left == null && root.right == null) {
            return root.val + curr_sum * 10;
        }
        
        return left_sum + right_sum;
    }
}