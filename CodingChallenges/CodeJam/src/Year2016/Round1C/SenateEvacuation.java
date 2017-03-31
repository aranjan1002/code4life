package Year2016.Round1C;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Created by Anshu on 3/19/2017.
 */
public class SenateEvacuation {
    public static void main(String[] args)
            throws Exception {
        new SenateEvacuation().start("input.in");
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
            int[] P = new int[N];
            for (int i = 0; i < N; i++) P[i] = sc.nextInt();

            // call function and get output
            String result = solve(P, N);
            System.out.println("Case #" + (tot_test - loop) + ": " + result);
            bw.write("Case #" + (tot_test - loop) + ": " + result + "\n");
        }
        bw.close();
    }


    public String solve(int[] P, int N)
            throws Exception {
        PriorityQueue<SenatorCount> pq = new PriorityQueue<>();
        int total = 0;
        for (int i = 0; i < N; i++) { pq.add(new SenatorCount(i, P[i])); total += P[i]; }
        String result = "";
        while (!pq.isEmpty()) {
            SenatorCount sc1 = pq.poll();
            int party1 = sc1.party;
            sc1.count--;
            total--;
            result += (char) ('A' + party1);
            if (sc1.count > 0) {
                pq.add(sc1);
            }
            if (!pq.isEmpty()) {
                SenatorCount sc2 = pq.poll();
                if (!pq.isEmpty()) {
                    int nextPartyCount = pq.peek().count;
                    if (nextPartyCount * 2 > (total - 1)) {
                        pq.add(sc2);
                        result += " ";
                        continue;
                    }
                }
                result += (char) ('A' + sc2.party);
                total--;
                sc2.count--;
                if (sc2.count > 0) pq.add(sc2);
                result += " ";
            }
        }

        return result;
    }

    class SenatorCount implements Comparable<SenatorCount> {
        int party;
        int count;
        SenatorCount(int p, int c) { party = p; count = c;}

        @Override
        public int compareTo(SenatorCount o) {
            return (-1) * Integer.compare(count, o.count);
        }
    }
}
