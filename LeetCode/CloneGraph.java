/**
 * Definition for undirected graph.
 * class UndirectedGraphNode {
 *     int label;
 *     List<UndirectedGraphNode> neighbors;
 *     UndirectedGraphNode(int x) { label = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
 * };
 */
public class CloneGraph {
public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        if (node == null) {
            return null;
        }
        UndirectedGraphNode new_node = new UndirectedGraphNode(node.label);
        List<UndirectedGraphNode> neighbors = node.neighbors;
        node_map.put(new Integer(new_node.label), new_node);

        for (int i = 0; i <= neighbors.size() - 1; i++) {
            // System.out.println(neighbors.get(i).label + " " +  node.label);
            UndirectedGraphNode neighbor = neighbors.get(i);
            Integer neighbor_label = new Integer(neighbor.label);
            // System.out.println(node_map);
            if (node_map.containsKey(neighbor_label) == false) {
                //System.out.println(neighbor_label);
                new_node.neighbors.add(cloneGraph(neighbor));
            } else {
                new_node.neighbors.add(node_map.get(neighbor_label));
            }
        }
        return new_node;
    }

    Map<Integer, UndirectedGraphNode> node_map = new HashMap<Integer,
        UndirectedGraphNode>();
}