public class GameofLife {
    public void gameOfLife(int[][] board) {
        if (board.length == 0 || board[0].length == 0) {
            return;
        }
        
        int row = board.length;
        int col = board[0].length;
        
        int[][] result = new int[row][col];
        
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j <= col - 1; j++) {
                result[i][j] = getStatus(board, i, j);
            }
        }
        
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j <= col - 1; j++) {
                board[i][j] = result[i][j];
            }
        }
    }
    
    int getStatus(int[][] board, int r, int c) {
        int numLives = 0;
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (isValid(board, i, j) == true && (i != r || j != c)) {
                    if (board[i][j] == 1) {
                        numLives++;
                    } 
                }
            }
        }
        
        if (numLives < 2 || numLives > 3) {
            return 0;
        } else if (numLives == 3) {
            return 1;
        } 
        return board[r][c];
    }
    
    boolean isValid(int[][] num, int r, int c) {
        int row = num.length;
        int col = num[0].length;
        
        if (r < 0 || r >= row || c < 0 || c >= col) {
            return false;
        }
        
        return true;
    }
}