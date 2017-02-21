public class RotateImage {
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        int[][] transpose_matrix = new int[n][n];
        
        for (int i = 0; i <= n - 1; i++) {
            for (int j = 0; j <= n - 1; j++) {
                transpose_matrix[i][j] = matrix[j][i];
            }
        }
        
        for (int i = 0; i <= n - 1; i++) {
            for (int j = 0; j <= n - 1; j++) {
                matrix[i][j] = transpose_matrix[i][n - 1 - j];
                // matrix[i][j] = transpose_matrix[i][j];
            }
        }
    }
}