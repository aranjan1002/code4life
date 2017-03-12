package Year2016.Round2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class RatherPerplexingShowdown {
    public static void main(String[] args)
            throws Exception {
        new RedTapeCommittee().start("input.in");
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
            int R = sc.nextInt();
            int P = sc.nextInt();
            int S = sc.nextInt();

            // call function and get output
            String result = solve(N, R, P, S);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result);
        }
        bw.close();
    }

    public String solve(int N, int R, int P, int S)
            throws Exception {
//        int players = 2 ^ N;
//        int[] tree = new int[(players * 2) - 1];
//        int len = tree.length;
//
//        for (int i = len / 2; i <= len - 1; i++) {
//            if ()
//        }

        return "";
    }

    boolean backTrack(int[] tree, int idx) {
        for (int i = 1; i < 3; i++) {
            tree[idx] = i;
            if (possible(tree, idx)) {
                place(tree, idx, i);
                if (backTrack(tree, idx + 1)) return true;
            }
        }
        return false;
    }

    boolean possible(int[] tree, int idx) {
        int curr_val = tree[idx];
        while (idx > 0) {
            int parentIdx = (idx - 1) / 2;
            if (tree[parentIdx] == 0) return true;
            else if (tree[parentIdx] != curr_val) {
                idx = parentIdx;
                curr_val = tree[idx];
            }
            else return false;
        }
        return false;
    }

    void place(int[] tree, int idx, int val) {
//        int curr_val = tree[idx];
//        while (idx > 0) {
//            int parentIdx = (idx - 1) / 2;
//            if (tree[parentIdx] == 0) return;
//            else (tree[parentIdx] != curr_val) {
//                int winner = getWinner(tree[parentIdx], curr_val);
//                idx = parentIdx;
//                curr_val = winner;
//                tree[idx] = curr_val;
//            }
//        }
    }
}
