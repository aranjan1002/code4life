public class CountPrimes {
    public int countPrimes(int n) {
        boolean[] isNotPrime = new boolean[n + 1];
        int count = 0;
        
        for (int i = 2; i < n; i++) {
            if (isNotPrime[i] == true) {
                continue;
            } else {
                count++;
            }
            for (int j = i + i; j <= n; j = j + i) {
                isNotPrime[j] = true;
            }
        }
        return count;
    }
}