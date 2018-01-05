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
public class MatrixCutting {
    public static void main(String[] args)
            throws Exception {
        new MatrixCutting().start("input.in");
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
            int M = sc.nextInt();
            int[][] mat = new int[N][M];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    mat[i][j] = sc.nextInt();
                }
            }

            // call function and get output
            String result = solve(N, M, mat);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int N, int M, int[][] mat)
            throws Exception {
        result = Long.MIN_VALUE;
        recurse(0, mat[0], new boolean[M - 1], 0);
        return Long.toString(result);
    }

    public void recurse(int sum, int[] mat, boolean[] sliced, int idx) {
        //System.out.println(sum);
        if (idx == sliced.length) {
            result = Math.max(result, sum);
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < sliced.length; i++) {
            min = Math.min(min, mat[i]);
            if (!sliced[i]) {
                sliced[i] = true;
                int origSum = sum;
                int j = i;
                while (j + 1 <= sliced.length) {
                    j++;
                    min = Math.min(min, mat[j]);
                    if (j < sliced.length && sliced[j]) break;
                }
                recurse(sum + min, mat, sliced, idx + 1);
                sliced[i] = false;
                sum = origSum;
            } else min = mat[i + 1];
        }
    }

    long result;

}
