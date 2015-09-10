package edu.strippacking.FFDH;

import java.io.*;
import java.lang.*;

class Main {
    public static void main(String[] args) 
    throws IOException {
	new Main().start(args[0]);
    }

    public void start(String fileName) 
	throws IOException {
	BufferedReader br = getBufferedReader(fileName);
        String line;
	line = br.readLine();
	int total = Integer.parseInt(line);
	int[] widths = new int[total];
	boolean[] is_preemptive = new boolean[total];
	int cnt = 0;
        while ((line = br.readLine()) != null) {
	    String[] str = line.split(" ");
	    widths[cnt] = Integer.parseInt(str[0]);
	    if ("T".equals(str[1])) {
		is_preemptive[cnt] = true;
	    }
	    cnt++;
	}
	System.out.println(mixedFFDHSameHeight.
			   applyMixedFFDHAndGetHeight(widths, 
						      is_preemptive, 
						      MAX_HEIGHT));
    }

    private void print(int[] x, boolean[] y) {
	int l = 0;
	while (l < x.length) {
	    System.out.println(x[l] + " " + y[l]);
	    l++;
	}
    }
	

    private BufferedReader getBufferedReader(String fileName)
        throws IOException {
        InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream(fileName));
        return new BufferedReader(is);
    }

    MixedFFDHSameHeight mixedFFDHSameHeight = new MixedFFDHSameHeight();
    
    private static final int MAX_HEIGHT = 100;
}