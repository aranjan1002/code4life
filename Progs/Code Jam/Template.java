import java.lang.*;
import java.util.*;
import java.io.*;

class ClassName {
    public static void main(String[] args) 
    throws Exception {
	new ChargingChaos().start(args[0]);
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
	// return null;
    }
}