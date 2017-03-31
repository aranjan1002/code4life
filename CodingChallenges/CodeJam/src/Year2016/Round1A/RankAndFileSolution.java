package Year2016.Round1A;

import java.io.*;
import java.util.*;
public class RankAndFileSolution {
    public static void main(String[] args) throws IOException {
        input.init(new FileInputStream("input.in"));
        PrintWriter out = new PrintWriter(System.out);
        int T = input.nextInt();
        for(int t = 0; t<T; t++)
        {
            int n = input.nextInt();
            int[] f = new int[2501];
            for(int i= 0; i<2*n-1; i++)
                for(int j = 0; j<n; j++)
                    f[input.nextInt()]++;
            out.print("Case #" + (t+1)+":");
            for(int i = 1; i<=2500; i++)
                if(f[i]%2 == 1)
                    out.print(" "+i);
            out.println();
        }
        out.close();
    }
    static class arr implements Comparable<arr>
    {
        int[] a;
        public arr(int[] aa)
        {
            a = aa;
        }
        @Override
        public int compareTo(arr o) {
            // TODO Auto-generated method stub
            for(int i = 0; i<a.length; i++)
                if(a[i] != o.a[i])
                    return a[i] - o.a[i];
            return 0;
        }
    }
    public static class input {
        static BufferedReader reader;
        static StringTokenizer tokenizer;

        static void init(InputStream input) {
            reader = new BufferedReader(new InputStreamReader(input));
            tokenizer = new StringTokenizer("");
        }

        static String next() throws IOException {
            while (!tokenizer.hasMoreTokens())
                tokenizer = new StringTokenizer(reader.readLine());
            return tokenizer.nextToken();
        }

        static int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        static double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }

        static long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    }
}

