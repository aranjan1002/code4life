public class Searcha2DMatrixII {
    public boolean searchMatrix(int[][] matrix, int target) {
        int row = matrix.length;
        if (row == 0) {
            return false;
        }
        int col = matrix[0].length;
        if (col == 0) {
            return false;
        }
        int curr_row = 0;
        int curr_col = col - 1;
        while (curr_row < row && curr_col >= 0) {
            if (matrix[curr_row][curr_col] == target) {
                return true;
            }
            if (matrix[curr_row][curr_col] < target) {
                curr_row++;
            } else {
                curr_col--;
            }
        }
        return false;
    }
}