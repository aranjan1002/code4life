package Year2017.KickStartRoundE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Anshu on 8/27/2017.
 */
public class BlackHole {
    public static void main(String[] args)
            throws Exception {
        new BlackHole().start("input.in");
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
            int a = sc.nextInt();
            sc.nextInt();
            sc.nextInt();
            int b = sc.nextInt();
            sc.next();
            sc.nextInt();
            int c = sc.nextInt();
            sc.next();
            sc.next();

            // call function and get output
            String result = solve(a, b, c);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int a, int b, int c)
            throws Exception {
        int min = Math.min(a, Math.min(b, c));
        int max = Math.max(a, Math.max(b, c));

        int tot = max - min;
        double result = (tot / 6.0);
        return Double.toString(result);
    }
}
