public class BullsandCows {
    public String getHint(String secret, String guess) {
        char[] s1 = secret.toCharArray();
        char[] s2 = guess.toCharArray();
        boolean[] cowFlag = new boolean[s1.length];
        
        int bulls = 0, cows = 0;
        
        for (int i = 0; i <= s1.length - 1; i++) {
            if (s1[i] == s2[i]) {
                bulls++;
                continue;
            }
            for (int j = 0; j <= s2.length - 1; j++) {
                if (s1[i] == s2[j] && s1[j] != s2[j] && cowFlag[j] == false) {
                        cows++;
                        cowFlag[j] = true;
                        break;
                }
            }
        }
        
        return bulls + "A" + cows + "B";
    }
}