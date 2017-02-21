public class LongestValidParentheses {
    public int longestValidParentheses(String s) {
        if(s == null || s.length() <= 1) {
            return 0;
        } 
        
        int maxLen = 0;
        Stack<Integer> st = new Stack<Integer>();
        for (int i = 0; i <= s.length() - 1; i++) {
            if (s.charAt(i) == '(') {
                st.push(i);
            } else {
                if (st.isEmpty() == false && s.charAt(st.peek()) == '(') {
                    st.pop();
                    int start = -1;
                    if (st.isEmpty() == false) {
                        start = st.peek();
                    }
                    int curLen = i - start;
                    if (curLen > maxLen) {
                        maxLen = curLen;
                    }
                } else {
                    st.push(i);
                }
            }
        }
        
        return maxLen;
    }
}