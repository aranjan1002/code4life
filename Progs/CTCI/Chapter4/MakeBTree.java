public static TreeNode addToTree(int arr[], int start, int end) {
    if (start < end) {
        int mid = (start + end) / 2;
    
        TreeNode node = new TreeNode(mid);
    
        node.left = addToTree(arr, start, mid - 1);
        node.right = addToTree(arr, mid + 1, end);
    
        return node;
    } else if (start > end) {
        return null;
    } 
}

public static TreeNode createMinimalBST(int array[]) {
    return addToTree(arr, 0, array.length - 1);
}