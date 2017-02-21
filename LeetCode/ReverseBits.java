public class ReverseBits {
    // you need treat n as an unsigned value
    public int reverseBits(int n) {
        int b = 0;
        int cnt = 0;
        while (cnt < 32) {
            b = (b << 1) | (n & 1);
            n = n >> 1;
            cnt++;
        }
        return b;
    }
}