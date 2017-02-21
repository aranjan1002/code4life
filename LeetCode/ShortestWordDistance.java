public class ShortestWordDistance {
    public int shortestDistance(String[] words, String word1, String word2) {
        int result = Integer.MAX_VALUE;
        int lastA = words.length + 1;
        int lastB = words.length + 1;
        for (int i = 0; i <= words.length - 1; i++) {
            if (words[i].equals(word1) == true) {
                lastA = i;
                result = Math.min(result, Math.abs(i - lastB));
            } else if (words[i].equals(word2) == true) {
                lastB = i;
                result = Math.min(result, Math.abs(i - lastA));
            }
        }
        return result;
    }
}