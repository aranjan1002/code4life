import java.io.*;
import java.util.*;
import java.lang.*;

class Mushroom {
    public static void main(String[] args)
        throws Exception {
        new Mushroom().start();
    }

    public void start()
        throws Exception {
        InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream("Input2"));
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
            System.out.println("Case #" + test++ + ": " + calc(br));
	}
    }

    public String calc(BufferedReader br) throws IOException {
	String line = br.readLine();
	int num_of_intervals = Integer.parseInt(line);
	long[] num = new long[num_of_intervals];
	String[] mush_at_intervals = br.readLine().split(" ");
	for (int i = 0; i <= num_of_intervals - 1; i++) {
	    num[i] = Long.parseLong(mush_at_intervals[i]);
	}
	
	long result1 = 0;
	long max_diff = 0;
	for (int i = 0; i <= num.length - 2; i++) {
	    if (num[i + 1] < num[i]) {
		long diff = num[i] - num[i + 1];
		result1 = result1 + diff;
		if (diff > max_diff) {
		    max_diff = diff;
		}
	    }
	}
	
	long result2 = 0;

	for (int i = 0; i <= num.length - 2; i++) {
	    if (num[i] <= max_diff) {
		result2 += num[i];
	    } else {
		result2 += max_diff;
	    }
	}

	String result = Long.toString(result1) + " " + Long.toString(result2);
	return result;
    }
}

