public class UniquePathsII {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int row = obstacleGrid.length;
        if (row == 0) {
            return 0;
        }
        int col = obstacleGrid[0].length;
        if (col == 0) {
            return 0;
        }
        
        obstacleGrid = flip(obstacleGrid, row, col);
        
        if (obstacleGrid[0][0] == -1) {
            return 0;
        }
        // obstacleGrid[0][0] = 1;
        
        for (int i = 0; i <= row - 1; i++) {
            if (obstacleGrid[i][0] != -1) {
                obstacleGrid[i][0] = 1;
            }
            else {
                break;
            }
        }
        
        for (int i = 1; i <= col - 1; i++) {
            if (obstacleGrid[0][i] != -1) {
                obstacleGrid[0][i] = 1;
            } else {
                break;
            }
        }
        
        for (int i = 1; i <= row - 1; i++) {
            for (int j = 1; j <= col - 1; j++) {
                if (obstacleGrid[i][j] == -1) {
                    continue;
                }
                obstacleGrid[i][j] = Math.max(obstacleGrid[i - 1][j], 0) + Math.max(obstacleGrid[i][j - 1], 0);
            }
        }
        
        return obstacleGrid[row - 1][col - 1] == -1 ? 0 : obstacleGrid[row - 1][col - 1];
    }
    
    int[][] flip(int[][] obstacleGrid, int row, int col) {
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j <= col - 1; j++) {
                if (obstacleGrid[i][j] == 1) {
                    obstacleGrid[i][j] = -1;
                } 
            }
        }
        
        return obstacleGrid;
    }
}