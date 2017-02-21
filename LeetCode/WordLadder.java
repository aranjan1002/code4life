public class WordLadder {
    public int ladderLength(String start, String end, Set<String> dict) {
        Set<String> used_words = new HashSet<String>();
        Set<String> temp_words = new HashSet<String>();
        used_words.add(start);
        
        int curr_min_distance = calcDistance(start, end);
        int curr_distance = 1;
        if (curr_min_distance <= 1) {
            return curr_distance + curr_min_distance;
        } else {
            while (curr_min_distance > 1) {
                curr_distance++;
                temp_words.clear();
                Iterator<String> unused_words_itr = dict.iterator();
                while (unused_words_itr.hasNext() == true) {
                    String word = unused_words_itr.next();
                    if (isDistanceOne(word, used_words) == true) {
                        temp_words.add(word);
                        unused_words_itr.remove();
                        int new_distance = calcDistance(word, end);
                        if (new_distance < curr_min_distance) {
                            curr_min_distance = new_distance;
                        }
                    }
                }
                if (temp_words.size() == 0) {
                    return 0;
                }
                used_words.clear();
                used_words.addAll(temp_words);
            }
            
            curr_distance += curr_min_distance;
            return curr_distance;
        }
    }
    
    int calcDistance(String a, String b) {
        int dist = 0;
        for (int i = 0; i <= a.length() - 1; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                dist++;
            }
        }
        
        return dist;
    }
    
    boolean isDistanceOne(String word, Set<String> words) {
        Iterator<String> it = words.iterator();
        
        while (it.hasNext() == true) {
            String w = it.next();
            if (calcDistance(w, word) == 1) {
                return true;
            }
        }
        
        return false;
    }
}