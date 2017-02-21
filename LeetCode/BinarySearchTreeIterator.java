/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */

public class BSTIterator {

    public BSTIterator(TreeNode root) {
        nodeList = new ArrayList<TreeNode>();
        
        if(root != null) {
            addToList(root);
            currIdx = 0;
            size = nodeList.size();
        }
    }
    
    public void addToList(TreeNode node) {
        if (node == null) {
            return;
        }
        
        addToList(node.left);
        nodeList.add(node);
        addToList(node.right);
    }
    
    /** @return whether we have a next smallest number */
    public boolean hasNext() {
        if (currIdx <= size - 1) {
            return true;
        } else {
            return false;
        }
    }

    /** @return the next smallest number */
    public int next() {
        return nodeList.get(currIdx++).val;
    }
    
    List<TreeNode> nodeList;
    int size = -1;
    int currIdx = 0;
}

/**
 * Your BSTIterator will be called like this:
 * BSTIterator i = new BSTIterator(root);
 * while (i.hasNext()) v[f()] = i.next();
 */