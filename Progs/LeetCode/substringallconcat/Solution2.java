import java.util.*;
import java.lang.*;

public class Solution2 {
    public static void main(String[] args) {
	String S = "barfoothefoobarman";
	String[] L = {"foo","bar"};

	System.out.println(new Solution2().findSubstring(S, L));
    }

    public List<Integer> findSubstring(String S, String[] L) {
        List<Integer> result = new ArrayList<Integer>();
        if (L.length == 0 || S.length() == 0) {
            return result;
        }
        
        Map<String, Integer> freq_L = makeFrequencyMap(L);
        
        int num_of_words = L.length;
        int word_length = L[0].length();
        int total_chars = num_of_words * word_length;
        Map<String, Integer> freq_S = new HashMap<String, Integer>();
        for (int i = 0; i <= S.length() - total_chars - 1; i++) {
            int word_count = 0;
            freq_S.clear();
            int start = i;
            for (int j = i; j <= S.length() - 1 - word_length; 
		 j = j + word_length) {
		String str = S.substring(i, i + word_length);
		if (freq_L.containsKey(str) == true) {
		    if (freq_S.containsKey(str) == false) {
                        freq_S.put(str, new Integer(1));
                        word_count++;
		    } else {
                        int max_freq = freq_L.get(str).intValue();
                        int curr_freq = freq_S.get(str).intValue();
                        if (curr_freq < max_freq) {
                            freq_S.put(str, new Integer(curr_freq + 1));
                            word_count++;
                        } else {
			    System.out.println(freq_L);
			    System.out.println(freq_S);
			    System.out.println(start);
			    System.out.println(result);
                            String curr_word = 
				S.substring(start, start + word_length);
                            while (curr_word.equals(str) == false) {
				System.out.println(curr_word);
                                int curr_word_freq = 
				    freq_S.get(curr_word).intValue();
                                freq_S.put(curr_word, 
					   new Integer(curr_word_freq - 1));
                                start += word_length;
                                curr_word = S.substring(start, start + word_length);
                            }
			    start += word_length;
                        }
                    }
                }  else {
		    start = j + word_length;
		    freq_S.clear();
		}
                if (word_count == num_of_words) {
                    result.add(new Integer(start));
                    start += word_length;
                    word_count--;
		    System.out.println("result = " + freq_S + num_of_words);
                }
            } 
        }
        return result;
    }
    
    Map<String, Integer> makeFrequencyMap(String[] L) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (int i = 0; i <= L.length - 1; i++) {
            if (result.containsKey(L[i]) == false) {
                result.put(L[i], new Integer(1));
            } else {
                Integer freq = result.get(L[i]);
                Integer new_freq = new Integer(freq.intValue() + 1);
                result.put(L[i], new_freq);
            }
        }
        
        return result;
    }
}