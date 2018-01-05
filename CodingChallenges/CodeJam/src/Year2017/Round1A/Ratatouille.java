package Year2017.Round1A;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Anshu on 12/27/2017.
 */
public class Ratatouille {
    public static void main(String[] args)
            throws Exception {
        new Ratatouille().start("input.in");
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
            int p = sc.nextInt();
            int[] r = new int[n];
            int[][] q = new int[n][p];
            for (int i = 0; i < n; i++) {
                r[i] = sc.nextInt();
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < p; j++) {
                    q[i][j] = sc.nextInt();
                 }
            }

            // call function and get output
            String result = solve(n, p, r, q);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int n, int p, int[] r, int[][] q)
            throws Exception {

        int[][][] intls = new int[n][p][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                intls[i][j][0] = (int) Math.ceil((q[i][j] * 10.0) / (r[i] * 11));
                intls[i][j][1] = (q[i][j] * 10) / (r[i] * 9);
            }
            Arrays.sort(intls[i], (o1, o2) -> o1[0] != o2[0] ? Integer.compare(o1[0], o2[0]) : Integer.compare(o1[1], o2[1]));
//            System.out.println();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                System.out.print(intls[i][j][0] + "," + intls[i][j][1] + " ");
            }
            System.out.println();
        }

        int[] ptr = new int[n];
        int result = 0;
        int[] currMinRange = intls[0][0];
        int currPtr = 0;
        int currMinPtr = 0;
        while (true) {
            //System.out.println(currPtr + " " + ptr[currPtr] + " " + result);
            if (ptr[currPtr] == p) break;
            int[] currIntl = intls[currPtr][ptr[currPtr]];
            if (currPtr == 0) currMinRange = intls[0][ptr[0]];
            if (currIntl[1] < currIntl[0]) {ptr[currPtr]++; }
            else if (overlaps(currMinRange, currIntl)) {
                currMinRange[0] = Math.min(currMinRange[0], currIntl[0]);
                currMinRange[1] = Math.min(currMinRange[1], currIntl[1]);
                if (currMinRange[0] > currIntl[0]) currMinPtr = currPtr;
                else if (currMinRange[0] == currIntl[0] && currMinRange[1] > currIntl[1]) currMinPtr = currPtr;
                //ptr[currPtr]++;
                if (currPtr == n - 1) { result++; for (int i = 0; i < n; i++) ptr[i]++;  currPtr = 0; currMinPtr = 0; }
                else currPtr++;
            } else {
                ptr[currMinPtr]++;
                currMinPtr = 0;
                currPtr = 0;
            }
       }

        return Integer.toString(result);
    }

    private boolean overlaps(int[] currMinRange, int[] ints) {
        if (ints[1] < currMinRange[0] || currMinRange[1] < ints[0]) return false;
        return true;
    }
}
