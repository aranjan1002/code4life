import java.util.*;
import java.io.*;

class SwingingWild {
    public static void main(String[] args) 
    throws Exception {
	new SwingingWild().start();
    }

    public void start() 
    throws Exception {
	InputStreamReader is =
	    new InputStreamReader(
				  new FileInputStream("Input"));
	BufferedReader br = new BufferedReader(is);
        OutputStreamWriter os =
            new OutputStreamWriter(
                                   new FileOutputStream("Output"));
	BufferedWriter bw = new BufferedWriter(os);
	String line;

	int test = 1;
	line = br.readLine();
	while ((line = br.readLine()) != null) {
	    System.out.print("Case #" + test++ + ": ");
	    result = "NO";
	    int numOfRopes = Integer.parseInt(line);
	    inputRopes.clear();
	    for (int i = 0; i < numOfRopes; i++) {
		line = br.readLine();
		//System.out.print ln(n);
		String[] inp = line.split(" ");
		int a = Integer.parseInt(inp[0]);
		int b = Integer.parseInt(inp[1]);
		inputRopes.add(new Rope(a, b));	
	    }	
	    line = br.readLine();
	    destination = Integer.parseInt(line);
	    //System.out.println(destination);
	    Rope r1 = inputRopes.get(0);
	    if (r1.dFromStart > r1.dFromRoot) {
		System.out.println("NO");
	    } else {
		r1.flaggedAt = r1.dFromStart;
		recurse(r1, 0);
		System.out.println(result);
	    }  
	}
    }

    void recurse(Rope r, int idx) {
	if (result.equals("YES")) {
	    return;
	}
	if (r.dFromStart + r.flaggedAt >= destination) {
	    //System.out.println("Rope number: " + idx +
	    //		       " dFromStart: " + r.dFromStart +
	    //		       " flaggedAt: " + r.flaggedAt);
	    result = "YES";
	    return;
	}
	for (int i = idx + 1; i < inputRopes.size(); i++) {
	    Rope r2 = inputRopes.get(i);
	    if (r2.dFromStart - r.dFromStart <= r.flaggedAt) {
		int flagPoint = Math.min(r2.dFromStart - r.dFromStart,
					 r2.dFromRoot);
		if (r2.flaggedAt < flagPoint) {
		    r2.flaggedAt = flagPoint;
		    recurse(r2, i);
		}
	    }
	    else {
		break;
	    }	    
	}
    }
    String result = new String();
    int destination;
    ArrayList<Rope> inputRopes = new ArrayList<Rope>();
    class Rope {
	int dFromStart;
	int dFromRoot;
	int flaggedAt = 0;
	
	Rope(int a, int b) {
	    dFromStart = a;
	    dFromRoot = b;
	}
    }
}