import java.util.*;

class UndirectedGraphNode {
     int label;
     List<UndirectedGraphNode> neighbors;
     UndirectedGraphNode(int x) { 
	 label = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
};
