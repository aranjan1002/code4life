public class N-QueensII {
    int result = 0;
    public int totalNQueens(int n) {
        int[] rows = new int[n];
        totalNQueens(rows, 0);
        return result;
    }
    
    public void totalNQueens(int[] rows, int row) {
        if (row == rows.length) {
            result++;
            return;
        }   
        
        for (int i = 0; i <= rows.length - 1; i++) {
            rows[row] = i;
            if (check(rows, row) == true) {
                totalNQueens(rows, row + 1);
            }
        }
    }
    
    public boolean check(int[] rows, int row) {
        for (int i = 0; i <= row - 1; i++) {
            // check column
            if (rows[i] == rows[row]) {
                return false;
            }
            // check diagonal
            if (Math.abs(rows[i] - rows[row]) == Math.abs(i - row)) {
                return false;
            }
        }
        return true;
    }
}