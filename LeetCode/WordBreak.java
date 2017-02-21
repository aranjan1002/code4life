public class WordBreak {
    public boolean wordBreak(String s, Set<String> wordDict) {
        if (s == null && wordDict == null) {
            return true;
        }
        
        if (s == null || wordDict == null) {
            return false;
        }
        
        boolean[] dp = new boolean[s.length()];
        
        for (int i = 1; i <= s.length(); i++) {
            if (wordDict.contains(s.substring(0, i)) == true) {
                dp[i - 1] = true;
            }
        }
        
        for (int i = 1; i <= s.length() - 1; i++) {
            for (int j = 0; j <= i - 1; j++) {
                if (dp[j] == true && wordDict.contains(s.substring(j + 1, i + 1)) == true) {
                    dp[i] = true;
                }
            }
        }
        
        return dp[s.length() - 1] == true;
    }
}