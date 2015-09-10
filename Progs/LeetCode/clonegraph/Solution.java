/**
 * Definition for undirected graph.
 * class UndirectedGraphNode {
 *     int label;
 *     List<UndirectedGraphNode> neighbors;
 *     UndirectedGraphNode(int x) { label = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
 * };
 */

import java.util.*;
import java.lang.*;

public class Solution {
    public static void main(String[] args) {
	UndirectedGraphNode node1 = new UndirectedGraphNode(1);
	UndirectedGraphNode node2 = new UndirectedGraphNode(2);
	UndirectedGraphNode node3 = new UndirectedGraphNode(3);
	
	node1.neighbors.add(node2);
	node2.neighbors.add(node3);
	node3.neighbors.add(node1);
	
	System.out.println(new Solution().cloneGraph(node1).neighbors.size());
    }

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