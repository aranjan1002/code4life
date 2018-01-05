package Year2017.Qualification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Anshu on 12/19/2017.
 */
public class TidyNumbers {
    public static void main(String[] args)
            throws Exception {
        new TidyNumbers().start("input.in");
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
            String N = sc.next();

            // call function and get output
            String result = solve(N.toCharArray());
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(char[] num)
            throws Exception {
        int len = num.length;
        int lastIdx = len;
        for (int i = len - 2; i >= 0; i--) {
            if (num[i] > num[i + 1]) {
                num[i]--;
                lastIdx = i;
            }
        }

        for (int i = lastIdx + 1; i <= len - 1; i++) num[i] = '9';

        return Long.toString(Long.parseLong(new String(num)));
    }
}
