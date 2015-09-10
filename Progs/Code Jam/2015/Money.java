import java.io.*;
import java.util.*;
import java.lang.*;

class BattleShip {
    public static void main(String[] args) 
	throws Exception {
	new Money().start(args[0]);
    }

    public void start(String fileName)
        throws Exception {
        InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream(fileName));
        BufferedReader br = new BufferedReader(is);
        OutputStreamWriter os =
            new OutputStreamWriter(
                                   new FileOutputStream("Output"));
        BufferedWriter bw = new BufferedWriter(os);
        String line;

        int test = 1;
        line = br.readLine();
        int tot_test = Integer.parseInt(line);
        while (tot_test >= test) {
            System.out.println("Case #" + test++ + ": " + start2(br));
        }
    }


    public String start2(BufferedReader br) 
    throws Exception {
	String[] line = br.readLine().split(" ");
	int C = Integer.parseInt(line[0]), 
	    D = Integer.parseInt(line[1]), 
	    V = Integer.parseInt(line[2]);
	
	int[] denom = getIntArray(br.readlLine().split(" "));

	List<Integer> index = new ArrayList<Integer>();
	for (int i = 0; i <= D - 1; i++) {
	    index.add(new Integer(i));
	}

	List<List<Integer>> = new List<ArrayList<Integer>>();

	if (W == R * C) {
	    return Integer.toString(W);
	}
	
	int result = C / W;
	result += W - 1;
	if (C % W != 0) {
	    result++;
	}

	return Integer.toString(result);

    }
    
    int[] getIntArray(String[] str_array) {
	int[] result = new int[str_array.lenth()];
	
	for (int i = 0; i <= str_array.length() - 1; i++) {
	    result[i] = Integer.parseInt(str_array[i]);
	}

	return result;
    }

}
