public class LongestSubstringWithoutRepeatingCharacters {
    public int lengthOfLongestSubstring(String s) {
        if (s.length() == 0) {
            return 0;
        }   
        
        int start = 0;
        int idx = 0;
        int curr_length = 0, max_length = 0;
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        while (idx <= s.length() - 1) {
            Character curr_char = s.charAt(idx);
            int prev_idx = -1;
            if (map.containsKey(curr_char) == true) {
                prev_idx = map.get(curr_char).intValue();
            }
            if (prev_idx < start) {
                map.put(curr_char, idx);
                curr_length++;
                if(curr_length > max_length) {
                    max_length = curr_length;
                }
            } else {
                start = prev_idx + 1;
                curr_length = idx - start + 1;
                map.put(curr_char, new Integer(idx));
            }
            idx++;
        }
        return max_length;
    }
}