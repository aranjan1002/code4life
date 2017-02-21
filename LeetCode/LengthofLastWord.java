public class LengthofLastWord {
    public int lengthOfLastWord(String s) {
        int idx = s.length() - 1;
        
        while (idx >= 0 && s.charAt(idx) == ' ') {
            idx--;
        }
        
        if (idx < 0) {
            return 0;
        }
        int cnt = 0;
        while (idx >= 0 && s.charAt(idx) != ' ') {
            cnt++;
            idx--;
        }
        
        return cnt;
    }
}