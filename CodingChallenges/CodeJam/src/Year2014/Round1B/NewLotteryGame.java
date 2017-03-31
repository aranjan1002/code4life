package Year2014.Round1B;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Anshu on 3/25/2017.
 */
public class NewLotteryGame {
    public static void main(String[] args)
            throws Exception {
        new NewLotteryGame().start("input.in");
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
            int[] nums = new int[3];
            for (int i = 0; i < 3; i++) nums[i] = sc.nextInt();

            // call function and get output
            String result = solve(nums);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int[] nums)
            throws Exception {
        long result = solve(new char[31], new char[31], 30,
                nums[0], nums[1], nums[2],
                0, 0);
        return Long.toString(result);
    }

    long solve(char[] a, char[] b, int idx,
               long A, long B, long K,
               long num1, long num2) {
            if (num1 >= A || num2 >= B) return 0;
            if (num1 >= K && num2 >= K && a[idx + 1] == '1' && b[idx + 1] == '1') return 0;
            if (A <= K || B <= K) return A * B;
            if (idx < 0) return 1;
            long maxPossible = 1 << idx;
            if (!(num1 == 0 && num2 == 0))
                if (maxPossible + num1 < K && maxPossible + num2 < K) return (1 << (2 * (idx + 1)));

            // k is smallest
            long e = 1 << idx;
            long result = 0;

            for (int i = 0; i < 4; i++) {
                a[idx] = c1[i];
        b[idx] = c2[i];
        long num1_param = a[idx] == '0' ? num1 : num1 + e;
        long num2_param = b[idx] == '0' ? num2 : num2 + e;
        result += solve(a, b, idx - 1, A, B, K, num1_param, num2_param);
    }
    //System.out.println(num1 + " " + num2 + " " + idx + " " + result);
        return result;
}

    char[] c1 = {'0', '0', '1', '1'};
    char[] c2 = {'0', '1', '0', '1'};
}
