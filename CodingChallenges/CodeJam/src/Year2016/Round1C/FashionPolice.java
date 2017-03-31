package Year2016.Round1C;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Anshu on 3/19/2017.
 */
public class FashionPolice {
    public static void main(String[] args)
            throws Exception {
        new FashionPolice().start("input.in");
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
            int a = sc.nextInt();
            int b = sc.nextInt();
            int c = sc.nextInt();
            int k = sc.nextInt();

            // call function and get output
            String result = solve(a, b, c, k);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }

    String solve(int a, int b, int c, int k) {
        int[] limit = new int[]{a, b, c};
        int[] current = new int[]{1, 1, 1};
        HashMap<String, Integer> map = new HashMap<>();
        int count = 0;
        StringBuilder result = new StringBuilder();
        while (true) {
            if (!exceedsCombLimit(current, map, k)) {
                count++;
                result.append(getString(current) + "\n");
            }
            current = increment(limit, current);
            if (current[0] == -1)
            break;
        }
        return count + "\n" + result.substring(0, result.length() - 1).toString();
    }

    boolean exceedsCombLimit(int[] comb, HashMap<String, Integer> map, int k) {
        String comb1 = getComb(comb[0], comb[1], 0);
        if (map.containsKey(comb1) && map.get(comb1) == k) return true;
        String comb2 = getComb(comb[0], 0, comb[2]);
        if (map.containsKey(comb2) && map.get(comb2) == k) return true;
        String comb3 = getComb(0, comb[1], comb[2]);
        if (map.containsKey(comb3) && map.get(comb3) == k) return true;
        if (!map.containsKey(comb1)) map.put(comb1, 0);
        if (!map.containsKey(comb2)) map.put(comb2, 0);
        if (!map.containsKey(comb3)) map.put(comb3, 0);
        map.put(comb1, map.get(comb1) + 1);
        map.put(comb2, map.get(comb2) + 1);
        map.put(comb3, map.get(comb3) + 1);
        return false;
    }

    String getComb(int a, int b, int c) {
        return Integer.toString(a) + " " +
                Integer.toString(b) + " " + Integer.toString(c);
    }

    String getString(int[] comb) {
        String result = "";
        for (int i : comb) result += i + " ";
        return result;
    }

    int[] increment(int[] limit, int[] current) {
        int[] result = new int[]{current[0], current[1], current[2]};
        for (int i = limit.length - 1; i >= 0; i--) {
            if (result[i] < limit[i]) {
                result[i]++;
                for (int j = i + 1; j < limit.length; j++) result[j] = 1;
                return result;
            }
        }
        return new int[]{-1, -1, -1};
    }

    public String solve()
            throws Exception {
        return "";
    }
}
