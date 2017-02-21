public class ValidPalindrome {
    public boolean isPalindrome(String s) {
        if (s.length() == 0) {
            return true;
        }
        s = modify(s);
        return isPalindrome2(s);
    }
    
    String modify(String str) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i <= str.length() - 1; i++) {
            int c = (int) str.charAt(i);
            if ((65 <= c && c <= 90) || (97 <= c && c <= 122) || (48 <= c && c <= 57)) {
                if (65 <= c && c <= 90) {
                    c = c + 32;
                }
                s.append((char) c);
            }
        }
        return s.toString();
    }
    
    boolean isPalindrome2(String s) {
        int head = 0;
        int tail = s.length() - 1;
        
        while (head < tail) {
            if (s.charAt(head) != s.charAt(tail)) {
                return false;
            }
            head++;
            tail--;
        }
        
        return true;
    }

}