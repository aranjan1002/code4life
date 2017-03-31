package Year2014.Round1A;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Anshu on 3/23/2017.
 */
public class ProperShuffle {
    public static void main(String[] args)
            throws Exception {
        new ProperShuffle().start("input.in");
    }

    public void start(String fileName)
            throws Exception {
        Scanner sc = new Scanner(new File(fileName));
        OutputStreamWriter os =
                new OutputStreamWriter(
                        new FileOutputStream("Output"));
        BufferedWriter bw = new BufferedWriter(os);

        int tot_test = Integer.parseInt(sc.next());
        int loop = 1;//tot_test;
        while (loop-- > 0) {
            // Code to read input

            // call function and get output
            String result = solve();
//            System.out.println("Case #" + (tot_test - loop) + ": " + result);
//            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve()
            throws Exception {
        int cnt = 100;
        while (cnt-- > 0) {
            Random r = new Random();
            int n = 52;
            int Low = 0;
            int High = n;
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) nums[i] = i;
            for (int i = n - 1; i > 0; i--) {
                int rnum = r.nextInt(High - Low);
                int tmp = nums[i];
                nums[i] = nums[rnum];
                System.out.print(rnum + " ");
                nums[rnum] = tmp;
            }
            //for (int i = 0; i < n; i++)
                //System.out.print(nums[i] + " ");
            System.out.println();
        }
        return "";
    }
}
