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
public class Kicksort {
    public static void main(String[] args)
            throws Exception {
        new Kicksort().start("input.in");
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
            int[] nums = new int[n];
            int idx = 0;
            while (n-- > 0) {
                nums[idx++] = sc.nextInt();
            }

            // call function and get output
            String result = solve(nums);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int[] nums)
            throws Exception {
        List<Integer> list = new ArrayList<>();
        for (int n : nums) list.add(n);
        while (!list.isEmpty()) {
            int midIdx = (list.size() - 1) / 2;
            int midEl = list.get(midIdx);
            int small = 0;
            int large = 0;
            for (int i = 0; i < list.size(); i++) {
                if (i == midIdx) continue;
                int val = list.get(i);
                if (val <= midEl) small++;
                else large++;
            }
            //System.out.println(midEl + " " + small + " " + large);

            if (small != 0 && large != 0) return "NO";
            list.remove(midIdx);
        }

        return "YES";
    }
}
