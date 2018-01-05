package Year2017.KickStartRoundE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Anshu on 8/27/2017.
 */
public class CopyPaste {
    public static void main(String[] args)
            throws Exception {
        new CopyPaste().start("input.in");
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
            String s = sc.next();

            // call function and get output
            String result = solve(s);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(String s)
            throws Exception {
        return Integer.toString(solve(s, "", 0, 0));
    }

    public int solve(String s, String cb, int idx, int total) {
        //System.out.println(s + " " + cb + " " + idx + " " + total);
        if (s.length() <= idx) return total;
        boolean cbMatches = doesCbMatch(s, cb, idx);
        String newMatch = longestPrefix(s, idx);
        //System.out.println(newMatch);
        if (cbMatches) {
            if (newMatch.length() <= cb.length()) {
                idx += cb.length();
                return solve(s, cb, idx + cb.length(), total + 1);
            }
            return Math.min(solve(s, cb, idx + cb.length(), total + 1), solve(s, newMatch, idx + newMatch.length(), total + 2));
        } else {
            int result1 = solve(s, cb, idx + 1, total + 1);
            int result2 = result1;
            if (newMatch.length() > 0) {
                result2 = solve(s, newMatch, idx + newMatch.length(), total + 2);
            }
            return Math.min(result1, result2);
        }
    }

    boolean doesCbMatch(String s, String cb, int idx) {
        if (cb.length() == 0) return false;
        for (int i = idx; i < s.length() && i - idx < cb.length(); i++) if (s.charAt(i) != cb.charAt(i - idx)) return false;
        return true;
    }

    String longestPrefix(String s, int idx) {
        String result = "";
        for (int i = 0; i < idx; i++) {
            if (s.charAt(i) == s.charAt(idx)) {
                int j = i;
                int k = idx;
                while (j < idx && k < s.length() && s.charAt(j) == s.charAt(k)) {j++; k++;}
                //System.out.println(j + " "  + k + " " + idx);
                if (result.length() < k - idx) {
                    result = s.substring(idx, k);
                }
            }
        }
        return result;
    }

}
