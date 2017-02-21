public class SudokuSolver {
    public void solveSudoku(char[][] board) {
        solveSudoku(board, 0, 0);
    }
    
    boolean solveSudoku(char[][] board, int row, int col) {
        //System.out.println(row + " " + col);
        if (row == 9) {
            return true;
        }
        if (board[row][col] != '.') {
            return solveSudoku(board, getNextRow(row, col), getNextCol(row, col));
        }
        for (int i = 1; i <= 9; i++) {
            if (check(board, row, col, (char) ('0' + i)) == true) {
                board[row][col] = (char) ('0' + i);
                if (solveSudoku(board, getNextRow(row, col), getNextCol(row, col)) == true) {
                    return true;
                }
                //System.out.println("Got false.. trying with " + (i + 1));
            }
        }
        board[row][col] = '.';
        //printBoard(board);
        //System.out.println("return false");
        return false;
    }
    
    void printBoard(char[][] board) {
        for (int i = 0; i <= 8; i++) {
            for (int j = 0; j <= 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    boolean check(char[][] board, int row, int col, char c) {
        //printBoard(board);
        //System.out.println("Checking " + row + " " + col + " " + c);
        int first_row = (row / 3) * 3;
        int first_col = (col / 3) * 3;
        for (int i = first_row; i <= first_row + 2; i++) {
            for (int j = first_col; j <= first_col + 2; j++) {
                if (i != row || j != col) {
                    if (board[i][j] == c) {
                        return false;
                    }
                }
            }
        }
        for (int i = 0; i <= 8; i++) {
            if (i != row && board[i][col] == c) return false;
            if (i != col && board[row][i] == c) return false;
        }
        //System.out.println("Check " + true + " " + c);
        return true;
    }
    
    int getNextRow(int row, int col) {
        if (col == 8) {
            return row + 1;
        }
        return row;
    }
    
    int getNextCol(int row, int col) {
        if (col == 8) {
            return 0;
        }
        return col + 1;
    }
}