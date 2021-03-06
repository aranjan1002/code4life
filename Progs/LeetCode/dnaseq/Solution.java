import java.util.*;

public class Solution {
    public static void main(String[] args) {
	System.out.println(new Solution().findRepeatedDnaSequences
			   ("asdf"));
    }

    public List<String> findRepeatedDnaSequences(String s) {
        List<String> result = new ArrayList<String>();
        Set<String> substrings = new HashSet<String>();
        
        for (int i = 0; i <= s.length() - 10; i++) {
            String substr = s.substring(i, i + 10);
            if (substrings.contains(substr) == false) {
                substrings.add(substr);   
            } else {
                if (result.contains(substr) == false) {
                    result.add(substr);
                }
            }
        }
        
        return result;
    }
}