package Year2017.Qualification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Anshu on 12/20/2017.
 */
public class BathroomStalls {
    public static void main(String[] args)
            throws Exception {
        new BathroomStalls().start("input.in");
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
            long n = sc.nextLong();
            long k = sc.nextLong();

            // call function and get output
            String result = solve(n, k);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }

    public String solve(long n, long k)
            throws Exception {
        Map<String, Node> map = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                long min1 = Math.min(o1.l, o1.r);
                long min2 = Math.min(o2.l, o2.r);
                if (min1 != min2) return Long.compare(min2, min1);
                long max1 = Math.max(o1.l, o1.r);
                long max2 = Math.max(o2.l, o2.r);
                if (max1 != max2) return Long.compare(max2, max1);
                return 0;
            }
        });

        long l = (n - 1) / 2;
        long  r = n / 2;

        Node root = new Node(l, r);
        pq.add(root);
        map.put(key(root.l, root.r), root);

        Node curr = root;
        while (k > 0) {
            curr = pq.poll();
            //System.out.println("Curr: " + key(curr.l, curr.r) + " " + curr.c);
            long l1 = (curr.l - 1) / 2;
            long r1 = curr.l / 2;
            long cnt = curr.c;
            long l2 = (curr.r - 1) / 2;
            long r2 = curr.r / 2;

            addToQ(map, pq, l1, r1, cnt);
            addToQ(map, pq, l2, r2, cnt);
            k -= curr.c;
            //System.out.println("K = " + k);
        }
        return Long.toString(curr.r) + " " + Long.toString(curr.l);
    }

    void addToQ(Map<String, Node> map, PriorityQueue<Node> pq, long l, long r, long cnt) {
        String key1 = key(l, r);
        //System.out.println(key1);
        if (map.containsKey(key1)) {
            map.get(key1).c += cnt;
        } else
        {
            Node node = new Node(l, r);
            node.c = cnt;
            map.put(key1, node);
            pq.add(node);
        }
    }

    String key(long l, long r) {
        return str(l) + " " + str(r);
    }

    class Node {
        Node(long l, long r) { this.l = l; this.r = r; c = 1;}
        long l, r, c;
    }

    String str(Object o) {
        return o.toString();
    }
}
