package Year2016.Round1C;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * Created by Anshu on 3/19/2017.
 */
public class Slides {
    public static void main(String[] args)
            throws Exception {
        new Slides().start("input.in");
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
            int B = sc.nextInt();
            long M = sc.nextLong();

            // call function and get output
            String result = solve(B, M);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int B, long M)
            throws Exception {
        int[][] mat = new int[B][B];
        mat[0][B - 1] = 1;
        M--;

        for (int i = 1; i < B - 1; i++) {
            mat[0][i] = 1;
            for (int j = 1; j < i; j++) {
                mat[j][i] = 1;
            }
        }

        int midNodes = B - 2;
        long[] maxFromNodes = new long[midNodes + 1];
        long max = getMax(midNodes, maxFromNodes);
        //System.out.println(getMax(midNodes));
        if (max < M) return "IMPOSSIBLE";

        int[] edges = getEdges(M, midNodes, maxFromNodes);
        for (int i = 1; i < edges.length; i++) {
            if (edges[i] == 1) {
                mat[i][B - 1] = 1;
            }
        }

        StringBuilder result = new StringBuilder();
        result.append("POSSIBLE\n");
        for (int i = 0; i < B; i++) {
            for (int j = 0; j < B; j++) {
                result.append(Integer.toString(mat[i][j]));
            }
            if (i != B - 1) result.append("\n");
        }

        return result.toString();
    }

    int[] getEdges(long diff, int N, long[] maxFromNodes) {
        int[] result = new int[N + 1];
        while (diff > 0) {
            if (maxFromNodes[N] <= diff) {
                result[N] = 1;
                diff -= maxFromNodes[N];
            }
            N--;
        }
        return result;
    }
//
//    long getMax(int N) {
//        int sum = 0;
//        for (int i = 1; i <= N; i++) sum += i;
//        return sum;
//    }

    long getMax(int N, long[] maxFromNodes) {
        if (N == 0) return 0;
        long result = 0;
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j < i; j++) {
                maxFromNodes[i] += maxFromNodes[j];
            }
            maxFromNodes[i] += 1;
            result += maxFromNodes[i];
        }
        return result;
    }

//    long getMax(int N, long[] maxFromNodes) {
//        if (N == 1) return 1;
//        if (N == 0)  return 0;
//        long[] facts = new long[N + 1];
//        fact(N, facts);
//        long result = 0;
//        for (int i = 1; i <= N; i++) {
//            long temp = (facts[N] / (facts[i] * facts[N - i]));
//            maxFromNodes[i] = temp;
//            result += temp;
//        }
//        return  result;
//    }

    void fact(int N, long[] facts) {
        facts[1] = facts[0] = 1;
        for (int i = 2; i <= N; i++) facts[i] = facts[i - 1] * i;
    }
}
