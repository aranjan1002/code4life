package Year2017.KickStartRoundG;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Anshu on 10/22/2017.
 */
public class CardsGame {
    public static void main(String[] args)
            throws Exception {
        new CardsGame().start("input.in");
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
            int N = sc.nextInt();
            long[] R = new long[N];
            long[] B = new long[N];
            for (int i = 0; i < N; i++) R[i] = sc.nextLong();
            for (int i = 0; i < N; i++) B[i] = sc.nextLong();

            // call function and get output
            String result = solve(N, R, B);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int N, long[] R, long[] B)
            throws Exception {
        int idx = 0;
        result = Long.MAX_VALUE;
        recurse(0, new boolean[N], R, B, 0);
        //System.out.println("R " + result);
        return Long.toString(result);
    }

    public void recurse(long sum, boolean[] disc, long[] R, long[] B, int idx) {
        //System.out.println(sum + " " + idx);
        if (idx == R.length - 1) {result = Math.min(sum, result); return; }
        for (int i = 0; i < disc.length; i++) {
            if (!disc[i]) {
                for (int j = i + 1; j < disc.length;j++) {
                    if (!disc[j]) {
                        long origSum = sum;
                        sum += (R[i] ^ B[j]);
                        disc[i] = true;
                        recurse(sum, disc, R, B, idx + 1);
                        disc[i] = false;

                        disc[j] = true;
                        recurse(sum, disc, R, B, idx + 1);
                        disc[j] = false;
                        sum = origSum;

                        sum += (B[i] ^ R[j]);
                        disc[i] = true;
                        recurse(sum, disc, R, B, idx + 1);
                        disc[i] = false;

                        disc[j] = true;
                        recurse(sum, disc, R, B, idx + 1);
                        disc[j] = false;
                        sum = origSum;
                    }
                }
            }
        }
    }

    long result = Long.MAX_VALUE;
}
