package Year2017.Round1C;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Anshu on 1/9/2018.
 */
public class AmpleSyrup {
    public static void main(String[] args)
            throws Exception {
        new AmpleSyrup().start("input.in");
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
            int n = sc.nextInt();
            int k = sc.nextInt();
            int[][] rh = new int[n][2];
            for (int i = 0; i < n; i++) {
                rh[i][0] = sc.nextInt();
                rh[i][1] = sc.nextInt();
            }

            // call function and get output
            String result = solve(n, k, rh);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int n, int k, int[][] rh)
            throws Exception {
        Arrays.sort(rh, (o1, o2) -> Integer.compare(o2[0], o1[0]));

        double sa;
        double max_sa = Double.MIN_VALUE;
        for (int i = 0; i <= n - k; i++) {
            sa = Math.PI * (double) rh[i][0] * rh[i][0];
            sa += Math.PI * (double)rh[i][0] * rh[i][1] * 2.0;
            int[][] cp = new int[n - i - 1][2];
            for (int j = i + 1; j < n; j++) {
                cp[j - i - 1][0] = rh[j][0];
                cp[j - i - 1][1] = rh[j][1];
            }
            Arrays.sort(cp, (o1, o2) -> Double.compare(o2[0] * (double) o2[1], o1[0] * (double) o1[1]));
            for (int j = 0; j < k - 1; j++) {
                sa += Math.PI * (double) cp[j][0] * cp[j][1] * 2.0;
            }
            max_sa = Math.max(sa, max_sa);
        }

        return Double.toString(max_sa);
    }

    private void p() {System.out.println(); }

    private void p(int n) {
        System.out.println(n);
    }

    private void p(int[] num) {
        p(Arrays.toString(num));
    }

    private void p(Object o) {
        p(o.toString());
    }

    private void p(String s) {
        System.out.println(s);
    }

    private void p2d(int[][] mat) {
        int r = mat.length;
        if (r == 0) return;
        int c = mat[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void p2d(double[][] mat) {
        int r = mat.length;
        int c = mat[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println();
        }
    }
}
