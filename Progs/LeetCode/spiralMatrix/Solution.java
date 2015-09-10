public class Solution {
    public static void main(String[] args) {
	new Solution().generateMatrix(4);
    }

    public int[][] generateMatrix(int n) {
        int[][] result = new int[n][n];
        int dist = 0;
        
        int cnt = 1;
        while (cnt <= n * n) {
	    for (int i = dist; i <= n - 1 - dist; i++) {
		System.out.println(i + " " + cnt);
		result[dist][i] = cnt++;
		print(result);
	    }
	    for (int i = dist + 1; i <= n - 1 - dist; i++) {
		result[i][n - 1 - dist] = cnt++;
		print(result);
	    }
	    for (int i = n - dist - 2; i >= dist; i--) {
		result[n - 1 - dist][i] = cnt++;
	    }
	    for (int i = n - 2 - dist; i >= dist + 1; i--) {
		result[i][dist] = cnt++;
	    }
	    dist++;
        }
	print (result);
	return result;
    }

    void print(int[][] A) {
	int row = A.length;
	int col = A[0].length;

	for (int i = 0; i <= row - 1; i++) {
	    for(int j = 0; j <= col - 1; j++) {
		System.out.print(A[i][j] + " ");
	    }
	    System.out.println();
	}
    }
}