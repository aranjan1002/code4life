package Year2017.Round1C;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Anshu on 2/15/2018.
 */
public class CoreTraining {
    public static void main(String[] args)
            throws Exception {
        new CoreTraining().start("input.in");
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
            double u = sc.nextDouble();
            double[] p = new double[n];
            for (int i = 0 ; i < n; i++) p[i] = sc.nextDouble();

            // call function and get output
            String result = solve(n, k, u, p);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int n, int k, double u, double[] p)
            throws Exception {
        Arrays.sort(p);
        double th = 0.5;
        //p(p);
        while (u > 0.000006) {
            int toInc = countLessThan(p, th);
            double incBy = u / toInc;
            for (int i = 0; i < toInc; i++) { u -= Math.min(incBy, th - p[i]); p[i] += Math.min(incBy, th - p[i]); }
            while (toInc < p.length && p[toInc] == th) toInc++;
            if (toInc == p.length) th = 1;
            else th = p[toInc];
            //p(p);
        }

        double res = 1;
        for (double pr : p) res *= pr;

        return Double.toString(res);
    }

    int countLessThan(double[] p, double n) {
        int res = 0;
        for (double d : p) if (d < n) res++; else return res;
        return res;
    }

    private void p(int n) {
        System.out.println(n);
    }

    private void p(int[] num) {
        p(Arrays.toString(num));
    }

    private void p(double[] num) {
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
