package Year2017.Round2A;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Anshu on 12/31/2017.
 */
public class StableNeighbors {
    public static void main(String[] args)
            throws Exception {
        new StableNeighbors().start("input.in");
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
            int[][] c = new int[6][2];
            for (int i = 0; i < 6; i++) { c[i][0] = sc.nextInt(); c[i][1] = i; }

            // call function and get output
            String result = solve(n, c);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }

    private boolean check(int[][] c, int i1, int i2) {
        if (c[i2][0] > 0) if (c[i2][0] + 1 > c[i1][0]) return true;
        return false;
    }

    public int non0(int[][] c) {
        int cnt = 0;
        for (int[] a : c) if (a[0] > 0) cnt++;
        return cnt;
    }

    public String solve(int n, int[][] c)
            throws Exception {
        List<String> res = new ArrayList<>();

        for (int i = 0; i < 6; i++) if (c[i][0] > n / 2) return "IMPOSSIBLE";
        if (check(c, 4, 1) || check(c, 0, 3) || check(c, 2, 5)) {
            if (non0(c) > 2) return "IMPOSSIBLE";
            int p, s;
            if (check(c, 4, 1)) { p = 4; s = 1; }
            else if (check(c, 0, 3)) { p = 0; s = 3; }
            else { p = 2; s = 5; }
            if (c[p][0] != c[s][0]) return "IMPOSSIBLE";
            String r = "";
            for (int i = 0; i < c[p][0]; i++) r += Character.toString(cs[p]) + Character.toString(cs[s]);
            return r;
        }

        int size = 0;
        size += getSize(c, 0, 3);
        size += getSize(c, 4, 1);
        size += getSize(c, 2, 5);
//        p(size);
        for (int i = 0; i < 6; i++) if (i % 2 == 0 && c[i][0] > size / 2) return "IMPOSSIBLE";

        for (int i = 0; i < size; i++) res.add("");


        int idx = 0;
        Arrays.sort(c, (o1, o2) -> Integer.compare(o1[0], o2[0]));
        //p(c);
        for (int i = 5; i >= 0; i--) {
            if (c[i][1] % 2 == 1) continue;
            boolean flag = false;
            if (find(c, c[i][1])) flag = true;
            while (c[i][0] > 0) {
//               p(c[i]);
                String s = flag ? getStr(getSecVal(c, c[i][1]), getSec(c[i][1])) : getStr(c[i][0], c[i][1]);
                if (flag) flag = false;
                res.set(idx, s);
                if (c[i][1] % 2 == 0) c[i][0]--;
                else c[i][0] = 0;
                if (idx + 2 >= size) {
                    idx = 1;
                } else {
                    idx = idx + 2;
                }
//                p(c[i]);
            }
//            if (idx > 1) idx--;
//            p(res);
        }

        String result = "";
        for (int i = 0; i < size; i++) {
            result += res.get(i);
        }
        if (n != result.length()) throw new RuntimeException("Invalid");
        return result;
    }

    private boolean find(int[][] c, int i) {
        int toFind = getSec(i);

        for (int [] x : c) {
            if (x[1] == toFind) {
                return x[0] > 0;
            }
        }
        return false;
    }

    private int getSecVal(int[][] c, int k) {
        int secIdx = getSec(k);
        for (int i = 0; i < c.length; i++) {
            if (c[i][1] == secIdx) return c[i][0];
        }
        return -1;
    }

    public int getSec(int i) {
        int toFind = 1;
        if (i == 0) toFind = 3;
        else if (i == 2) toFind = 5;
        return toFind;
    }

    private String getStr(int c, int i) {
        if (i % 2 == 0) return Character.toString(cs[i]);
        String p = getPrimary(i);
        String s = Character.toString(cs[i]);
        String res = p;
        while (c-- > 0) {
            res += s;
            res += p;
        }
        return res;
    }

    private String getPrimary(int i) {
        switch (i) {
            case 1 :
                return "B";
            case 3 :
                return "R";
            case 5:
                return "Y";
            default:
                return "ERROR";
        }
    }

    int getSize(int[][] c, int i1, int i2) {
        if (c[i2][0] > 0) {
            c[i1][0] = c[i1][0] - c[i2][0];
        }
        return c[i1][0];
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

    char[] cs = new char[]{'R', 'O', 'Y', 'G', 'B', 'V'};
}
