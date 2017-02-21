public class SingleNumberII {
    public int singleNumber(int[] nums) {
        int[] bits_count = new int[32];
        for (int n : nums) {
            for (int i = 0; i < 32; i++) {
                if (((n >> i) & 1) == 1) {
                    bits_count[i] = (bits_count[i] + 1) % 3;
                }
            }
        }
        int result = 0;
        for (int i = 0; i <= 31; i++) {
            //System.out.println(i + " " + bits_count[i]);
            if (bits_count[i] > 0) {
                result = result | (1 << i);
            }
        }
        return result;
    }
}