public class PerfectSquares {
    public int numSquares(int n) {
        int[] result = new int[n + 1];
        
        result[1] = 1;
        for (int i = 1; i * i <= n; i++) {
            result[i * i] = 1;
        }
        for (int i = 2; i <= n; i++) {
            if (result[i] == 0) {
                int min = Integer.MAX_VALUE;
                for (int j = i - 1; j >= i / 2; j--) {
                    if (result[j] + result[i - j] < min) {
                        min = result[j] + result[i - j];
                    }
                }
                result[i] = min;
            }
        }
        
        return result[n];
    }
}