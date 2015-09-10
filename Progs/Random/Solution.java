import java.io.*;
import java.lang.*;

public class Solution {
    int P, N;
    int[] curr, des;
    int count=0;
    StringBuilder moves = new StringBuilder();
    
    public static void main(String[] args) 
    throws Exception {
	new Solution().start();
    }

    public void start() throws Exception {
	try {

	BufferedReader br = 
	    new BufferedReader(new InputStreamReader(System.in));
        String[] x = br.readLine().split(" ");
	P = Integer.parseInt(x[1]);
	N = Integer.parseInt(x[0]);
	String[] y = br.readLine().split(" ");
	String[] z = br.readLine().split(" ");
        curr = new int[N+1];
        des = new int[N+1];
	for(int i=0; i<N; i++) {
	    curr[i+1] = Integer.parseInt(y[i]);
	    des[i+1] = Integer.parseInt(z[i]);
	}
        int i;
	System.out.println(getAux(2));
	for(i=N; i>0 && des[i]==curr[i]; i--);
	if(i>0) {
	    moveDisk(curr[i], des[i], getAux(i), i);
        }
	StringBuilder res = new StringBuilder();
	res.append(new Integer(count).toString() + "\n");
	res.append(moves);
	System.out.println(res);
	} catch (Exception e) {
      	    System.err.println("Error:" + e.getMessage());
	    throw new Exception(e);
	}
    }

    public void moveDisk(int from, int to, int aux, int n) {
	System.out.println(n + " " + aux + "." + to);
	if(n>1) {
	    moveDisk(from, aux, getAux(n-1), n-1);
	}
	moves.append(from + " " + to + "\n");
	curr[n] = to;
	count++;
	int i;
        for(i=n-1; i>0 && des[i]==curr[i]; i--);
	if(i>0) {
	    moveDisk(curr[i], des[i], getAux(n-1), i);
	}
    }

    public int getAux(int pos) {
	int[] c = new int[P+1];
	if(des[pos] != des[pos-1]) {
	    return des[pos-1];
	}
	for(int i=pos-2; i>0; i--) {
	    c[des[i]]++;
	}
	c[des[pos]]=1000;
	c[curr[pos]]=1000;
	int min=1000;
	int mini=1;
	for(int i=1; i<=P; i++) {
	    if(c[i] < min) {
		min = c[i];
		mini= i;
	    }
	}
	return mini;
    }
}
