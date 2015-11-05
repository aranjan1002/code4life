import java.lang.*;
import java.util.*;
import java.io.*;

class IOError {
    public static void main(String[] args)
	throws Exception {
        new IOError().start(args[0]);
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
    
    String calc(BufferedReader br) throws Exception {
        br.readLine();
	String s = br.readLine();
	
	String result = new String();
	for (int i = 0; i <= s.length() - 1; i += 8) {
	    int c = 0;
	    for (int j = 0; j <= 7; j++) {
		if (s.charAt(j + i) == 'I') {
		    c = (c << 1) | 1;
		} else {
		    c = c << 1;
		}
	    }
	    result = result + Character.toString((char) c);
	    //System.out.println(result);
	}
   
	return result;
    }
}
