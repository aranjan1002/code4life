public class SingleNumberIII {
    public int[] singleNumber(int[] nums) {
        int x = 0;
        for (int n : nums) {
            x = x ^ n;
        }
        int diff_bit = 0;
        for (diff_bit = 0; diff_bit <= 31; diff_bit++) {
            if (((x >> diff_bit) & 1) == 1) {
                break;
            }
        }
        int[] result = new int[2];
        for (int n : nums) {
            if (((n >> diff_bit) & 1) == 1) {
                result[0] = result[0] ^ n;
            } else {
                result[1] = result[1] ^ n;
            }
        }
        return result;
    }
}