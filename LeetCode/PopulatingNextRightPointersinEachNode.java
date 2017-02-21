/**
 * Definition for binary tree with next pointer.
 * public class TreeLinkNode {
 *     int val;
 *     TreeLinkNode left, right, next;
 *     TreeLinkNode(int x) { val = x; }
 * }
 */
public class PopulatingNextRightPointersinEachNode {
    public void connect(TreeLinkNode root) {
        if (root == null) {
            return;
        }
        TreeLinkNode dummyNode = new TreeLinkNode(-1);
        LinkedList<TreeLinkNode> q = new LinkedList<TreeLinkNode>();
        
        q.offer(root);
        q.offer(dummyNode);
        while(q.isEmpty() == false) {
            //System.out.println(q.size());
            TreeLinkNode n = q.poll();
            //System.out.println(n.val);
            if (n == dummyNode && q.isEmpty() == true) break;
            else if (n == dummyNode) {
                System.out.println(q.size());
                q.offer(dummyNode);
                continue;
            }
            if (n.left != null) {
                q.offer(n.left);
            }
            if (n.right != null) {
                q.offer(n.right);
            }
            if (q.element() == dummyNode) {
                n.next = null;
            } else {
                n.next = q.peekFirst();
            }
        }
    }
}