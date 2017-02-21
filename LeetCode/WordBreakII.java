public class WordBreakII {
    HashMap<Integer, List<String>> map = new HashMap<Integer, List<String>>();
    public List<String> wordBreak(String s, Set<String> wordDict) {
        return wordBreak(s, wordDict, s.length() - 1);
    }
    
    public List<String> wordBreak(String s, Set<String> wordDict, int idx) {
        List<String> result = new ArrayList<String>();
        for (int i = idx; i >= 0; i--) {
            String substr = s.substring(i, idx + 1);
            if (wordDict.contains(substr) == true) {
                List<String> temp;
                if (i - 1 < 0) {
                    result.add(substr);
                    break;
                }
                if (map.containsKey(i - 1) == true) temp = map.get(i - 1);
                else temp = wordBreak(s, wordDict, i - 1);
                //System.out.println(i - 1 + " got " + temp);
                if (temp.size() >= 1) {
                    for (String str : temp) {
                        str += " " + substr;
                        result.add(str);
                    }
                }
            }
        }
        map.put(idx, result);
        //System.out.println(idx + " " + result);
        return result;
    }
}