public class Numberof1Bits {
    // you need to treat n as an unsigned value
    public int hammingWeight(int n) {
        int cnt = 0;
        for (int i = 1; i <= 32; i++) {
            cnt += n & 1;
            n = n >> 1;
        }
        
        return cnt;
    }
}