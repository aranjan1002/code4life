public class ImplementstrStr {
    public int strStr(String haystack, String needle) {
        if (haystack == null || needle == null) return -1;
        if ("".equals(needle) == true) return 0;
        for (int i = 0; haystack.length() - i > needle.length() - 1; i++) {
            int cnt1 = i, cnt2 = 0;
            while (cnt1 <= haystack.length() - 1 && cnt2 <= needle.length() - 1 && 
                   haystack.charAt(cnt1) == needle.charAt(cnt2)) {
                       cnt1++;
                       cnt2++;
                   } 
            if (cnt2 == needle.length()) return i;
        }
        return -1;
    }
}