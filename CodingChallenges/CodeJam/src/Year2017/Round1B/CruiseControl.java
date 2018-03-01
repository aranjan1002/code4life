package Year2017.Round1B;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * Created by Anshu on 12/31/2017.
 */
public class CruiseControl {
    public static void main(String[] args)
            throws Exception {
        new CruiseControl().start("input.in");
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
            int d = sc.nextInt();
            int n = sc.nextInt();
            int[][] h = new int[n][2];
            for (int i = 0; i < n; i++) {h[i][0] = sc.nextInt(); h[i][1] = sc.nextInt(); }

            // call function and get output
            String result = solve(d, n, h);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int d, int n, int[][] h)
            throws Exception {
        double maxT = 0;

        for (int i = n - 1; i >= 0; i--) {
            maxT = Math.max(maxT, compute(d, h[i]));
        }

        double minS = d / maxT;

        return Double.toString(minS);
    }

    private double compute(int d, int[] nums) {
        double dist = d - nums[0];
        return dist / nums[1];
    }
}
