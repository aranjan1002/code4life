public class IsomorphicStrings {
    public boolean isIsomorphic(String s, String t) {
        HashMap<Character, Character> map = new HashMap<Character, Character>();
        HashSet<Character> mapped = new HashSet<Character>();
    	if (s == null && t == null) {
    		return true;
    	}
    	if (s == null || t == null) {
    		return false;
    	}
    	if (s.length() != t.length()) {
    		return false;
    	}
    	for (int i = 0; i <= s.length() - 1; i++) {
    		char c1 = s.charAt(i);
    		char c2 = t.charAt(i);
    		if (map.containsKey(c1) == true) {
			    char c = map.get(c1);
				if (c != c2) {
					return false;
				}
    		} else {
    		    if (mapped.contains(c2)) {
    		        return false;
    		    }
    			map.put(c1, c2);
    			mapped.add(c2);
    		}
    	}
    	return true;
    }
}