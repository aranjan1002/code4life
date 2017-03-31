package Year2014.Round1B;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Anshu on 3/25/2017.
 */
public class TheRepeater {
    public static void main(String[] args)
            throws Exception {
        new TheRepeater().start("input.in");
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
            String[] strs = new String[N];
            for (int i = 0; i < N; i++) strs[i] = sc.next();

            // call function and get output
            String result = solve(strs, N);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(String[] strs, int N)
            throws Exception {
        Pair[] pairs = new Pair[N];
        for (int i = 0; i < N; i++) pairs[i] = getSig(strs[i]);
        String sig = pairs[0].sig;
        int[] min = new int[sig.length()];
        int[] max = new int[sig.length()];
        Arrays.fill(min, Integer.MAX_VALUE);
        Arrays.fill(max, Integer.MIN_VALUE);

        for (int j = 0; j < N; j++) {
            if (pairs[j].sig.equals(sig)) {
                for (int i = 0; i < sig.length(); i++) {
                    min[i] = Math.min(min[i], pairs[j].lens[i]);
                    max[i] = Math.max(max[i], pairs[j].lens[i]);
                }
            } else {
                return "Fegla Won";
            }
        }

        int result = 0;
        for (int k = 0; k < N; k++) {
            int result_for_this_char = Integer.MAX_VALUE;
            for (int i = 0; i < sig.length(); i++) {
                int curr_result = 0;
                for (int j = min[i]; j <= max[i]; j++) {
                    curr_result += Math.abs(pairs[k].lens[i] - j);
                }
                result_for_this_char = Math.min(result_for_this_char, curr_result);
            }
            result += result_for_this_char;
        }
        return Integer.toString(result);
    }

    Pair getSig(String s) {
        String result = "";
        List<Integer> list = new ArrayList<Integer>();
        char prev = '0';
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (prev != c) {
                prev = c;
                result += Character.toString(c);
                list.add(i);
            }
        }
        list.add(s.length());
        int[] lens = new int[result.length()];
        for (int i = 1; i < list.size(); i++) lens[i - 1] = list.get(i) - list.get(i - 1);
        return new Pair(result, lens);
    }

    class Pair {
        String sig;
        int[] lens;
        Pair(String s, int[] l) {sig = s; lens = l;}
    }
}
