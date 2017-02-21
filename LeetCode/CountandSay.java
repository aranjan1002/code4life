public class CountandSay {
    public String countAndSay(int n) {
        String curr_seq = "1";
        for (int i = 2; i <= n; i++) {
            curr_seq = findNextSeq(curr_seq);
        }
        
        return curr_seq;
    }
    
    String findNextSeq(String str) {
        int idx = 0;
        String result = new String();
        while (idx <= str.length() - 1) {
            int cnt = 0;
            char curr_char = str.charAt(idx);
            while (idx <= str.length() - 1 && str.charAt(idx) == curr_char) {
                idx++;
                cnt++;
            }
            result += new Integer(cnt).toString() + curr_char;
        }
        
        return result;
    }
}