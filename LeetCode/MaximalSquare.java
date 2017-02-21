public class MaximalSquare {
    public int maximalSquare(char[][] matrix) {
        int max = 0;
        int row = matrix.length;
        if (row == 0) {
            return 0;
        }
        int col = matrix[0].length;
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j <= col - 1; j++) {
                if (matrix[i][j] == '1') {
                    int m = getMax(matrix, i, j, row, col);
                    if (m > max) {
                        max = m;
                    }
                }
            }
        }
        return max * max;
    }
    
    public int getMax(char[][] matrix, int i, int j, int row, int col) {
        int max = 1;
        int maxSize = Math.min(row - i, col - j);
        for (int size = 2; size <= maxSize; size++) {
            int r = i + size - 1;
            int c = j + size - 1;
            
            for (int k = j; k <= j + size - 1; k++) {
                if (matrix[r][k] == '0') {
                    return size - 1;
                }
            }
            for (int k = i; k <= i + size - 1; k++) {
                if (matrix[k][c] == '0') {
                    return size - 1;
                }
            }
        }
        return maxSize;
    }
}