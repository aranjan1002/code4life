boolean containsTree(TreeNode t1, TreeNode t2) {
    if (t2 == null) {
        return true;
    } else {
        return findSubtree(t1, t2);
    }
}

boolean findSubtree(TreeNode t1, TreeNode t2) {
    if (t1 == null) {
        return false;
    }
    if (t1 == t2) {
        if (checkSubtree(t1, t2) == true) {
            return true;
        }
    } else {
        findSubtree(t1.left, t2);
        findSubtree(t1.right, t2);
    }
    
    return false;
}    
        
boolean checkSubtree(TreeNode t1, TreeNode t2) {
    if (t1 != t2) {
        return false;
    }
    if (t1 == null && t2 == null) {
        return true;
    }
    if (t1 == null || t2 == null) {
        return false;
    }
    return (checkSubtree(t1.left, t2.left) && checkSubtree(t1.right, t2.right));
}