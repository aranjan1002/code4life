public class UglyNumberII {
    PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
    public int nthUglyNumber(int n) {
        pq.offer(1);
        int cnt = 0; 
        int result = 0;
        while (cnt < n) {
            cnt++;
            result = pq.poll();
            if (!pq.contains(result * 3) && result < Integer.MAX_VALUE / 3) {
                pq.offer(result * 3);
            }
            if (!pq.contains(result * 2) && result < Integer.MAX_VALUE / 2) {
                pq.offer(result * 2);
            }
            if (!pq.contains(result * 5) && result < Integer.MAX_VALUE / 5) {
                pq.offer(result * 5);
            }
        }
        return result;
    }
}