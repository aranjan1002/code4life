class TestStack {
    public static void main(String[] args) {
	StackWithMin s = new StackWithMin();
	s.push(23);
	s.push(3);
	s.push(12);
	s.push(1);
	s.push(32);
	System.out.println("Popped: " + s.pop());
	System.out.println("Min: " + s.getMin());
	System.out.println("Popped: " + s.pop());
	System.out.println("Min: " + s.getMin());
	System.out.println("Popped: " + s.pop());
	System.out.println("Popped: " + s.pop());
	System.out.println("Min: " + s.getMin());
	System.out.println("Popped: " + s.pop());
        System.out.println("Popped: " + s.pop());
    }
}
	