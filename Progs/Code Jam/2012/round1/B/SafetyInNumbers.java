import java.io.*;
import java.util.*;

class SafetyInNumbers {
    public static void main(String[] args)
        throws Exception {
        new SafetyInNumbers().start();
    }

    void start() 
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
        int[] inputs;
        int test = 1;
        line = br.readLine();
        while ((line = br.readLine()) != null) {
	    //System.out.print("Case #" + test + ": ");
            String toWrite = "Case #" + test++ + ": ";
	    String[] inp = line.split(" ");
	    inputs = new int[inp.length];
	    double j = 0;
	    int cnt = 0;
            for (String s :  inp) {
                inputs[cnt++] = Integer.parseInt(s);
		if (cnt != 1) {
		    j += inputs[cnt - 1];
		}
            }
	    int[] orgInputs;
	    orgInputs = Arrays.copyOf(inputs, inputs.length);
	    Arrays.sort(inputs, 1, inputs.length);
	    //System.out.println("input sorted: " + Arrays.toString(inputs));
	    double[] results = new double[inputs.length];
	    double tot = 0;
	    for (int i = 2; i < inputs.length; i++) {
		double diff = (inputs[i] - inputs[i - 1]) / j;
		//System.out.println("diff: " + diff);
		if ((i - 1) * diff > 1 - tot) {
		    diff = (1 -  tot) / (i -1);
		    addToAll(diff, i - 1, results);
		    tot = 1;
		    break;
		}
		else {
		    addToAll(diff, i - 1, results);
		    tot += diff * (i - 1);
		}
		//System.out.println(Arrays.toString(results));
	    }
	    //System.out.println("tot: " + tot);
	    Map<Integer, Double> resultMap = new HashMap<Integer, Double>();
	    if (tot < 1) {
		//System.out.println("leftover: " + 
		//		   (1.0 - tot) / (inputs.length - 1));
		addToAll((1.0 - tot) / (inputs.length - 1), 
			 inputs.length - 1, 
			 results);
	    }
	    for (int i = 1; i < results.length; i++) {
		resultMap.put(new Integer(inputs[i]), results[i] * 100);
		//toWrite += Double.toString(results[i]) + " ";
	    }
	    for (int i = 1; i < results.length; i++) {
                toWrite += resultMap.get(new Integer(orgInputs[i])).
		    toString() + " ";
            }
	    System.out.println(toWrite);
	    bw.write(toWrite, 0, toWrite.length());
	    bw.newLine();
	}
	br.close();
	bw.close();
    }
    
    public void addToAll(double diff, int j, double[] results) {
	for (int i = 1; i <= j; i++){
	    results[i] += diff;
	}
	//System.out.println(Arrays.toString(results));
    }
}