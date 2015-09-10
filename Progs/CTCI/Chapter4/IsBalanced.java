public class CheckTreeBalance {
    public isBalanced(Node root) {
        int minDepth = calcMinDepth(root);
        int maxDepth = calcMaxDepth(root);
        
        if (Math.abs(minDepth - maxDepth) > 1) {
            return false;
        }
        
        return true;
    }
    
    int calcMinDepth(Node root) {
        if (root == null) {
            return 0;
        } else {
            return 1 + Math.min(calcMinDepth(root.left), calcMinDepth(root.right));
        }
    }
    
    int calcMaxDepth(Node root, int d) {
        if (root == null) {
            return 0;
        } else {
            return 1 + Math.max(calcMaxDepth(root.left), calcMaxDepth(root.right));
        }
    }
}