package pa1;

import pa.InputFileReader;
import pa.TwoIntegers;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Pa1_q1 {
    public static void main(String[] args) throws IOException {
	ArrayList<TwoIntegers> input = new InputFileReader().
	    readTwoIntegersPerLine(args[0]);
	Collections.sort(input, new Comparator<TwoIntegers>() {
		public int compare(TwoIntegers a, TwoIntegers b) {
		    int a_diff = a.int1 - a.int2;
		    int b_diff = b.int1 - b.int2;
		    if (a_diff == b_diff) {
			if (a.int1 >= b.int1) {
			    return -1;
			}
			else {
			    return 1;
			}
		    }
		    
		    return (b_diff - a_diff);
		}
	    });
	    long sum = 0;
	    long curr_time = 0;
	    for (TwoIntegers temp : input) {
		curr_time += temp.int2;
		//System.out.println(temp.int1 + " " + temp.int2);
		System.out.println(sum);
		sum += (temp.int1 * curr_time);
	    }
	    System.out.println(sum);
    }
}