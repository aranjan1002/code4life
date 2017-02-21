public class SetMatrixZeroes {
    public void setZeroes(int[][] matrix) {
        int row = matrix.length;
        if (row == 0) {
            return;
        }
        int col = matrix[0].length;
        setZeroes2(matrix, 0, 0, row, col);
    }
    
    public void setZeroes2(int[][] matrix, int curr_row, int curr_col, int row, int col) {
        for (int i = curr_row; i <= row - 1; i++) {
            for (int j = 0; j <= col - 1; j++) {
                if (i == curr_row && j < curr_col) continue;
                if (matrix[i][j] == 0) {
                    setZeroes2(matrix, i, j + 1, row, col);
                    setZeroes3(matrix, i, j, row, col);
                    j = col;
                    i = row;
                }
            }
        }
    }
    
    public void setZeroes3(int[][] matrix, int i, int j, int row, int col) {
        for (int r = 0; r <= row - 1; r++) {
            matrix[r][j] = 0;
        }
        for (int c = 0; c <= col - 1; c++) {
            matrix[i][c] = 0;
        }
    }
}