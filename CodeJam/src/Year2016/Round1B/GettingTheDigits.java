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
public class GettingTheDigits {
    public static void main(String[] args)
            throws Exception {
        new GettingTheDigits().start("input.in");
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
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!map.containsKey(c)) {
                map.put(c, 1);
            } else {
                map.put(c, map.get(c) + 1);
            }
        }

        int[] result = new int[10];
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(chars[i])) {
                //System.out.println(chars[i] + " " + map.containsKey(chars[i]));
                int count = map.get(chars[i]);
                //System.out.println(chars[i] + " " + count);
                if (count > 0) {
                    result[nums[i]] = count;
                    String str = words[i];
                    for (int j = 0; j < str.length(); j++) {
                        map.put(str.charAt(j), map.get(str.charAt(j)) - count);
                        if (map.get(str.charAt(j)) < 0) System.out.println("Wrong");
                    }
                }
            }
        }

        String res = new String();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i]; j++) {
                res += Integer.toString(i);
            }
        }
        return res;
    }

    int[] nums = new int[]{0, 2, 4, 6, 8, 1, 3, 5, 7, 9};
    char[] chars = new char[]{'Z', 'W', 'U', 'X', 'G', 'O', 'T', 'F', 'S', 'I'};
    String[] words = new String[] {"ZERO", "TWO", "FOUR", "SIX", "EIGHT", "ONE", "THREE", "FIVE", "SEVEN", "NINE"};
}
