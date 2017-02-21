public class JumpGame {
    public boolean canJump(int[] A) {
        if (A.length == 0) {
            return false;
        }
        
        boolean[] mem = new boolean[A.length];
        mem[0] = true;
        int set_until = 0;
        
        for (int i = 0; i <= A.length - 1; i++) {
            if (mem[i] == false) {
                return false;
            }
            for (int j = set_until + 1; j <= i + A[i]; j++) {
                if (j <= A.length - 1) {
                    mem[j] = true;
                    set_until++;
                }
            }
            
            if (set_until > A.length - 1) {
                return true;
            }
        }
        
        return mem[A.length - 1];
    }
}