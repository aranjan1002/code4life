package Year2016.Round1A;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * Created by Anshu on 3/5/2017.
 */
public class TheLastWord {
    public static void main(String[] args)
            throws Exception {
        new TheLastWord().start("input.in");
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
            String s = sc.next();
            // call function and get output
            String result = solve(s);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(String s)
            throws Exception {
        String result = Character.toString(s.charAt(0));
        for(int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= result.charAt(0)) {
                result = Character.toString(c) + result;
            } else {
                result += Character.toString(c);
            }
        }
        return result;
    }
}
