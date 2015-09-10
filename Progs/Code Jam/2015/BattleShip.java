import java.io.*;
import java.util.*;
import java.lang.*;

class BattleShip {
    public static void main(String[] args) 
	throws Exception {
	new BattleShip().start(args[0]);
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
	int R = Integer.parseInt(line[0]), 
	    C = Integer.parseInt(line[1]), 
	    W = Integer.parseInt(line[2]);
	if (R == 0 || C == 0 || W == 0) {
	    return "0";
	}
	
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
    

}
