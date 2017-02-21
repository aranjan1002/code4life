public class ValidSudoku {
    public boolean isValidSudoku(char[][] board) {
        for (int i = 0; i <= 8; i++) {
            if (isZeroToNineUnique(board, i, i, 0, 8) == false) {
                return false;
            }
            if (isZeroToNineUnique(board, 0, 8, i, i) == false) {
                return false;
            }
        }
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (isZeroToNineUnique(board, i * 3, (i * 3) + 2, j * 3, (j * 3) + 2) == false) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public boolean isZeroToNineUnique(char[][] board, int row1, int row2, int col1, int col2) {
        Set <Character> set = new HashSet<Character>();
        for (int row = row1; row <= row2; row++) {
            for (int col = col1; col <= col2; col++) {
                Character c = new Character(board[row][col]);
                if (set.contains(c) == true) {
                    return false;
                } else if (c != '.') {
                    set.add(c);
                }
            }
        }
        return true;
    }
}