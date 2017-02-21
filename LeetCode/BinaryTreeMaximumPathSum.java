/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class BinaryTreeMaximumPathSum {
    public int maxPathSum(TreeNode root) {
        maxPathSum2(root);
        return maxSum;
    }
    
    public int maxPathSum2(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            int left_max_sum = 0, right_max_sum = 0, sum;
            left_max_sum = maxPathSum2(root.left) + root.val;
            right_max_sum = maxPathSum2(root.right) + root.val;
            
            sum = left_max_sum + right_max_sum - root.val;
            
            int max_sum = Math.max(left_max_sum, (Math.max(right_max_sum, root.val)));
            int max_sum_overall = Math.max(max_sum, sum);
            if (max_sum_overall > maxSum) {
                maxSum = max_sum_overall;
            }
            return max_sum;
        }
    }
    
    int maxSum = Integer.MIN_VALUE;
}