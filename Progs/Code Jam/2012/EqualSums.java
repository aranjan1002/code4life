import java.io.*;
import java.util.*;
import java.lang.*;

class EqualSums {
    public static void main(String[] args) 
	throws Exception {
	new EqualSums().start();
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
	    System.out.println("Case #" + test++ + ": ");
	    String[] inp = line.split(" ");
	    input = new long[500];
	    for (int i = 0; i < 500; i++) {
		//System.out.println(inp[i+1]);
		input[i] = Long.parseLong(inp[i + 1]);
	    }
	    /*
	    for (i = 1; i < 1048576; i++) {
		int sum = 0;
		for (int j = 0; j < 20; j++) {
		    sum += ((i >> j) & 1) * input[j];
		}
		//print(i);
		//System.out.println("Sum: " + sum + " i: " + i);
		Integer num = new Integer(sum);
		if (map.containsKey(num) == true) {
		    // System.out.println("found sum: " + sum);
		    // System.out.println(map.get(num) + " " + i);
		    print(map.get(num));
		    print(i);
		    break;
		}
		else {
		    map.put(num, new Integer(i));
		    // System.out.print("putting into map: " + 
		    //		     sum + 
		    //		     " = " + i + ": ");
		    // print(i);
		}
	    }
	    */
	    Random randomGen = new Random();
	    while (true) {
		Set<Integer> subsetIdx = new HashSet<Integer>();
		long sum = 0;
		for (int i = 0; i < 6; i++) {
		    int idx = randomGen.nextInt(500);
		    Integer idxInt = new Integer(idx);
		    if (subsetIdx.contains(idxInt) == false) {
			subsetIdx.add(new Integer(idx));
			sum += input[idx];
		    }
		    else {
			i--;
		    }
		}
		Long sumInt = new Long(sum);
		if (map.containsKey(sumInt) == true) {
		    Set set = map.get(sumInt);
		    if (set.equals(subsetIdx) == true) {
			continue;
		    }
		    print(subsetIdx);
		    System.out.println();
		    print(map.get(sumInt));
		    break;
		}
		else {
		    map.put(sumInt, subsetIdx);
		}
	    }
	    System.out.println();
	    map.clear();
	}
    }

    void print(Set<Integer> subsetIdx) {
	Iterator it = subsetIdx.iterator();
	while (it.hasNext()) {
	    System.out.print(input[((Integer)it.next()).intValue()] + " ");
	}
	//System.out.println();
    }

    void print(Integer x) {
	print(x.intValue());
    }

    void print(int x) {
	// System.out.println("printing" + x);
	for (int j = 0; j < 20; j++) {
	    //System.out.println(x + " " + ((x >> j) & 1));
	    if (((x >> j) & 1) == 1) {
		System.out.print(input[j] + " ");
	    }
	}
	System.out.println();
    }

    long[] input;
    Map<Long, Set<Integer>> map = new HashMap<Long, Set<Integer>>();
}
