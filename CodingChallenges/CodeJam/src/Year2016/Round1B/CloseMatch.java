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
    String firstString = null;
    String secondString = null;

    long getLong(String s) { return Long.parseLong(s);}

    public String solve(String s1, String s2) {
        //System.out.println(s1 + " " + s2);
        firstString = s1;
        secondString = s2;
        long min_diff = Long.MAX_VALUE;
        String[] newS1S2 = new String[] {s1, s2};
        String[] bestS1S2 = new String[] {s1, s2};
        for (int i = 0; i < s1.length(); i++) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if (c1 != '?' && c2 != '?') {
                if (c1 == c2) {
                    continue;
                }
                long diff;
                if ((int) c1 < (int) c2) {
                    diff = getDiff(newS1S2, i, true);
                } else {
                    diff = getDiff(newS1S2, i, false);
                }
                if ((bestS1S2[0].equals(s1) && bestS1S2[1].equals(s2)) ||
                        diff < min_diff ||
                        diff == min_diff && ((getLong(newS1S2[0]) < getLong(bestS1S2[0]) ||
                        (getLong(newS1S2[0]) == getLong(bestS1S2[0]) && getLong(newS1S2[1]) < getLong(bestS1S2[1])))))
                    return newS1S2[0] + " " + newS1S2[1];
                return bestS1S2[0] + " " + bestS1S2[1];
            } else if (c1 == '?' && c2 == '?') {
                newS1S2[0] = setDigit(newS1S2[0], i, 0);
                newS1S2[1] = setDigit(newS1S2[1], i, 1);
                long diff = getDiff(newS1S2, i, true);
                //System.out.println(diff + " " + min_diff);
                if (diff < min_diff  || diff == min_diff && (getLong(newS1S2[0]) < getLong(bestS1S2[0]) ||
                        getLong(newS1S2[0]) == getLong(bestS1S2[0]) && getLong(newS1S2[1]) < getLong(bestS1S2[1]))) {
                    //System.out.println("am here");
                    bestS1S2[0] = newS1S2[0];
                    bestS1S2[1] = newS1S2[1];
                    min_diff = diff;
                }
                newS1S2[0] = setDigit(newS1S2[0], i, 1);
                newS1S2[1] = setDigit(newS1S2[1], i, 0);
                diff = getDiff(newS1S2, i, false);
                if (diff < min_diff || diff == min_diff && (getLong(newS1S2[0]) < getLong(bestS1S2[0]) ||
                        getLong(newS1S2[0]) == getLong(bestS1S2[0]) && getLong(newS1S2[1]) < getLong(bestS1S2[1]))) {
                    bestS1S2[0] = newS1S2[0];
                    bestS1S2[1] = newS1S2[1];
                    min_diff = diff;
                }
                newS1S2[0] = setDigit(newS1S2[0], i, 0);
            } else if (c1 == '?') {
                if ((int) c2 > '0') {
                    newS1S2[0] = setDigit(newS1S2[0], i, (int) c2 - 49);
                    long diff = getDiff(newS1S2, i, true);
                    if (diff < min_diff  || diff == min_diff && (getLong(newS1S2[0]) < getLong(bestS1S2[0]) ||
                            getLong(newS1S2[0]) == getLong(bestS1S2[0]) && getLong(newS1S2[1]) < getLong(bestS1S2[1]))) {
                        bestS1S2[0] = newS1S2[0];
                        bestS1S2[1] = newS1S2[1];
                        min_diff = diff;
                    }
                }
                if ((int) c2 < '9') {
                    newS1S2[0] = setDigit(newS1S2[0], i, (int) c2 - 47);
                    long diff = getDiff(newS1S2, i, false);
                    if (diff < min_diff || diff == min_diff && (getLong(newS1S2[0]) < getLong(bestS1S2[0]) ||
                            getLong(newS1S2[0]) == getLong(bestS1S2[0]) && getLong(newS1S2[1]) < getLong(bestS1S2[1]))) {
                        bestS1S2[0] = newS1S2[0];
                        bestS1S2[1] = newS1S2[1];
                        min_diff = diff;
                    }
                }
                newS1S2[0] = setDigit(newS1S2[0], i, (int) c2 - 48);
            } else {
                if ((int) c1 > '0') {
                    newS1S2[1] = setDigit(newS1S2[1], i, (int) c1 - 49);
                    long diff = getDiff(newS1S2, i, false);
                    if (diff < min_diff  || diff == min_diff && (getLong(newS1S2[0]) < getLong(bestS1S2[0]) ||
                            getLong(newS1S2[0]) == getLong(bestS1S2[0]) && getLong(newS1S2[1]) < getLong(bestS1S2[1]))) {
                        bestS1S2[0] = newS1S2[0];
                        bestS1S2[1] = newS1S2[1];
                        min_diff = diff;
                    }
                }

                if ((int) c1 < '9') {
                    newS1S2[1] = setDigit(newS1S2[1], i, (int) c1 - 47);
                    //System.out.println("made " + newS1S2[1]);
                    long diff = getDiff(newS1S2, i, true);
                    if (diff < min_diff || diff == min_diff && (getLong(newS1S2[0]) < getLong(bestS1S2[0]) ||
                            getLong(newS1S2[0]) == getLong(bestS1S2[0]) && getLong(newS1S2[1]) < getLong(bestS1S2[1]))) {
                        bestS1S2[0] = newS1S2[0];
                        bestS1S2[1] = newS1S2[1];
                        min_diff = diff;
                    }
                }
                newS1S2[1] = setDigit(newS1S2[1], i, (int) c1 - 48);
                //System.out.println(newS1S2[1]);
            }

            //System.out.println(newS1S2[0]+ " " + newS1S2[1]);
        }
       // System.out.println(newS1S2[0]+ " " + newS1S2[1]);
        long diff = Math.abs(Long.parseLong(newS1S2[0]) - Long.parseLong(newS1S2[1]));
        if (diff < min_diff  || diff == min_diff && (getLong(newS1S2[0]) < getLong(bestS1S2[0]) ||
                getLong(newS1S2[0]) == getLong(bestS1S2[0]) && getLong(newS1S2[1]) < getLong(bestS1S2[1]))) {
            bestS1S2[0] = newS1S2[0];
            bestS1S2[1] = newS1S2[1];
            min_diff = diff;
        }
        return bestS1S2[0] + " " + bestS1S2[1];
    }

    long getDiff(String[] s1s2, int i, boolean firstSmaller) {
        String a, b;

        if (firstSmaller) {
            a = s1s2[0].substring(0, i + 1) + firstString.substring(i + 1);
            b = s1s2[1].substring(0, i + 1) + secondString.substring(i + 1);
        } else {
            b = s1s2[0].substring(0, i + 1) + firstString.substring(i + 1);
            a = s1s2[1].substring(0, i + 1) + secondString.substring(i + 1);
        }
        //System.out.println(a + " " + b + " " + i);
         for (int j = i + 1; j < a.length(); j++) {
            if (a.charAt(j) == '?') a = setDigit(a, j, 9);
            if (b.charAt(j) == '?') b = setDigit(b, j, 0);
        }
        s1s2[0] = firstSmaller ? a : b;
        s1s2[1] = firstSmaller ? b : a;
        return Math.abs(Long.parseLong(a) - Long.parseLong(b));
    }

    String setDigit(String s, int idx, int val) {
        char[] s_arr = s.toCharArray();
        s_arr[idx] = Integer.toString(val).charAt(0);
        return new String(s_arr);
    }

//    String prepend(long a, String s) {
//        String a_str = Long.toString(a);
//        if (a_str.length() == s.length()) return a_str;
//        int len1 = a_str.length();
//        int len2 = s.length();
//        for (int i = 0; i < len2 - len1; i++) {
//            a_str = "0" + a_str;
//        }
//        return a_str;
//    }
//
//    String[] getBest(String a, String b, int idx) {
//
//        long min_diff = Long.MAX_VALUE;
//        String newB = b;
//        for (int k = 0; k <= 9; k++) {
//            String temp = setDigit(b, idx, k);
//            if (getDiff(temp, a) < min_diff) {
//                newB = temp;
//                min_diff = getDiff(temp, a);
//            }
//            //System.out.println((temp - a) + " " + temp);
//        }
//        //System.out.println(min_diff);
//        return new String[]{Long.toString(min_diff), newB};
//    }
//
//    long getDiff(String s1, String s2) {
//        return Math.abs(Long.parseLong(s1) - Long.parseLong(s2));
//    }
//
//    String[] getLong(String s1, String s2) {
//        String[] res = new String[2];
//        res[0] = "";
//        res[1] = "";
//        for (int i = 0; i < s1.length(); i++) {
//            char c1 = s1.charAt(i);
//            char c2 = s2.charAt(i);
//
//            if (c1 == '?' && c2 == '?') {
//                res[0] += "0";
//                res[1] += "0";
//            } else if (c1 == '?') {
//                res[1] += (Character.toString(c2));
//                res[0] += (Character.toString(c2));
//            } else if (c2 == '?'){
//                res[1] += (Character.toString(c1));
//                res[0] += (Character.toString(c1));
//            } else {
//                res[0] += (Character.toString(c1));
//                res[1] += (Character.toString(c2));
//            }
//        }
//        return res;
//    }
}
