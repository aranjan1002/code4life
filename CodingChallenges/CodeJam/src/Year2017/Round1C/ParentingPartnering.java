package Year2017.Round1C;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Anshu on 1/9/2018.
 */
public class ParentingPartnering {
    public static void main(String[] args)
            throws Exception {
        new ParentingPartnering().start("input.in");
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
            int c = sc.nextInt();
            int j = sc.nextInt();
            int[][] is = new int[c + j][3];
            for (int i = 0; i < c; i++) {
                is[i][0] = sc.nextInt();
                is[i][1] = sc.nextInt();
                is[i][2] = 0;
            }

            for (int i = c; i < c+j; i++) {
                is[i][0] = sc.nextInt();
                is[i][1] = sc.nextInt();
                is[i][2] = 1;
            }

            // call function and get output
            String result = solve(c, j, is);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }

    int curr_min = 15000;

    public String solve(int c, int j, int[][] is)
            throws Exception {
        int ct = 0;
        int jt = 0;
        for (int i = 0; i < c; i++) {
            ct += is[i][1] - is[i][0];
        }

        for (int i = c; i < is.length; i++) {
            jt += is[i][1] - is[i][0];
        }

        Arrays.sort(is, (o1, o2) -> Integer.compare(o1[0], o2[0]));
        is = Arrays.copyOf(is, is.length + 1);
        is[is.length - 1] = Arrays.copyOf(is[0], is[0].length);
        is[is.length - 1][0] += 1440;
        is[is.length - 1][1] += 1440;

        List<Integer> l = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        int result = 0;
        for (int i = 1; i < is.length; i++) {
            if (is[i][2] == is[i-1][2]) {
                if (is[i][2] == 0) l.add(is[i][0] - is[i - 1][1]);
                else l2.add(is[i][0] - is[i - 1][1]);
            } else result++;
        }
        Collections.sort(l);
        int i;
//        p(ct);
        for ( i = 0; i < l.size(); i++) {
            int t = l.get(i);
            if (t + ct <= 720) ct+=t;
            else break;
        }
        result += (l.size() - i) * 2;
        Collections.sort(l2);
        for ( i = 0; i < l2.size(); i++) {
            int t = l2.get(i);
            if (t + jt <= 720) jt+=t;
            else break;
        }
        result += (l2.size() - i) * 2;



        return Integer.toString(result);
    }

    int recurse(int[][] is, int i, int[] t, int e, int b) {
        if (e >= curr_min) return curr_min;
//        p(i < is.length ? is[i] : new int[]{});
//        p(e + " " + curr_min);
//        p(i);
//        p(is.length);
//        p();
        int[] temp = Arrays.copyOf(t, 3);
        if (t[1] > 720 || t[0] > 720) return 15000;
        if (i == is.length) {
            int fB = e % 2 == 0 ? b : (b ^ 1);
            // No exchange
            temp[b] += 1440 - is[i - 1][1];
            int r1;
            if (temp[b] > 720) r1=15000;
            else r1 = e + (b ^ fB);

            // exchange
            b ^= 1;
            int r2 = e + 1 + (b ^ fB);
            curr_min = Math.min(curr_min, Math.min(r1, r2));
            return Math.min(r1, r2);
        }
        if (i == 0) {
            // No exchange
            b = is[0][2] == 0 ? 1 : 0;

            temp[b] = is[0][1];
            int r1 = recurse(is, 1, temp, e, b);

            // Exchange
            int[] t2 = Arrays.copyOf(t, 3);
            t2[2] = is[0][0];
            t2[b] = is[0][1] - is[0][0];
            int r2 = recurse(is, 1, t2, 1, b);
            return Math.min(r1, r2);
        }
        if (is[i][2] == b) {
            b = is[i][2] == 0 ? 1 : 0;
            temp[2] += is[i][0] - is[i - 1][1];
            temp[b] += is[i][1] - is[i][0];

            return recurse(is, i + 1, temp, e + 1, b);
        }

        // No exchange
        temp[b] += is[i][1] - is[i - 1][1];
        int r1 = recurse(is, i + 1, temp, e, b);

        // Exchange
        if (is[i - 1][1] == is[i][0]) return r1;
        int[] t2 = Arrays.copyOf(t, t.length);
        t2[b] += is[i][1] - is[i][0];
        t2[2] += is[i][0] - is[i - 1][1];
        int r2 = recurse(is, i + 1, t2, e + 2, b);
        return Math.min(r1, r2);
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

    private void p() {System.out.println();}

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
