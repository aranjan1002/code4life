public class BitwiseANDofNumbersRange {
    public int rangeBitwiseAnd(int m, int n) {
        if (m == n) {
            return m;
        }
        int i = 1;
        for (i = 1; i <= 32; i++) {
            if (((m >> (i - 1)) & 1) == 1) {
                int j = findDiff(m, i);
                if (n - m < j) {
                    break;
                }
            }
        }
        int mask = (1 << (32 - i + 1)) - 1;
        return (m & (mask << (i - 1)));
    }
    
    int findDiff(int num, int bit) {
        return ((1 << bit) - 1) - (((1 << bit) - 1) & num) + 1;
    }
}