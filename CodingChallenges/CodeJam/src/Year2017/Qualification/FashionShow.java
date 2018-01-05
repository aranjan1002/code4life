package Year2017.Qualification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Anshu on 12/23/2017.
 */
public class FashionShow {
    public static void main(String[] args)
            throws Exception {
        new FashionShow().start("input.in");
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
            int c = sc.nextInt();
            char[][] mat = new char[n +1][n + 1];
            for (int i = 0; i < c; i++) {
                char s = sc.next().charAt(0);
                mat[sc.nextInt()][sc.nextInt()] = s;
            }

            // call function and get output
            String result = solve(n, c, mat);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int n, int c, char[][] mat)
            throws Exception {


        return "";
    }

//    boolean isValid(char[][] mat, int x, int y, char c, ) {
//        switch (c) {
//            case 'o':
//                for (int )
//        }
//    }
}
