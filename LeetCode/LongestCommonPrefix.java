public class LongestCommonPrefix {
    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) {
            return "";
        }
        String result = strs[0];
        for (String str : strs) {
            result = getLongestCommonPrefix(str, result);
        }
        return result;
    }
    
    String getLongestCommonPrefix(String str1, String str2) {
        int idx1 = 0, idx2 = 0;
        while (idx1 <= str1.length() - 1 && idx2 <= str2.length() - 1 && str1.charAt(idx1) == str2.charAt(idx2)) {
            idx1++;
            idx2++;
        }
        return str1.substring(0, idx1);
    }
}