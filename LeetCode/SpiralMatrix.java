public class SpiralMatrix {
    public List<Integer> spiralOrder(int[][] matrix) {
        int starti = 0;
        int startj = -1;
        List<Integer> result = new ArrayList<Integer>();
        int m = matrix.length;
        if (m == 0) {
            return result;
        }
        int n = matrix[0].length;
        if (n == 0) {
            return result;
        }
        
        int cnt = 0;
        while (m > 0 && n > 0) {
            // go right
            for (int i = 1; i <= n; i++) {
                startj++;
                result.add(matrix[starti][startj]);
            }
            
            if (1 > m - 1) {
                break;
            }
            // go down
            for (int i = 1; i <= m - 1; i++) {
                starti++;
                result.add(matrix[starti][startj]);
            }
            
            if (1 > n - 1) {
                break;
            }
            // go left
            for (int i = 1; i <= n - 1; i++) {
                startj--;
                result.add(matrix[starti][startj]);
            }
            
            
            // go up
            for (int i = 1; i <= m - 2; i++) {
                starti--;
                result.add(matrix[starti][startj]);
            }
            
            m -= 2;
            n -= 2;
        }
        return result;
    }
}