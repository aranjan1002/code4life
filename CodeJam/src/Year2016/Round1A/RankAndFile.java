package Year2016.Round1A;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class RankAndFile {
    public String res = new String();

    public static void main(String[] args)
            throws Exception {
        new RankAndFile().start("input.in");
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
            int[][] nums = new int[N * 2 - 1][N];
            for (int i = 0; i < N * 2 - 1; i++) {
                for (int j = 0; j < N; j++) {
                    nums[i][j] = sc.nextInt();
                }
            }
            // call function and get output
            String result = solve(N, nums);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
            res = "";
        }
        bw.close();
    }


    public String solve(int N, int[][] nums)
            throws Exception {
        Arrays.sort(nums, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]);
            }
        });
        //for (int i = 0; i < nums.length; i++) { for (int j = 0; j < N; j++) System.out.print(nums[i][j] + " "); System.out.println();}
        //System.out.println();
        recurse(nums, new int[N], 0, 0, N);
        return res;
    }

    public void recurse(int[][] nums, int[] selRows, int cntSel, int idx, int N) {
        //System.out.println(idx + " " + cntSel + " " + N);

        if (idx == nums.length) {
            if (isCorrect(nums, selRows, N)) {
                //for (int i : selRows) System.out.print(i + " ");
                int col = findMissingCol(nums, selRows, N);
                //System.out.println("Col "  + col + " " +  cntSel);
                if (col != -1) {
                    String result = "";
                    for (int i = 0; i < N; i++) {
                        result += nums[selRows[i]][col] + " ";
                    }
                    this.res = result;
                    return;
                }
            }
        }
        if (cntSel < N) {
            selRows[cntSel] = idx;
            recurse(nums, selRows, cntSel + 1, idx + 1, N);
        }
        if (nums.length - idx > N - cntSel) {
            if (cntSel < N) selRows[cntSel] = -1;
            recurse(nums, selRows, cntSel, idx + 1, N);
        }
    }

    public boolean isCorrect(int[][] nums, int[] selRows, int N) {
        //for (int i : selRows) System.out.print(i + " ");
        //System.out.println();
        for (int i = 0; i < N; i++) {
            for (int j = 1; j < N; j++) {
                if (nums[selRows[j]][i] <= nums[selRows[j - 1]][i]) return false;
            }
        }
        return true;
    }

    public int findMissingCol(int[][] nums, int[] selRows, int N) {
        int numNotFound = 0;
        int res = -1;
        for (int i = 0; i < N; i++) {
            int num = nums[selRows[0]][i];

            boolean found = false;
            for (int j = 0; j < nums.length; j++) {
                if (!contains(selRows, j) && check(nums[j], nums, selRows, i) == true) {found = true; break;}
            }
            if (!found) { numNotFound++; res = i; }
        }
        if (numNotFound == 1) return res;
        return -1;
    }

    public boolean contains(int[] nums, int i) {
        for (int n : nums) if (i == n) return true;
        return false;
    }

    public boolean check(int[] num, int[][] nums, int[] selRows, int idx) {
        //System.out.println(selRows.length);
        for (int i = 0; i < selRows.length; i++) {
            //System.out.println(idx + " " + selRows[i]);
            if (nums[selRows[i]][idx] != num[i]) return false;
        }
        return true;
    }
}
