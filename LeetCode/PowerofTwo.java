public class PowerofTwo {
    public boolean isPowerOfTwo(int n) {
        if (n == 0 || n == Integer.MAX_VALUE || n == Integer.MIN_VALUE) return false;
        return ((n - 1) & n) == 0;
    }
}