public class ReverseWordsinaString {
    public String reverseWords(String s) {
        List<String> words = new ArrayList<String>();
        boolean startSpace = false, endSpace = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= s.length() - 1; i++) {
            if (s.charAt(i) == ' ') {
                if (i == 0) {
                    startSpace = true;
                } else if (i == s.length() - 1) {
                    //endSpace = true;
                }
                if (sb.length() > 0) {
                    words.add(sb.toString());
                    sb = new StringBuilder();
                }
            } else {
                sb.append(Character.toString(s.charAt(i)));
            }
        }
        if ("".equals(sb.toString()) == false) {
            words.add(sb.toString());
        }
        StringBuilder result = new StringBuilder();
        int idx = words.size() - 1;
        if (idx >= 0) {
            result.append(words.get(idx--));
        }

        for (int i = idx; i >= 0; i--) {
            result = result.append(" ").append(words.get(i));
        }
        
        if (endSpace == true) {
            result.append(" ");
        }
        
        // if (startSpace == true && result.length() >= 1) {
        //     return (" " + result.toString());
        // }
        return result.toString();
    }
}