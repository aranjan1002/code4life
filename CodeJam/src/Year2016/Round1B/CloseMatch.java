package Year2016.Round1B;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Anshu on 3/11/2017.
 */
public class CloseMatch {
    public static void main(String[] args)
            throws Exception {
        new CloseMatch().start("input.in");
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
            String s1 = sc.next();
            String s2 = sc.next();

            // call function and get output
            String result = solve(s1, s2);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }

    public String solve(String s1, String s2) {
        String[] ab = getLong(s1, s2);
        String a = ab[0];
        String b = ab[1];
        for (int i = 0; i < s1.length(); i++) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if (c1 != '?' && c2 != '?') {
                continue;
            } else if (c1 == '?' && c2 == '?') {
                long min_diff = Long.MAX_VALUE;
                String newA = a, newB = b;
                for (int j = 0; j <= 9; j++) {
                    String temp1 = setDigit(a, i, j);
                    //System.out.println("got " + temp1);
                    String[] res = getBest(temp1, b, i);
                    if (Long.parseLong(res[0]) < min_diff) {
                        min_diff = Long.parseLong(res[0]);
                        newB = res[1];
                        newA = temp1;
                        //System.out.println(min_diff);
                    }
                    //System.out.println(newA + " " + newB);
                }
                a = newA;
                b = newB;
            } else if (c2 == '?') {
                long min_diff = Long.MAX_VALUE;
                String newB = b;
                for (int j = 0; j <= 0; j++) {
                    String[] res = getBest(a, b, i);
                    if (Long.parseLong(res[0]) < min_diff) {
                        min_diff = Long.parseLong(res[0]);
                        newB = res[1];
                    }
                }
                b = newB;
            } else {
                long min_diff = Long.MAX_VALUE;
                String newA = a;
                for (int j = 0; j <= 0; j++) {
                    String[] res = getBest(b, a, i);
                    if (Long.parseLong(res[0]) < min_diff) {
                        min_diff = Long.parseLong(res[0]);
                        newA = res[1];
                    }
                }
                a = newA;
            }
            //System.out.println(a + " " + b);
        }
        return a + " " + b;
    }

    String prepend(long a, String s) {
        String a_str = Long.toString(a);
        if (a_str.length() == s.length()) return a_str;
        int len1 = a_str.length();
        int len2 = s.length();
        for (int i = 0; i < len2 - len1; i++) {
            a_str = "0" + a_str;
        }
        return a_str;
    }

    String[] getBest(String a, String b, int idx) {

        long min_diff = Long.MAX_VALUE;
        String newB = b;
        for (int k = 0; k <= 9; k++) {
            String temp = setDigit(b, idx, k);
            if (getDiff(temp, a) < min_diff) {
                newB = temp;
                min_diff = getDiff(temp, a);
            }
            //System.out.println((temp - a) + " " + temp);
        }
        //System.out.println(min_diff);
        return new String[]{Long.toString(min_diff), newB};
    }

    long getDiff(String s1, String s2) {
        return Math.abs(Long.parseLong(s1) - Long.parseLong(s2));
    }

    String[] getLong(String s1, String s2) {
        String[] res = new String[2];
        res[0] = "";
        res[1] = "";
        for (int i = 0; i < s1.length(); i++) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);

            if (c1 == '?' && c2 == '?') {
                res[0] += "0";
                res[1] += "0";
            } else if (c1 == '?') {
                res[1] += (Character.toString(c2));
                res[0] += (Character.toString(c2));
            } else if (c2 == '?'){
                res[1] += (Character.toString(c1));
                res[0] += (Character.toString(c1));
            } else {
                res[0] += (Character.toString(c1));
                res[1] += (Character.toString(c2));
            }
        }
        return res;
    }

    String setDigit(String s, int idx, int val) {
        char[] s_arr = s.toCharArray();
        s_arr[idx] = Integer.toString(val).charAt(0);
        return new String(s_arr);
    }
}
