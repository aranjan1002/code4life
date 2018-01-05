package Year2017.Round1A;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Anshu on 12/24/2017.
 */
public class AlphabetCake {
    public static void main(String[] args)
            throws Exception {
        new AlphabetCake().start("input.in");
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
            int r = sc.nextInt();
            int c = sc.nextInt();
            char[][] mat = new char[r][c];
            for (int i = 0; i < r; i++) {
                mat[i] = sc.next().toCharArray();
            }

            // call function and get output
            String result = solve(mat, r, c);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(char[][] mat, int r, int c)
            throws Exception {
        StringBuilder res = new StringBuilder();
        res.append("\n");
        Set<Character> visited = new HashSet<Character>();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (visited.contains(mat[i][j]) || mat[i][j] == '?') continue;
                visited.add(mat[i][j]);
                int left = j;
                int right = j;
                while (left - 1 >= 0 && mat[i][left - 1] == '?') mat[i][--left] = mat[i][j];
                while (right + 1 < c && mat[i][right + 1] == '?') mat[i][++right] = mat[i][j];
                for (int row = i + 1; row < r; row++) if (!fillIfEmpty(mat[row], left, right + 1, mat[i][j])) break;
                for (int row = i - 1; row >= 0; row--) if (!fillIfEmpty(mat[row], left, right + 1, mat[i][j])) break;
            }
        }

        for (int i = 0; i < r; i++) {
            res.append(mat[i]);
            res.append("\n");
        }

        return res.toString();
    }

    private boolean fillIfEmpty(char[] mat, int i, int k, char c) {
//        System.out.println(i);
//        System.out.println(i + " " + k);
        for (int t = i; t < k; t++) if (mat[t] != '?') return false;
        for (int t = i; t < k; t++) mat[t] = c;
        return true;
    }
}
