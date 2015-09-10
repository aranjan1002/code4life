import java.util.*;

public class Solution {
    public static void main(String[] args) {
	new Solution().solve();
    }

    public void solve() {
	solve(board);
	display();
    }

    void display() {
	for (int i = 0; i <= b - 1; i++) {
	    for (int j = 0; j <= l - 1; j++) {
		System.out.print(board[i][j] + " ");
	    }
	    System.out.println();
	}
	System.out.println();
    }

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

        /*display();
	int cnt = 1;
	for (int i = 1; i <= b - 2; i++) {
	    for (int j = 1; j <= l - 2; j++) {
		if (board[i - 1][j] == 'o' &&
		    board[i][j] == 'O') {
		    board[i][j] = 'o';
		}
	    }
	}

	for (int i = b - 2; i >= 1; i--) {
	    for (int j = 1; j <= l - 2; j++) {
		if (board[i + 1][j] == 'o' &&
		    board[i][j] == 'O') {
		    board[i][j] = 'o';
		}
	    }
	}

	for (int i = 1; i <= l - 2; i++) {
	    for (int j = 1; j <= b - 2; j++) {
		if (board[i][j - 1] == 'o' &&
		    board[i][j] == 'O') {
		    board[i][j] = 'o';
		}
	    }
	}

	for (int i = l - 2; i >= 1; i--) {
	    for (int j = 1; j <= b - 2; j++) {
		if (board[i][j + 1] == 'o' &&
		    board[i][j] == 'O') {
		    board[i][j] = 'o';
		}
	    }
	}*/


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
    
    void MakeSmallO(int i, int j) {
        board[i][j] = 'o';
        if (i - 1 >= 0) {
            if (board[i - 1][j] == 'O') {
                MakeSmallO(i - 1, j);
            }
        }
        
        if (j - 1 >= 0) {
            if (board[i][j - 1] == 'O') {
                MakeSmallO(i, j - 1);
            }
        }
        
        if (i + 1 <= b - 1) {
            if (board[i + 1][j] == 'O') {
                MakeSmallO(i + 1, j);
            }
        }
        
        if (j + 1 <= l - 1) {
            if (board[i][j + 1] == 'O') {
                MakeSmallO(i, j + 1);
            }
        }
    }
    
    char[][] board = {{'X', 'X', 'X', 'X'},
		      {'X', 'O', 'O', 'X'},
		      {'X', 'X', 'O', 'X'},
		      {'X', 'O', 'X', 'X'},
		      {'X', 'O', 'O', 'O'}};
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