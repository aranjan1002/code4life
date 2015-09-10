public class Solution {
    public static void main(String[] args) {
	System.out.println(new Solution().longestPalindrome("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabcaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }
    
    public String longestPalindrome(String s) {
        int n = s.length();
        int[][] isPalindrome = new int[n][n];
        int[] result = new int[2];
        int max_palindrome_length = 1;
        
        for (int i = 0; i <= n - 1; i++) {
            isPalindrome[i][i] = 1;
            if ((i + 1 <= s.length() - 1) && s.charAt(i) == s.charAt(i + 1)) {
                isPalindrome[i][i + 1] = 1;
            }
        }
        
        for (int j = 2; j <= n - 1; j++) {
            for (int i = 0; i <= n - j - 1; i++) {
                // System.out.println(i + " " + j);
                if (s.charAt(i) == s.charAt(i + j) &&
                    isPalindrome[i + 1][i + j - 1] > 0) {
                    isPalindrome[i][i + j] = isPalindrome[i + 1][i + j - 1] + 1;
                    // System.out.println(isPalindrome[i][j]);
                    if (isPalindrome[i][i + j] > max_palindrome_length) {
                        max_palindrome_length = isPalindrome[i][i + j];
                        result[0] = i;
                        result[1] = i + j;
                        // System.out.println(max_palindrome_length);
                    }
                }
            }
        }

        // System.out.println(result[0] + " " + result[1]);
        return s.substring(result[0], result[1] + 1);

    }

    public String longestPalindrome2(String s) {
        int curr_size = 0;
        int[] result_idx = new int[2];
        for (int i = 0; i <= s.length() - 1; i++) {
            for (int j = s.length() - 1; j > i; j--) {
		// System.out.println(i + " " + j);
                if (s.charAt(i) == s.charAt(j) && j - i + 1 > curr_size) {
                    if (isPalindrome(s, i, j) == true) {
                        result_idx[0] = i;
                        result_idx[1] = j + 1;
			curr_size = j - i + 1;
			// System.out.println(curr_size);
                    }       
		}
            }
        }
	System.out.println(result_idx[0] + " " + result_idx[1]);
	System.out.println(s.length());
        
        return s.substring(result_idx[0], result_idx[1]);
    }
    
    boolean isPalindrome(String s, int idx1, int idx2) {
	// System.out.println("Checking palindrome: " + idx1 + " " + idx2);
        while (idx1 < idx2) {
            if (s.charAt(idx1) != s.charAt(idx2)) {
		// System.out.println(idx1 + " " + idx2);
                return false;
            }
            idx1++;
            idx2--;
        }
        // System.out.println("returning true");
        return true;
    }
}