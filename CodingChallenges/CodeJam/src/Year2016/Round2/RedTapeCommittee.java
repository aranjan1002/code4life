package Year2016.Round2;

import java.io.*;
import java.util.*;
import java.lang.*;

class RedTapeCommittee {
    public static void main(String[] args)
            throws Exception {
        new RedTapeCommittee().start("input.in");
    }

    public void start(String fileName)
            throws Exception {
        Scanner sc = new Scanner(new File(fileName));
        OutputStreamWriter os =
                new OutputStreamWriter(
                        new FileOutputStream("Output"));
        BufferedWriter bw = new BufferedWriter(os);

        int tot_test = Integer.parseInt(sc.next());
        int loop = tot_test;
        while (loop-- > 0) {
            // Code to read input

            // call function and get output
            String result = solve();
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve()
            throws Exception {
        return "";
    }
}
