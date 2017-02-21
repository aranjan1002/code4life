public class LongestPalindromicSubstring {
    // Dynamic programming approach
    
    public String longestPalindrome(String s) {
        int n = s.length();
        boolean[][] isPalindrome = new boolean[n][n];
        int[] result = new int[2];
        int max_palindrome_length = 1;
        
        for (int i = 0; i <= n - 1; i++) {
            isPalindrome[i][i] = true;
            if ((i + 1 <= n - 1) && s.charAt(i) == s.charAt(i + 1)) {
                isPalindrome[i][i + 1] = true;
                max_palindrome_length = 2;
                result[0] = i;
                result[1] = i + 1;
            }
        }
        
        for (int j = 2; j <= n - 1; j++) {
            for (int i = 0; i <= n - j - 1; i++) {
                // System.out.println(i + " " + j);
                if (s.charAt(i) == s.charAt(i + j) &&
                    isPalindrome[i + 1][i + j - 1] == true) {
                    isPalindrome[i][i + j] = true;
                    // System.out.println(isPalindrome[i][j]);
                    if (j + 1 >max_palindrome_length) {
                        max_palindrome_length = j;
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
}