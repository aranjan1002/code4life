import java.util.HashSet;
import java.util.List;

class Main {
    public static void main(String[] args) {
	Node node1 = new Node();
	Node node2 = new Node();
	Node node3 = new Node();
	Node node4 = new Node();

	addEdge(node1, node2, 2);
	addEdge(node2, node3, 3);
	addEdge(node3, node1, 4);
	addEdge(node4, node1, 10);
	addEdge(node4, node2, 1);
	
       
	//System.out.println(Prims.getNodeCount(node1, new HashSet<Node>()));
	print(Prims.getMSTUsingPrims(node1));
    }

    public static void addEdge(Node node1, Node node2, int weight) {
	Edge edge1 = new Edge();
	edge1.node1 = node1;
	edge1.node2 = node2;
	edge1.weight = weight;
	node1.edges.add(edge1);
	node2.edges.add(edge1);
    }

    public static void print(List<Edge> edges) {
	System.out.println("Result:");
	for (Edge e : edges) {
	    System.out.println(e.weight);
	}
    }
}
