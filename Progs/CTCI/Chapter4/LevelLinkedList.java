ArrayList<LinkedList<TreeNode>> findLevelLinkList(TreeNode root) {
    Queue<TreeNode> q = new Queue<TreeNode>();
    
    ArrayList<LinkedList<TreeNode>> result = new ArrayList<LinkedList<TreeNode>>();
    
    if (root == null) {
        return result;
    }
    
    TreeNode dummy_node = new TreeNode();
    
    q.push(root);
    
    while (q.size() > 1) {
        LinkedList<TreeNode> list = new LinkedList<TreeNode>();
        
        TreeNode node = q.pop();
        while (node.equals(dummy_node) == false) {
	    list.add(node);
	    addChildren(q, node);
	    node = q.pop();   
        }
        
        q.push(dummy_node);
        result.add(list);
    }
    
    return result;
}