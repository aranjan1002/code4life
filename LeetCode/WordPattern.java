public class WordPattern {
    public boolean wordPattern(String pattern, String str) {
        Map<Character, String> map1 = new HashMap<Character, String>();
        Map<String, Character> map2 = new HashMap<String, Character>();
        
        String[] str_arr = str.split(" ");
        if (str_arr.length != pattern.length()) {
            return false;
        }
        for (int i = 0; i <= pattern.length() - 1; i++) {
            Character c = new Character(pattern.charAt(i));
            
            if (map1.containsKey(c) == false && map2.containsKey(str_arr[i]) == false) {
                map1.put(c, str_arr[i]);
                map2.put(str_arr[i], c);
            } else {
                if (map1.containsKey(c) == false || map2.containsKey(str_arr[i]) == false) {
                    return false;
                }
                String s = map1.get(c);
                if (s.equals(str_arr[i]) == false) {
                    return false;
                }
                Character c_map = map2.get(s);
                if (c_map.equals(c) == false) {
                    return false;
                }
            }
        }
        
        return true;
    }
}