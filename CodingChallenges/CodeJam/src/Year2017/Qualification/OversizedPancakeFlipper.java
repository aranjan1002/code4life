package Year2017.Qualification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Anshu on 4/7/2017.
 */
public class OversizedPancakeFlipper {
    public static void main(String[] args)
            throws Exception {
        new OversizedPancakeFlipper().start("input.in");
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
            char[] S = sc.next().toCharArray();
            int K = sc.nextInt();

            // call function and get output
            String result = solve(S, K);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(char[] S, int K)
            throws Exception {
        int len = S.length;
        boolean[] mem = new boolean[len];
        int result = 0;
        int fc = 0;

        for (int i = 0; i < len - K + 1; i++) {
            if (mem[i]) fc--;
            if ((S[i] == '-' && (fc % 2 == 0)) || ((S[i] == '+') && (fc % 2 == 1))) {
                fc++;
                result++;
                if (i + K < len) mem[i + K] = true;
            }
        }

        for (int i = len - K + 1; i < len; i++) {
            if (mem[i]) fc--;
            if ((S[i] == '-' && (fc % 2 == 0)) || ((S[i] == '+') && (fc % 2 == 1))) return "IMPOSSIBLE";
        }

        return Integer.toString(result);
    }
}
