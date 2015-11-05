import java.lang.*;
import java.util.*;
import java.io.*;

class Dreary {
    public static void main(String[] args)
	throws Exception {
        new Dreary().start(args[0]);
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
            System.out.println("Case #" + test++ + ": " + calc(br));
        }
    }

    long calc(BufferedReader br) throws Exception {
	String[] split = br.readLine().split(" ");
        long k = Long.parseLong(split[0]);
        long v = Long.parseLong(split[1]);
        long result = 0;
	long w = v;
	if (k <= 2 * v) {
	    w = k;
	}

	for (long i = 0; i <= w && i <= k; i++) {
	    for (long j = i - v; j <= i + v; j++) {
		for (long l = Math.max(i, j) - v; l <= Math.min(i, j) + v; l++) {
		    if (j >= 0 && j <= k && l >=0 && l <= k) {
			//System.out.println(i + " " + j + " " + l);
			result++;
		    }
		}
	    }
	}

	if (w == k) {
	    return result;
	}

	result *= 2;
	long sum = 0;
	for (long i = 0; i <= v * 2; i++) {
	    for (long j = 0; j <= v * 2; j++) {
		if (Math.abs(i - j) <= v) {
		    sum++;
		}
	    }
	}

	long leftOver = k + 1 - 2 * (v + 1);
	result += leftOver * sum;
	return result;
    }

    long calc2(BufferedReader br) throws Exception {
        String[] split = br.readLine().split(" ");
	long k = Long.parseLong(split[0]);
	long v = Long.parseLong(split[1]);
	long result = 0;
	long max = 2 * v + 1;
	for (long i = 0; i <= v - 1 && i <= k; i++) {
	    long left = i;
	    long right = Math.min(v, k - i);
	    long sum = left + right + 1;
	    System.out.println(left + " " + right + " " + sum);
	    for (long j = left; j <= right; j++) {
		result += (sum - Math.abs(j - i));
	    }
	}
	System.out.println(result);
	for (long j = k; j >= k - v + 1 && j >= 2 * v - 1; j--) {
	    long left = v;
	    long right = k - j;
	    long sum = left + right + 1;
	    for (long l = left; l <= right; l++) {
		result += (sum - Math.abs(j - l));
	    }
	}

	long leftOver = Math.max(0, k - 2 * v + 1);
	
	result += max * leftOver;
	return result;
    }
}
