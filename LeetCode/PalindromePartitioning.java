public class PalindromePartitioning {
    public List<List<String>> partition(String s) {
        if (s.length() == 0) {
            return result;
        }
        
        List<String> curr_partition = new ArrayList<String>();
        partition(s, 0, curr_partition);
        return result;
    }
    
    public void partition(String s, int idx, List<String> curr_partition) {
        if (idx == s.length()) {
            result.add(curr_partition);
            return;
        }
        
        for (int i = idx; i <= s.length() - 1; i++) {
            if (checkPalindrome(s, idx, i) == true) {
                List<String> new_partition = new ArrayList(curr_partition);
                new_partition.add(s.substring(idx, i + 1));
                partition(s, i + 1, new_partition);
            }
        }
    }
    
    boolean checkPalindrome(String s, int idx1, int idx2) {
        for (int i = 0; i + idx1 < idx2 - i; i++) {
            if (s.charAt(i + idx1) != s.charAt(idx2 - i)) {
                return false;
            }
        }
        return true;
    }
    
    List<List<String>> result = new ArrayList<List<String>>();
}