public class N-Queens {
    public List<List<String>> solveNQueens(int n) {
        int[] rows = new int[n];
        List<List<String>> result = new ArrayList<List<String>>();
        solveNQueens(rows, 0, result);
        return result;
    }
    
    public void solveNQueens(int[] rows, int row, List<List<String>> result) {
        if (row == rows.length) {
            addToResult(rows, result);
            return;
        }
        
        for (int i = 0; i <= rows.length - 1; i++) {
            rows[row] = i;
            if (check(rows, row) == true) {
                solveNQueens(rows, row + 1, result);
            }
        }
    }
    
    boolean check(int[] rows, int row) {
        for (int i = 0; i <= row - 1; i++) {
            if (rows[i] == rows[row] ||
                row - i == rows[row] - rows[i] ||
                row - i == rows[i] - rows[row]) {
                    return false;
                }
        }
        return true;
    }
    
    void addToResult(int[] rows, List<List<String>> result) {
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < rows.length; i++) {
            String row_str = new String();
            for (int j = 0; j <= rows.length - 1; j++) {
                if (rows[i] == j) {
                    row_str += "Q";
                } else {
                    row_str += ".";
                }
            }
            res.add(row_str);
        }
        
        result.add(res);
    }
}