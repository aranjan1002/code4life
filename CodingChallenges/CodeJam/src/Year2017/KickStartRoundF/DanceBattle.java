package Year2017.KickStartRoundF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Anshu on 9/24/2017.
 */
public class DanceBattle {
    public static void main(String[] args)
            throws Exception {
        new DanceBattle().start("input.in");
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
            int e = sc.nextInt();
            int n = sc.nextInt();
            int[] s = new int[n];
            for (int i = 0; i < n; i++) {
                s[i] = sc.nextInt();
            }

            // call function and get output
            String result = solve(e, n, s);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int e, int n, int[] s)
            throws Exception {
        List<Integer[]> lineup = new ArrayList<>();
        for (int num : s) lineup.add(new Integer[]{num, 0});

        return Integer.toString(rec(e, n, lineup, 0));
    }

    private int rec(int e, int n, List<Integer[]> lineup, int curr_honor) {
        int honor1 = 0, honor2 = 0, honor3, honor4 = 0;
        if (lineup.isEmpty()) return curr_honor;
        Integer[] first = lineup.get(0);
        List<Integer[]> remList = lineup.subList(1, lineup.size());
        if (lineup.get(0)[0] < e) {
            honor1 = rec(e - lineup.get(0)[0], n, new ArrayList<>(remList), curr_honor + 1);
        }
        if (first[1] == 0) {
            first[1] = 1;
            List<Integer[]> remList2 = new ArrayList<>(remList);
            remList.add(first);
            honor2 = rec(e, n, remList2, curr_honor);
        }
        honor3 = rec(e, n, new ArrayList<>(remList), curr_honor);

        if (curr_honor > 0) {
            honor4 = rec(e + first[0], n, new ArrayList<>(remList), curr_honor - 1);
        }
        int result = Math.max(honor1, Math.max(honor2, Math.max(honor3, honor4)));
//        System.out.println(result + " " + honor1 + " " + honor2 + " " + honor3 + " " + honor4);
        System.out.println(lineup);
         return result;
    }

}
