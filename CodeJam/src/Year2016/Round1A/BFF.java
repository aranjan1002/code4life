package Year2016.Round1A;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Scanner;

public class BFF {
    public static void main(String[] args)
            throws Exception {
        new BFF().start("input.in");
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
            int[] F = new int[N + 1];
            for (int i = 1; i <= N; i++) F[i] = sc.nextInt();

            // call function and get output
            String result = solve(N, F);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }

    public String solve(int N, int[] F)
            throws Exception {
        HashSet<Integer> set = new HashSet<Integer>();
        int result = 0;
        boolean[] isTwoNodeLoop = new boolean[N + 1];
        int[] longestIncomingChain = new int[N + 1];
        int cntPairs = 0;

        for (int i = 1; i < F.length; i++)
            if (F[F[i]] == i) { isTwoNodeLoop[i] = isTwoNodeLoop[F[i]] = true; cntPairs++; }

        result = cntPairs;

        for (int i = 1; i < F.length; i++) {
            int start_node = i;
            int curr_node = i;
            int len = 0;
            while(true) {
                if (isTwoNodeLoop[curr_node]) {
                    if (start_node != curr_node) {
                        longestIncomingChain[curr_node] = Math.max(set.size(), longestIncomingChain[curr_node]);
                    }
                    break;
                } else if (!set.contains(curr_node)) {
                    set.add(curr_node);
                    curr_node = F[curr_node];
                } else {
                    break;
                }
            }
            if (curr_node == start_node) result = Math.max(result, set.size());
            set.clear();
        }
        int combinedChainLength = 0;
        for (int i = 1; i < N; i++)
            if(isTwoNodeLoop[i]) {
                combinedChainLength += longestIncomingChain[i] + longestIncomingChain[F[i]] + 2;
                isTwoNodeLoop[i] = isTwoNodeLoop[F[i]] = false;
            }
        result = Math.max(result, combinedChainLength);
        return Integer.toString(result);
    }
}
