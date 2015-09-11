import java.io.*;
import java.util.*;
import java.lang.*;

class Solution {
    public static void main(String[] args)
        throws Exception {
        new Solution().start(args[0]);
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

    int[] toIntArray(String[] arr) {
	int[] result = new int[arr.length];
	for (int i = 0; i <= arr.length - 1; i++) {
	    result[i] = Integer.parseInt(arr[i]);
	}
	return result;
    }


    public String start2(BufferedReader br)
	throws Exception {
	br.readLine();
        int[] count = toIntArray(br.readLine().split(" "));

	int[] result = new int[2];

	int minSpeed = 0;
	for (int i = 0; i < count.length - 1; i++) {
	    if (count[i + 1] < count[i]) {
		int diff = count[i] - count[i + 1];
		result[0] += diff;
		if (diff > minSpeed) {
		    minSpeed = diff;
		}
	    }
	}

	for (int i = 0; i < count.length - 1; i++) {
	    result[1] += Math.min(count[i], minSpeed);
	}
	String res = Integer.toString(result[0]) + " " + Integer.toString(result[1]);
	return res;
    }
}

	
	