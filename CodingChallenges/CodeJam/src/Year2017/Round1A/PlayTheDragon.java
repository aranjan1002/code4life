package Year2017.Round1A;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Anshu on 12/31/2017.
 */
public class PlayTheDragon {
    public static void main(String[] args)
            throws Exception {
        new PlayTheDragon().start("input.in");
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
            long[] vals = new long[6];
            for (int i = 0; i < 6; i++) vals[i] = sc.nextLong();

            // call function and get output
            String result = solve(vals);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(long[] vals)
            throws Exception {
        return bfs(vals);
    }

    public String bfs(long[] vals) {
        Queue<Node> q = new LinkedList<>();
        q.add(new Node(vals[0], vals[2], vals[1], vals[3]));
        Node dummyNode = new Node(-1, -1, -1, -1);
//        System.out.println(q.peek());
        q.add(dummyNode);
        HashSet<Node> mem = new HashSet<>();
        int steps = 1;
        while (true) {
//            System.out.println(q);
            Node n = q.poll();

            if (n.equals(dummyNode)) {steps++; if (q.isEmpty()) break; q.add(dummyNode); continue;}

            // Attack
            if (n.Hk - n.Ad <= 0) return Integer.toString(steps);
            if (n.Hd - n.Ak > 0) {
                Node n2 = new Node(n.Hd - n.Ak, n.Hk - n.Ad, n.Ad, n.Ak);
                add(n2, q, mem);
            }

            // Buff
            if (n.Hd - n.Ak > 0) {
                Node n2 = new Node(n.Hd - n.Ak, n.Hk, n.Ad + vals[4], n.Ak);
                add(n2, q, mem);
            }

            // Cure
            if (vals[0] - n.Ak > 0) {
                Node n2 = new Node(vals[0] - n.Ak, n.Hk, n.Ad, n.Ak);
                add(n2, q, mem);
            }

            // Debuff
            if (n.Hd - Math.max(0, n.Ak - vals[5]) > 0) {
                Node n2 = new Node(n.Hd - Math.max(0, n.Ak - vals[5]), n.Hk, n.Ad, Math.max(0, n.Ak - vals[5]));
                add(n2, q, mem);
            }
        }


        return "IMPOSSIBLE";
    }

    void add(Node n, Queue<Node> q, Set<Node> mem) {
        if (!mem.contains(n)) {
            q.add(n);
            mem.add(n);
        }
    }

    class Node {
        long Hd = 0;
        long Hk = 0;
        long Ad = 0;
        long Ak = 0;

        Node(long Hd, long Hk, long Ad, long Ak) {this.Hd = Hd; this.Hk = Hk; this.Ad = Ad; this.Ak = Ak;}

        @Override
        public boolean equals(Object o) {
            Node n = (Node) o;
            return n.Hd == Hd && n.Hk == Hk && n.Ak == Ak && n.Ad == Ad;
        }

        @Override
        public String toString() {
            return Hd + " " + Hk + " " + Ad + " " + Ak;
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }
    }
}
