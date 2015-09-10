public class Solution {
    public static void main(String[] args) {
	new Solution().isPalindrome("A man, a plan, a canal: Panama");
    }
    public boolean isPalindrome(String s) {
        if (s.length() == 0) {
            return true;
        }
	s = modify(s);
	System.out.println(s);
	return true;
    }
    
    String modify(String str) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i <= str.length() - 1; i++) {
            int c = (int) str.charAt(i);
            if ((65 <= c && c <= 90) || (97 <= c && c <= 122)) {
                if (65 <= c && c <= 90) {
                    c = c + 32;
                }
                s.append((char) c);
            }
        }
        return s.toString();
    }
}