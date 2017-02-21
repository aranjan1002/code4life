public class RepeatedDNASequences {
    public List<String> findRepeatedDnaSequences(String s) {
        Set<Integer> hash_set = new HashSet<Integer>();
        List<String> result = new ArrayList<String>();
        
        for (int i = 0; i <= s.length() - 10; i++) {
            String substr = s.substring(i, i + 10);
            Integer substr_hashcode = getHashCode(substr);
            if (hash_set.contains(substr_hashcode) == true && result.contains(substr) == false) {
                result.add(substr);
            } else {
                hash_set.add(substr_hashcode);
            }
        }
        
        return result;
    }
    
    Integer getHashCode(String str) {
        int hash = 0;
        for (int i = 0; i <= str.length() - 1; i++) {
            hash = hash << 2 | getCode(str.charAt(i));
        }
        
        return hash;
    }
    
    int getCode(char c) {
        if (c == 'A') {
            return 0;
        } else if (c == 'C') {
            return 1;
        } else if (c == 'G') {
            return 2;
        }
        return 3;
    }
}