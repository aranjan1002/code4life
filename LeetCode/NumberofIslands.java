public class NumberofIslands {
    public int numIslands(char[][] grid) {
        if (grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int result = 0;
        int row = grid.length;
        int col = grid[0].length;
        boolean[][] flag = new boolean[row][col];
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j <= col - 1; j++) {
                if (grid[i][j] == '1' && flag[i][j] == false) {
                    visit(grid, flag, i, j);
                    result++;
                }
            }
        }
        return result;
    }
    
    void visit(char[][] grid, boolean[][] flag, int r, int c) {
        if (flag[r][c] == true || grid[r][c] == '0') {
            return;
        }
        flag[r][c] = true;
        int row = grid.length;
        int col = grid[0].length;
        
        if (c - 1 >= 0) {
            visit(grid, flag, r, c - 1);
        }
        if (r - 1 >= 0) {
            visit(grid, flag, r - 1, c);
        }
        if (c + 1 <= col - 1) {
            visit(grid, flag, r, c + 1);
        }
        if (r + 1 <= row - 1) {
            visit(grid, flag, r + 1, c);
        }
    }
}