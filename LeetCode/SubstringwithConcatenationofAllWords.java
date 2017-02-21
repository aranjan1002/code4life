public class SubstringwithConcatenationofAllWords {
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> result = new ArrayList<Integer>();
        if (s == null || words.length == 0) {
            return result;
        }
        
        int word_length = words[0].length();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (String word : words) {
            int cnt = 1;
            if (map.containsKey(word) == true) {
                cnt = cnt + map.get(word);
            }
            map.put(word, cnt);
        }
        
        for (int i = 0; i <= s.length() - (word_length * words.length); i++) {
            HashMap<String, Integer> new_map = new HashMap<String, Integer>(map);
            for (int j = i; ; j = j + word_length) {
                String sub = s.substring(j, j + word_length);
                if (new_map.containsKey(sub) == true) {
                    int cnt = new_map.get(sub);
                    if (cnt == 1) {
                        new_map.remove(sub);
                    } else {
                        new_map.put(sub, cnt - 1);
                    }
                    if (new_map.isEmpty() == true) {
                        result.add(i);
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return result;
    }
}