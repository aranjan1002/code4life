public Tree commonAncestor(Tree root, Tree p, Tree q) {
    List<Tree> pathP = findPath(root, p);
    List<Tree> pathQ = findPath(roo, q);
    return findLowestCommon(pathP, pathQ);
}

List<Tree> findPath(Tree root, Tree p) {
    List<Tree> result = new ArrayList<Tree>();
    findPath(root, p, result);
    return result;
}

boolean findPath(Tree root, Tree p, List<Tree> result) {
    if (root == null) {
        return false;
    }
    if (p == root) {
        result.add(p);
        return true;
    } else {
        if (findPath(root.left, p, result) == true || findPath(root.right, p, result) == true) {
            result.add(root);
        }
    }
}
        
Tree findLowestCommon(List<Tree> pathP, List<Tree> pathQ) {
    int i = 0;
    while(pathP.get(i) == pathQ.get(i)) {
        i++;
        if (pathP.size() < i + 1 || pathQ.size() < i + 1) {
            return pathP.get(i - 1);
        }
    }
}
             