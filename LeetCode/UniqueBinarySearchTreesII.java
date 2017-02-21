/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class UniqueBinarySearchTreesII {
    public List<TreeNode> generateTrees(int n) {
        return generateTrees(1, n);
    }
    
    List<TreeNode> generateTrees(int l, int n) {
        System.out.println(l + " " + n);
        List<TreeNode> result = new ArrayList<TreeNode>();
        if (l > n) {
            return result;
        }
        if (l == n) {
            result.add(new TreeNode(l));
            return result;
        }
        for (int i = l; i <= n; i++) {
            List<TreeNode> list1 = generateTrees(l, i - 1);
            List<TreeNode> list2 = generateTrees(i + 1, n);
            if (list1.size() >= 1 && list2.size() >= 1) {
                for (TreeNode left : list1) {
                    for (TreeNode right : list2) {
                        TreeNode root = new TreeNode(i);
                        root.left = left;
                        root.right = right;
                        result.add(root);
                    }
                }
            } else if (list1.size() >= 1) {
                for (TreeNode left : list1) {
                    TreeNode root = new TreeNode(i);
                    root.left = left;
                    result.add(root);
                }
            } else {
                for (TreeNode right : list2) {
                    TreeNode root = new TreeNode(i);
                    root.right = right;
                    result.add(root);
                }
            }
        }
        //System.out.println("Size for " + l + " " + n + ": " + result.size());
        return result;
    }
}