package Year2016.Round1B;

import Utility.FordFulkerson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Anshu on 3/14/2017.
 */
public class Technobabble {
    public static void main(String[] args)
            throws Exception {
        new Technobabble().start("input.in");
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
            String[][] pair = new String[N][2];
            for (int i = 0; i < N; i++) {
                pair[i][0] = sc.next();
                pair[i][1] = sc.next();
            }

            // call function and get output
            String result = solve(pair, N);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(String[][] pair, int N)
            throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        HashMap<Integer, String> revMap = new HashMap<>();
        int idx = 1;
        for (int i = 0; i < N; i++) {
            String left = pair[i][0];
            String right = pair[i][1];
            if (!map.containsKey(left + "L")) map.put(left + "L", idx++);
            if (!map.containsKey(right + "R")) map.put(right + "R", idx++);
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            revMap.put(entry.getValue(), entry.getKey());
        }

        idx++;
        int[][] adjMat = new int[idx][idx];
        for (int i = 0; i < N; i++) {
            int leftIdx = map.get(pair[i][0] + "L");
            int rightIdx = map.get(pair[i][1] + "R");
            adjMat[0][leftIdx] = 1;
            adjMat[rightIdx][idx - 1] = 1;
            adjMat[leftIdx][rightIdx] = 1;
        }

//        for (int i = 0; i < adjMat.length; i++) {
//            for (int j = 0; j < adjMat[0].length; j++) {
//                System.out.print(adjMat[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();

        FordFulkerson ff = new FordFulkerson();
        ff.getMaxFlow(adjMat, 0, idx - 1);
        int[][] newAdjMat;
        newAdjMat = ff.getAdjMat();
        boolean[] isSel = new boolean[idx];
        int res = 0;

//        for (int i = 0; i < newAdjMat.length; i++) {
//            for (int j = 0; j < newAdjMat[0].length; j++) {
//                System.out.print(newAdjMat[i][j] + " ");
//            }
//            System.out.println();
//        }

        for (int i = 1; i < idx - 1; i++) {
            for (int j = 1; j < idx - 1; j++) {
                if (newAdjMat[i][j] == 0 && adjMat[i][j] == 1) {
                    res++;
                    isSel[i] = true;
                    isSel[j] = true;
                }
            }
        }

        for (int i = 1; i < idx - 1; i++) {
            if (!isSel[i]) res++;
            //else System.out.println(revMap.get(i));
        }


        return Integer.toString(N - res);
    }

    public int indexOf(String[] arr, String s) {
        for (int i = 0; i < arr.length; i++)
            if (s.equals(arr[i])) return i;
        return -1;
    }
}
