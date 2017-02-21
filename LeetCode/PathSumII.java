/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class PathSumII {
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        if (root == null) {
            return result;
        }
        
        List<Integer> list = new ArrayList<Integer>();
        pathSum2(root, sum, list);
        
        return result;
    }
    
    public void pathSum2(TreeNode root, int sum, List<Integer> currList) {
        if (root == null) {
            return;
        }
        
        if (root.left == null && root.right == null) {
            if (root.val == sum) {
                currList.add(new Integer(root.val));
                result.add(currList);
            }
        } else {
            currList.add(root.val);
            List<Integer> currList2 = new ArrayList(currList);
            pathSum2(root.left, sum - root.val, currList);
            pathSum2(root.right, sum - root.val, currList2);
        }
    }
    
    List<List<Integer>> result = new ArrayList<List<Integer>>();
}