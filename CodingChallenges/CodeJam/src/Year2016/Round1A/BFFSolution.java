package Year2016.Round1A;

import java.io.*;
import java.util.*;
public class BFFSolution {
    public static void main(String[] args) throws IOException {
        input.init(new FileInputStream("input.in"));
        PrintWriter out = new PrintWriter(System.out);
        int T = input.nextInt();
        for(int t = 0; t<T; t++)
        {
            n = input.nextInt();
            a = new int[n];
            for(int i = 0; i<n; i++) a[i] = input.nextInt()-1;
            int longestCycle = 0;
            for(int i = 0; i<n; i++)
            {
                int steps = 0;
                int at = i;
                for(int j = 0; j<n; j++)
                {
                    at = a[at];
                    if(at == i)
                    {
                        steps = j+1;
                        break;
                    }
                }
                if(steps != 0) longestCycle = Math.max(longestCycle, steps);
            }
            int res = longestCycle;
            ArrayList<Integer>[] revGraph = new ArrayList[n];
            for(int i = 0; i<n; i++) revGraph[i] = new ArrayList<Integer>();
            for(int i = 0; i<n; i++) revGraph[a[i]].add(i);
            boolean[][] g = new boolean[n][n];
            for(int i = 0; i<n; i++) g[i][a[i]] = true;
            int other = 0;
            for(int i = 0; i<n; i++)
                for(int j = i+1; j<n; j++)
                    if(g[i][j] && g[j][i])
                    {
                        other += bfs(i, j, revGraph) + bfs(j, i, revGraph) + 2;
                    }
            if(other > res) res = other;
            out.println("Case #" + (t+1)+": "+res);
        }
        out.close();
    }
    static int bfs(int start, int bad, ArrayList<Integer>[] g)
    {
        int n = g.length;
        Queue<Integer> q = new LinkedList<Integer>();
        q.add(start);
        int[] d = new int[n];
        Arrays.fill(d, -1);
        d[start] = 0;
        int max = 0;
        while(!q.isEmpty())
        {
            int at = q.poll();
            max = Math.max(max, d[at]);
            for(int e : g[at])
            {
                if(e == bad) continue;
                if(d[e] == -1)
                {
                    q.add(e);
                    d[e] = 1 + d[at];
                }
            }
        }
        return max;
    }
    static void go(int[] p, boolean[] used, int at)
    {
        if(at == n)
        {
            for(int i = 1; i<=n; i++)
            {
                ArrayList<Integer> list = new ArrayList<Integer>();
                for(int j = 0; j<i; j++) list.add(p[j]);
                boolean good = true;
                for(int j = 0; j<i; j++)
                {
                    int cur = list.get(j);
                    int need = a[cur];
                    if((list.get((j+1)%i)) != need && list.get((j+i-1)%i) != need)
                    {
                        good = false;
                        break;
                    }
                }

                if(good) max = Math.max(max, list.size());
            }

            return;
        }
        for(int i = 0; i<n; i++)
        {
            if(used[i]) continue;
            p[at] = i;
            used[i] = true;
            go(p, used, at+1);
            used[i] = false;
        }
    }
    static int max, n;
    static int[] a;
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

