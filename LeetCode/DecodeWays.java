public class DecodeWays {
    public int numDecodings(String s) {
        if (s.length() == 0 || "0".equals(s)) {
            return 0;
        }
        mem = new int[s.length() + 1];
        init();
        return numDecodings(s, 0);
    }
    
    public void init() {
        for (int i = 0; i <= mem.length - 1; i++) {
            mem[i] = -1;
        }
    }
    
    public int numDecodings(String s, int idx) {
        if (idx == s.length()) {
            return 1;
        } 
        
        if (s.charAt(idx) == '0') {
            return 0;
        }
        int result = 0;
        if (mem[idx + 1] == -1) {
            mem[idx + 1] = numDecodings(s, idx + 1);
        }
        result += mem[idx + 1];
        if (isTwoDigitEligible(s, idx) == true) {
            if (mem[idx + 2] == -1) {
                mem[idx + 2] = numDecodings(s, idx + 2);
            }
            result += mem[idx + 2];
        } 
        
        return result;
    }
    
    boolean isTwoDigitEligible(String s, int idx) {
        if (idx == s.length() - 1) {
            return false;
        }
        
        Integer d1 = new Integer(s.charAt(idx) - 48);
        if (d1 == 0) {
            return false;
        }
        
        Integer d2 = new Integer(s.charAt(idx + 1) - 48);
        
        int n = d1.intValue() * 10 + d2.intValue();
        if (n <= 26) {
            return true;
        }
        return false;
    }
    
    int[] mem;
}