public static TreeNode inorderSucc(TreeNode e) {
    if (e.right != null) {
        return leftMostChild(e.right);
    } else {
        if (e.parent == null) {
            return null;
        }
        
        while (e.parent.left != e) {
            e = e.parent;
            if (e.parent == null) {
                return null;
            }
        }
        return e.parent;
    }     
}

public static TreeNode leftMostChild(TreeNode e) {
    while (e.left != null) {
        e = e.left;
    }
    
    return e;
}