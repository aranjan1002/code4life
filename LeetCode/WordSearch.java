public class WordSearch {
    public boolean exist(char[][] board, String word) {
        int row = board.length;
        if (row == 0) {
            return false;
        }
        int col = board[0].length;
        if (col == 0) {
            return false;
        }
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j <= col - 1; j++) {
                boolean exst = exist(board, word, i, j, 0);
                if (exst == true) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    boolean exist(char[][] board, String word, int i, int j, int idx) {
        if (idx == word.length()) {
            return true;
        }
        
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return false;
        }
        
        if (board[i][j] != word.charAt(idx)) {
            return false;
        }
        
        board[i][j] ^= 256;
        
        boolean exst = exist(board, word, i - 1, j, idx + 1) || 
                       exist(board, word, i + 1, j, idx + 1) ||
                       exist(board, word, i, j - 1, idx + 1) ||
                       exist(board, word, i, j + 1, idx + 1);
                       
        board[i][j] ^= 256;
        return exst;
    }
}