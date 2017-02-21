public class ValidAnagram {
    public boolean isAnagram(String s, String t) {
        if (s == null && t == null) {
            return true;
        }
        if (s == null || t == null) {
            return false;
        }
        if (s.length() != t.length()) {
            return false;
        }
        
        int[] cnt = new int[26];
        
        boolean[] flag = new boolean[t.length()];
        for (int i = 0; i <= s.length() - 1; i++) {
            cnt[s.charAt(i) - 'a']++;
        }
        
        for (int j = 0; j <= t.length() - 1; j++) {
            cnt[t.charAt(j) - 'a']--;
            if (cnt[t.charAt(j) - 'a'] < 0) {
                return false;
            }        
        }
        
        for (int i = 0; i <= 25; i++) {
            if (cnt[i] != 0) {
                return false;
            }
        }
        
        return true;
    }
}