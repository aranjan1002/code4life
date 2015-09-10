package networks.project;

class Test {
    RoutingTable routingTable = new RoutingTable("s1-s1:1|");
    public static void main(String[] args) {
	new Test().start();
    }

    public void start() {
	System.out.println(routingTable.toString());
    }
}