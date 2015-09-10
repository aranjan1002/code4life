public enum State {
    Unvisited, Visited;
}

public static boolean search(Graph g, Node start, Node end) {
    Stack<Node> q = new Stack<Node>();
    
    if (start.equals(end) == true) {
        return true;
    } else {
        start.State = visited;
        q.add(start);
    }
    
    while(q.isEmpty() == false) {
        Node n = q.pop();
        List<Node> children = n.getChildren();
        for (int i = 0; i <= children.size() - 1; i++) {
            Node child = children.get(i);
            if (child.equals(end) == true) {
                return true;
            } else if (child.State = Unvisited) {
                child.State = Visited;
                q.add(child);
            }
        } 
    }
    
    return false;
}