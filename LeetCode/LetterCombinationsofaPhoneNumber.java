public class LetterCombinationsofaPhoneNumber {
    public List<String> letterCombinations(String digits) {
        if (digits.length() == 0) {
            return new ArrayList<String>();
        }
        return letterCombinations(digits, 0);
    }
    
    public List<String> letterCombinations(String digits, int idx) {
        int digit = digits.charAt(idx) - 48;
        String[] candidate_chars = str[digit - 2];
        
        List<String> result;
        if (idx < digits.length() - 1) {
            result = letterCombinations(digits, idx + 1);
        } else {
            result = new ArrayList<String>();
            for (int i = 0; i <= candidate_chars.length - 1; i++) {
                result.add(candidate_chars[i]);
            }
            return result;
        }
        
        return addToResult(result, candidate_chars);
    }
    
    List<String> addToResult(List<String> result, String[] candidate_chars) {
        List<String> res = new ArrayList<String>();
        for (int i = 0; i <= candidate_chars.length - 1; i++) {
            for (int j = 0; j <= result.size() - 1; j++) {
                String concat_str = candidate_chars[i] + result.get(j);
                res.add(concat_str);
            }
        }
        
        return res;
    }
    
    String[][] str = {{"a", "b", "c"},
                      {"d", "e", "f"},
                      {"g", "h", "i"},
                      {"j", "k", "l"},
                      {"m", "n", "o"},
                      {"p", "q", "r", "s"},
                      {"t", "u", "v"},
                      {"w", "x", "y", "z"}};
}               