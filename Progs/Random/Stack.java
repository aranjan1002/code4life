import java.io.*;

class Stack {
    protected class Node {
        int value;
        Node next;
        
        Node(int v) { 
            value = v;
        }
    }
    protected Node top;
    
    Stack() {}
    
    public void push(int v) {
        Node temp = new Node(v);
        temp.next = top;
        top = temp;
    }
    
    public int pop() {
        if (isEmpty()) {
            return Integer.MIN_VALUE;
        }
        else {
            int result = top.value;
            top = top.next;
            return result;
	}
    }
    
    public boolean isEmpty() {
        return top == null;
    }
}

class StackWithMin extends Stack {
    StackWithMin() {
        super();
    }
    
    @Override
    public void push(int v) {
	NodeWithMin temp = new NodeWithMin(v);
        if (isEmpty()) {
            minNode = temp;
        }
        else {
            if (minNode.value >= v) {
                temp.nextMinNode = minNode;
                minNode = temp;
            }
	}
	temp.next = top;
	top = temp;
    }
    
    @Override
	public int pop() {
        if (isEmpty()) {
            return Integer.MIN_VALUE;
        }
        else {
            if (minNode == top) {
                minNode = minNode.nextMinNode;
            }
        }
	int result = top.value;
	top = top.next;
	return result;
    }
 
    public int getMin() {
	if (isEmpty()) {
	    return Integer.MIN_VALUE;
	}
	else {
	    return minNode.value;
	}
    }
               
    class NodeWithMin extends Node {
        NodeWithMin(int value) {
            super(value);
        }
        
        NodeWithMin nextMinNode;
    }
    protected NodeWithMin minNode;
}