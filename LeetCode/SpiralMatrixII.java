public class SpiralMatrixII {
    public int[][] generateMatrix(int n) {
        int[][] result = new int[n][n];
        int dist = 0;
        
        int cnt = 1;
        while (cnt <= n * n) {
        for (int i = dist; i <= n - 1 - dist; i++) {
            result[dist][i] = cnt++;
        }
        for (int i = dist + 1; i <= n - 1 - dist; i++) {
            result[i][n - 1 - dist] = cnt++;
        }
        for (int i = n - 2 - dist; i >= dist; i--) {
            result[n - 1 - dist][i] = cnt++;
        }
        for (int i = n - 2 - dist; i >= dist + 1; i--) {
            result[i][dist] = cnt++;
        }
        dist++;
        }
        
        return result;
    }
}