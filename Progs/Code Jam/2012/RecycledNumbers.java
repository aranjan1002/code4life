import java.io.*;
import java.util.*;

class RecycledNumbers {
    public static void main(String[] args)
        throws Exception {
        new RecycledNumbers().start();
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
	    String[] inp = line.split(" ");
            inputs = new int[inp.length];
            int i = 0;
            for (String s :  inp) {
                inputs[i++] = Integer.parseInt(s);
            }
	    System.out.print("Case #" + test + ": ");
            String toWrite = "Case #" + test++ + ": ";
            bw.write(toWrite, 0, toWrite.length());

	    int len = inp[0].length();
	    int first = Integer.parseInt(inp[0]);
	    int second = Integer.parseInt(inp[1]);
	    int result = 0;
    
	    for (int j = first; j < second; j++) {
		String str = Integer.toString(j);
		str += str;
		//System.out.println(str);
		//System.out.println(len);
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (int k = 1; k < len; k++) {
		    int x = Integer.parseInt(str.
					     substring(k, k+len));
		    
		    if (x <= second && x > j) {
			if (!arr.contains(new Integer(x))) {
			    arr.add(new Integer(x));
			    result++;
			}
			//System.out.println(j + " " + x);
		    }
		    //System.out.print(x + " ");
		}
	    }
	    System.out.println(result);
	    String res = Integer.toString(result);
            bw.write(res, 0, res.length());

	    bw.newLine();
	}
	br.close();
	bw.close();
    }
}