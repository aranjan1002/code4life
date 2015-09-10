/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; left = null; right = null; }
 * }
 */

import java.util.*;

public class Solution {
    public static void main(String[] args) {
	System.out.println(new Solution().generateTrees(5).size());;
    }

    public List<TreeNode> generateTrees(int n) {
        List<TreeNode> result = new ArrayList<TreeNode>();
        for (int i = 1; i <= n; i++) {
            result.addAll(treesWithRoot(1, i, n));
        }
        
        return result;
    }
    
    public List<TreeNode> treesWithRoot(int low, int i, int high) {
	System.out.println(low + " " + i + " " + high);
        List<TreeNode> result1 = new ArrayList<TreeNode>();
        List<TreeNode> result2 = new ArrayList<TreeNode>();
        List<TreeNode> result = new ArrayList<TreeNode>();
        
        if (low == i && i == high) {
            TreeNode root_node = new TreeNode(i);
            result.add(root_node);
            return result;
        }
        else {
            for (int k = low + 1; k <= i - 1; k++) {
                result1.addAll(treesWithRoot(low, k, i - 1));
            } 
            
            for (int k = i + 1; k < high; k++) {
                result2.addAll(treesWithRoot(i + 1, k, high));
            }
            
            result.addAll(mergeResult(result1, result2, i));
	    if (low > i) {
		List<TreeNode> left_result = treesWithRoot(low, low, i - 1);
		result.addAll(mergeLeftResult(left_result, i));
	    }
	    if (i < high) {
		List<TreeNode> right_result = treesWithRoot(i + 1, high, high);
		result.addAll(mergeRightResult(right_result, i));
	    }
        }
        // System.out.println(result.size());
        return result;
    }
    
    List<TreeNode> mergeResult(List<TreeNode> result1, List<TreeNode> result2, int rt) {
        List<TreeNode> result = new ArrayList<TreeNode>();
        
        for (int i = 0; i <= result1.size() - 1; i++) {
            TreeNode left_node = result1.get(i);
            for (int j = 0; j <= result2.size() - 1; j++) {
                TreeNode right_node = result2.get(j);
                TreeNode root = new TreeNode(rt);
                root.left = left_node;
                root.right = right_node;
                result.add(root);
            }
        }
        System.out.println(result.size() + " " + result1.size() + "  " + result2.size());
        return result;
    }
    
    List<TreeNode> mergeLeftResult (List<TreeNode> result1, int rt) {
        List<TreeNode> result = new ArrayList<TreeNode>();
        for (int i = 0; i <= result1.size() - 1; i++) {
            TreeNode root_node = new TreeNode(rt);
            root_node.left = result1.get(i);
            result.add(root_node);
        }   
        
        return result;
    }
    
    List<TreeNode> mergeRightResult (List<TreeNode> result2, int rt) {
        List<TreeNode> result = new ArrayList<TreeNode>();
        for (int i = 0; i <= result2.size() - 1; i++) {
            TreeNode root_node = new TreeNode(rt);
            root_node.right = result2.get(i);
            result.add(root_node);
        }   
        
        return result;
    }

    public class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;
	TreeNode(int x) { val = x; left = null; right = null; }
    }

}