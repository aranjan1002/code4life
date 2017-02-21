public class FactorialTrailingZeroes {
    public int trailingZeroes(int n) {
        int ans = 0;
        while (n >= 5) {
            ans += n / 5;
            n = n / 5;
        }
        return ans;
    }
}