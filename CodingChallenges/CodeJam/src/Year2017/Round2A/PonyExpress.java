package Year2017.Round2A;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Anshu on 1/1/2018.
 */
public class PonyExpress {
    public static void main(String[] args)
            throws Exception {
        new PonyExpress().start("input.in");
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
            int q = sc.nextInt();
            int[][] h = new int[n][2];
            for (int i = 0; i < n; i++) { h[i][0] = sc.nextInt(); h[i][1] = sc.nextInt(); }
            int[][] m = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    m[i][j] = sc.nextInt();
                }
            }
            int[][] sd = new int[q][2];
            for (int i = 0; i < q; i++) {
                sd[i][0] = sc.nextInt();
                sd[i][1] = sc.nextInt();
            }

            // call function and get output
            String result = solve(n, q, h, m, sd);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int n, int q, int[][] h, int[][] m, int[][] qp)
            throws Exception {
        int[][] sd = applyFW(m, n, -1);
        double[][] tg = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tg[i][j] = Double.MAX_VALUE;
            }
        }

        for (int i = 0; i < n; i++) {
            int e = h[i][0];
            int s = h[i][1];
            for (int j = 0; j < n; j++) {
                if (i != j && sd[i][j] != Integer.MAX_VALUE && e >= sd[i][j]) {
                    tg[i][j] = ((double) sd[i][j]) / s;
                }
            }
        }

        double[][] st = applyFW(tg, n, Double.MAX_VALUE);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < qp.length; i++) {
            sb.append(Double.toString(st[qp[i][0] - 1][qp[i][1] - 1]) + " ");
        }
        return sb.toString();
    }

    int[][] applyFW(int[][] mat, int n, int noEdgeVal) {
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] != noEdgeVal) res[i][j] = mat[i][j];
                else res[i][j] = Integer.MAX_VALUE;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (res[i][j] - res[k][j] > res[i][k]) {
                        res[i][j] = res[i][k] + res[k][j];
                    }
                }
            }
        }
        return res;
    }

    double[][] applyFW(double[][] mat, int n, double noEdgeVal) {
        double[][] res = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] != noEdgeVal) res[i][j] = mat[i][j];
                else res[i][j] = Double.MAX_VALUE;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (res[i][j] - res[k][j] > res[i][k]) {
                        res[i][j] = res[i][k] + res[k][j];
                    }
                }
            }
        }
        return res;
    }

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
