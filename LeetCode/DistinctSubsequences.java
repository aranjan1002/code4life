public class DistinctSubsequences {
    public int numDistinct(String S, String T) {
        int[] ways_to_reach_char = new int[T.length()];
        
        for (int i = 0; i <= S.length() - 1; i++) {
            List<Integer> char_indices_list = getCharIndices(S.charAt(i), T);
            
            for (int j = 0; j <= char_indices_list.size() - 1; j++) {
                int idx = char_indices_list.get(j).intValue();
                if (idx == 0) {
                    ways_to_reach_char[idx]++;
                } else {
                    ways_to_reach_char[idx] += ways_to_reach_char[idx - 1];
                }
            }
        }
        
        return ways_to_reach_char[T.length() - 1];
    }
    
    List<Integer> getCharIndices(char c, String T) {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = T.length() - 1; i >= 0; i--) {
            if (T.charAt(i) == c) {
                result.add(new Integer(i));
            }
        }
        
        return result;
    }
}