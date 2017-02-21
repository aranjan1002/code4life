public class SurroundedRegions {
     public void solve(char[][] board) {
        this.board = board;
        b = board.length;

        if (b == 0) {
            return;
        }

        l = board[0].length;

        if (l == 0) {
            return;
        }
        Stack s = new Stack();

        for (int i = 0; i <= l - 1; i++) {
            if (board[0][i] == 'O') {
                board[0][i] = 'o';
                s.push(0, i);
            }
            if (board[b - 1][i] == 'O') {
                board[b - 1][i] = 'o';
                s.push(b - 1, i);
            }
        }
        for (int j = 0; j <= b - 1; j++) {
            if (board[j][0] == 'O') {
                board[j][0] = 'o';
                s.push(j, 0);
            }

            if (board[j][l - 1] == 'O') {
                board[j][l - 1] = 'o';
                s.push(j, l - 1);
            }
        }
        // display();

        while(s.isEmpty() == false) {
            int[] int_array = s.pop();
            int row = int_array[0];
            int col = int_array[1];
            board[row][col] = 'o';
            if (row > 0) {
                if (board[row - 1][col] == 'O') {
                    s.push(row - 1, col);
                }
            }
            if (col > 0) {
                 if (board[row][col - 1] == 'O') {
                    s.push(row, col - 1);
                }
            }

            if (row + 1 <= b - 1) {
                if (board[row + 1][col] == 'O') {
                    s.push(row + 1, col);
                }
            }

            if (col + 1 <= l - 1) {
                if (board[row][col + 1] == 'O') {
                    s.push(row, col + 1);
                }
            }
            // display();
        }
        
        for (int i = 0; i <= b - 1; i++) {
            for (int j = 0; j <= l - 1; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
                if (board[i][j] == 'o') {
                    board[i][j] = 'O';
                }
            }
        }
    }
    
 
    char[][] board;
    int b = 0;
    int l = 0;
    
    class Stack {
        void push(int a, int b) {
            Integer[] x = new Integer[2];
            x[0] = new Integer(a);
            x[1] = new Integer(b);
            s.add(idx++, x);
        }

        int[] pop() {
            int[] x = new int[2];
            Integer[] integer_array = s.get(--idx);
            x[0] = integer_array[0].intValue();
            x[1] = integer_array[1].intValue();
            return x;
        }

        boolean isEmpty() {
            if (idx == 0) {
                return true;
            }
            return false;
        }

        List<Integer[]> s = new ArrayList<Integer[]>();
        int idx = 0;
    }
}