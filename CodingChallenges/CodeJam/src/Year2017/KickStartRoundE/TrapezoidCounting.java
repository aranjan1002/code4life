package Year2017.KickStartRoundE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Anshu on 8/27/2017.
 */
public class TrapezoidCounting {
    public static void main(String[] args)
            throws Exception {
        new TrapezoidCounting().start("input.in");
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
            int[] nums = new int[sc.nextInt()];
            for (int i = 0; i < nums.length; i++) nums[i] = sc.nextInt();

            // call function and get output
            String result = solve(nums);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int[] nums)
            throws Exception {
        Arrays.sort(nums);
        int tot_uniq = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            if (!map.containsKey(num)) {
                tot_uniq++;
                map.put(num, 1);
            } else {
                map.put(num, map.get(num) + 1);
            }
        }

        int other_uniq_total_comb = getTotUniqComb(tot_uniq - 1);
        long result = 0;
        for (int i = 0; i < nums.length; i++) {
            int frq = map.get(nums[i]);
            if (frq == 1) continue;
            long chose2 = choose(frq, 2);
            result += chose2 * other_uniq_total_comb;
            if (frq > 2) {
                long chose3 = choose(frq, 3);
                result += chose3 * (nums.length - frq);
            }
            i += frq - 1;
            System.out.println(result);
        }
        return Long.toString(result);
    }

    int getTotUniqComb(int n) {
        return (n * (n - 1)) - (((n - 1) * (n)) / 2);
    }

    public static long choose(long total, long choose){
        if(total < choose)
            return 0;
        if(choose == 0 || choose == total)
            return 1;
        return choose(total-1,choose-1)+choose(total-1,choose);
    }
}
