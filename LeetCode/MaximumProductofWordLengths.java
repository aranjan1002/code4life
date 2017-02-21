public class MaximumProductofWordLengths {
    public int maxProduct(String[] words) {
        Arrays.sort(words, new Comparator() {
            public int compare(Object o1, Object o2) {
                int l1 = ((String) o1).length();
                int l2 = ((String) o2).length();
                return Integer.compare(l1, l2);
            }
        });
        int result = 0;
        for (int i = words.length - 1; i >= 1; i--) {
            if (words[i].length() * words[i - 1].length() <= result) break;
            for (int j = i - 1; j >= 0; j--) {
                if (words[j].length() * words[i].length()  <= result) break;
                if (letterMatch(words[i], words[j]) == false) {
                    if (words[i].length() * words[j].length() > result) {
                        result = words[i].length() * words[j].length();
                    }
                }
            }
        }
        
        return result;
    }
    
    boolean letterMatch(String word1, String word2) {
        for (int i = 0; i <= word1.length() - 1; i++) {
            for (int j = 0; j <= word2.length() - 1; j++) {
                if (word1.charAt(i) == word2.charAt(j)) return true;
            }
        }
        return false;
    }
}