package Year2017.KickStartRoundF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Anshu on 9/24/2017.
 */
public class EatCake {
    public static void main(String[] args)
            throws Exception {
        new EatCake().start("input.in");
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

            // call function and get output
            String result = solve(n);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int n)
            throws Exception {
        return Integer.toString(minSquares(n));
    }

    public int minSquares(int n) {
        if (map.containsKey(n)) return map.get(n);
        int result = Integer.MAX_VALUE;
        if (n <= 3) {
            result = n;
        } else {
            for (int i = (int) Math.sqrt(n); i >= 1; i--) {
                result = Math.min(result, minSquares(n - i * i) + 1);
            }
        }
        map.put(n, result);
        //System.out.println(n + " " + result);
        return result;
    }

    HashMap<Integer, Integer> map = new HashMap<>();
}
