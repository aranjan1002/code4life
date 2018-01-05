package Year2017.KickStartRoundG;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * Created by Anshu on 10/22/2017.
 */
public class HugeNumbers {
    public static void main(String[] args)
            throws Exception {
        new HugeNumbers().start("input.in");
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
            int A = sc.nextInt();
            int N = sc.nextInt();
            int P = sc.nextInt();

            // call function and get output
            String result = solve(A, N, P);
            System.out.println(A + " " + N + " " + P);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int A, int N, int P) {
        long result = A;
        if (N == 1) result = A % P;
        for (int i = N; i > 1; i--) {
            result = recurse(result, i, P) % P;
        }
        return Long.toString(result);
    }

    public long recurse(long A, long N, long P) {
        //if (A > P) return recurse(A % P, N, P);
        System.out.println(A + " " + N + " " + P);
        if (A % P == 0) return 0;
        if (N == 1) {return A % P;}
        long result = (recurse(A * A, N / 2, P));
        //System.out.println(result + ": r" + N);
        if (N % 2 == 0) return result;
        return ((result * A) % P);
    }
}
