public class Paranthesis {
    void printParanthesis(int n) {
	String s = new String();
	printParanthesis(s, 0, 0, n);
    }

    void printParanthesis(String s, int start, int close, int n) {
	if (close == n) {
	    System.out.println(s);
	}
	if (close < start) {
	    printParanthesis(s + ")", start, close + 1, n);
	}
	if (start < n) {
	    printParanthesis(s + "(", start + 1, close, n);
	}
    }

    public static void main(String[] args) {
	new Paranthesis().printParanthesis(5);
    }
}