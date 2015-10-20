import java.util.*;

class Spiral {
    public static void main(String[] args) {
	int[][] mat = {{1,2,3}, {4,5,6}, {7,8,9}};
	new Spiral().printSpiral(mat, 0, 0);
    }

    public void printSpiral(int[][] matrix, int i, int j) {
	int row = matrix.length;
	int col = matrix[0].length;
	i = i - 1;
	j = j + 1;

	for (int step = 2; step <= Math.max(row, col); step = step + 2) {
	    int cnt = 0;
	    if (j >= col) break;
	    // go down
	    while (cnt < step && i + 1 < row) {
		System.out.println(matrix[++i][j]);
		cnt++;
	    }
	    if (cnt < step) break;

	    cnt = 0;

	    // go left
            while (cnt < step && j - 1 >= 0) {
                System.out.println(matrix[i][--j]);
                cnt++;
            }
            if (cnt < step) break;

            cnt = 0;

	    // go up
            while (cnt < step && i - 1 >= 0) {
                System.out.println(matrix[--i][j]);
                cnt++;
            }
            if (cnt < step) break;

            cnt = 0;

	    // go right
            while (cnt < step && j + 1 < col) {
                System.out.println(matrix[i][++j]);
                cnt++;
            }
            if (cnt < step) break;

	    j++;
	}
    }
}
