import java.util.*;
import java.lang.*;
import java.io.*;

class Password {
    public static void main(String[] args) 
    throws IOException {
	new Password().start();
    }
    
    
    public void start() 
	throws IOException {
	InputStreamReader is =
	new InputStreamReader(
			      new FileInputStream("Input"));
    BufferedReader br = new BufferedReader(is);
        OutputStreamWriter os =
            new OutputStreamWriter(
                                   new FileOutputStream("Output"));
	BufferedWriter bw = new BufferedWriter(os);
	String line;
	int[] inputs;
	double[] p;
	int test = 1;
	line = br.readLine();
	while ((line = br.readLine()) != null) {
	    String[] inp = line.split(" ");
            inputs = new int[inp.length];
            int i = 0;
            for (String s :  inp) {
                inputs[i++] = Integer.parseInt(s);
            }
	    line = br.readLine();
	    inp = line.split(" ");
	    i = 0;
	    p = new double[inp.length];
	    for (String s :  inp) {
                p[i++] = Double.parseDouble(s);
            }
	    a = 0;
	    b = new double[inp.length];
	    result = 1 + inputs[1] + 1;
	    double[] incPos = new double[inp.length];
	    for (i = 0; i < incPos.length; i++) {
		incPos[i] = -1;
	    }
	    //System.out.println(result);
	    recurse(inputs, incPos, 0, -1, p, 1);
	    if (a < result) result = a;
	    for (i = 0; i < incPos.length; i++) 
		if (result > b[i]) result = b[i];
	    System.out.println("Case #" + test + ": " + result);
	    test++;
	    // System.out.println("new test");
	}

    }
    
    void recurse(int[] inputs, double[] incPos, int l,
		 int firstInc, double[] p, double currentP) {
	if (l < inputs[0]) {
	    double[] s = new double[incPos.length];
	    for (int i = 0; i < incPos.length; i++) {
		s[i] = incPos[i];
	    }
	    s[l] = 0;
	    incPos[l] = 1;
	    recurse(inputs, incPos, l+1 , firstInc, p, currentP * p[l]);
	    if (firstInc == -1)
		firstInc = l;
	    recurse(inputs, s, l+1  , firstInc, p, 
		    currentP * (1 - p[l]));
	   
	}
	else {
	    //System.out.println(Arrays.toString(incPos));
	if (firstInc == -1) {
	    a += (inputs[1] - inputs[0] + 1) * currentP;
	}
	else {
	    a += (inputs[1] - inputs[0] + 1 + inputs[1] + 1) * currentP;
	}
	//System.out.println(a);
	for (int i = 0; i < l; i++) {
	    if (firstInc > (l - i - 2) || firstInc == -1) {
		b[i] += (i + 2 + (inputs[1] - (inputs[0] - (i + 1)))) *currentP;
	    }
	    else {
		b[i] += (i + 1 + (inputs[1] - (inputs[0] - (i + 1))) +
			1 + inputs[1] + 1) * currentP;
	    }
	    //System.out.println(i + " " + b[i]);
	}
	}
    }
    double a;
    double[] b;
    double result = 0.0;
}