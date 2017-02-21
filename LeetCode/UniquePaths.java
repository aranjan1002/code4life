public class UniquePaths {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        
        if (m == 0 || n == 0) {
            return 0;
        }
        
        for (int i = 0; i <= m - 1; i++) {
            dp[i][0] = 1;
        }
        
        for (int j = 0; j <= n - 1; j++) {
            dp[0][j] = 1;
        }
        
        for (int i = 1; i <= m - 1; i++) {
            for (int j = 1; j <= n - 1; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        
        return dp[m - 1][n - 1];
    }
}