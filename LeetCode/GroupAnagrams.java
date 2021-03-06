public class GroupAnagrams {
    public List<String> anagrams(String[] strs) {
        String[] sorted_strs = sort(strs);
        List<String> result = new ArrayList<String>();
        Map<String, Integer> tab = new HashMap<String, Integer>();
        
        for (int i = 0; i < sorted_strs.length; i++) {
            if (tab.containsKey(sorted_strs[i])) {
                tab.put(sorted_strs[i], 2);
            } else {
                tab.put(sorted_strs[i], 1);
            }
        }
        
        for (int i = 0; i < sorted_strs.length; i++) {
            if (tab.get(sorted_strs[i]).equals(new Integer(2))) {
                result.add(strs[i]);
            }
        }
        
        return result;
    }
    
    public String[] sort(String[] strs) {
        String[] sorted_strs = new String[strs.length];
        for (int i = 0; i < strs.length; i++) {
            sorted_strs[i] = sort_string(strs[i]);
        }
        
        return sorted_strs;
    }
    
    public String sort_string(String str) {
        char[] sorted_str = str.toCharArray();
        
        for (int i = 0;  i < sorted_str.length; i++) {
            for (int j = i + 1; j < sorted_str.length; j++) {
                if (sorted_str[i] > sorted_str[j]) {
                    char temp = sorted_str[i];
                    sorted_str[i] = sorted_str[j];
                    sorted_str[j] = temp;
                }
            }
        }
        
        return new String(sorted_str);
    }
}