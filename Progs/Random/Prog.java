import java.io.*;
import java.lang.*;
import java.util.*;

public class Prog {
    
    class TreeNode {
	int leftW;
	int rightW;
	int[] left;
	int[] right;
	int sum = 0; 
	int leftSol;
	int rightSol;
    }
    public static void main(String[] args) throws Exception {
	new Prog().start();
    }

    int balCount = 0;
    ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();


    public void start() throws Exception {
	InputStreamReader ir = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(ir);
        String[] str;
	
	balCount = Integer.parseInt(br.readLine());
	
	for(int i=0; i<balCount; i++) {
	    TreeNode node = new TreeNode();
	    str = br.readLine().split(" ");
	    node.leftW = Integer.parseInt(str[0]);
	    node.left = new int[str.length - 1];
	    for(int j=1; j<str.length; j++) {
		node.left[j-1] = Integer.parseInt(str[j]);
	    }
	    str = br.readLine().split(" ");
	    node.rightW = Integer.parseInt(str[0]);
	    node.right = new int[str.length - 1];
	    for(int j=1; j<str.length; j++) {
		node.right[j-1] = Integer.parseInt(str[j]);
	    }
	    nodes.add(node);
	    // print(node);
	}
	for(int i=0; i<balCount; i++) {
	    if(nodes.get(i).sum==0) setSumAndSol(nodes.get(i));
	    System.out.println(i + ": " + nodes.get(i).leftSol + " " + nodes.get(i).rightSol);
	}
	//setSumAndSol(nodes.get(0));
	//print(nodes.get(0));
    }

    
    public void setSumAndSol(TreeNode node) {
	if(node.sum!=0) return;
	node.sum=10;
	if(node.left.length == 0 && node.right.length == 0) {
	    if(node.leftW < node.rightW) {
		node.leftSol = node.rightW - node.leftW;
		node.sum += node.rightW + node.rightW;
	    }
	    else {
		node.rightSol = node.leftW - node.rightW;
		node.sum += node.leftW + node.leftW;
	    }
	}
	else {
	    int left=node.leftW, right=node.rightW;
	    for(int i=0; i<node.left.length; i++) {
		setSumAndSol(nodes.get(node.left[i]));
		left += nodes.get(node.left[i]).sum;
	    }
	    for(int i=0; i<node.right.length; i++) {
		setSumAndSol(nodes.get(node.right[i]));
		right += nodes.get(node.right[i]).sum;
	    }
	    if(left < right) {
		node.leftSol = right - left;
		node.sum += right + right;
	    }
	    else {
		node.rightSol = left - right;
		node.sum += left + left;
	    }
	}
    }
			     
	    
    
    public void print(TreeNode node) {
	System.out.println(node.leftW + " " + node.rightW + node.left.length + node.right.length);
	System.out.println(node.leftSol + " " + node.rightSol);
    }
}
