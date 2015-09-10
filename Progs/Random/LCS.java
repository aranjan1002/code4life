public class LCS { 
    public String findLCS(String s1, String s2) {
	int[][] matrix = new int[s1.length()][s2.length()];
	int maxi = 0, maxj = 0, max = 0;
	matrix[0][0] = 0;
	for (int i = 1; i <= s1.length() - 1; i++) {
	    if (s1.charAt(i) == s2.charAt(0)) {
		matrix[i][0] = 1;
		max = 1;
		maxi = i;
		maxj = 0;
	    } else {
		matrix[i][0] = 0;
	    }
	}

	for (int i = 1; i <= s2.length() - 1; i++) {
	    if (s2.charAt(i) == s1.charAt(0)) {
		matrix[0][i] = 1;
		max = 1;
		maxi = 0;
		maxj = i;
	    } else {
		matrix[0][i] = 0;
	    }
	}

	for (int i = 1; i <= s1.length() - 1; i++) {
	    for (int j = 1; j <= s2.length() - 1; j++) {
		if (s1.charAt(i) == s2.charAt(j)) {
		    matrix[i][j] = matrix[i - 1][j - 1] + 1;
		    if (matrix[i][j] > max) {
			max = matrix[i][j];
			maxi = i;
			maxj = j;
		    }
		} else {
		    matrix[i][j] = 0;
		}
		System.out.print(matrix[i][j] + " ");
	    }
	    System.out.println();
	}
	    
	if (max == 0) {
	    return "";
	} else {
	    String result = new String();
	    while (maxi >= 0 && maxj >= 0 && matrix[maxi][maxj] >= 1) {
		result = Character.toString(s1.charAt(maxi)) + result;
		maxi--;
		maxj--;
	    }
	    return result;
	}
    }

    public static void main(String[] args) {
	System.out.println(new LCS().findLCS("ABABC", "BABCA"));
    }
}