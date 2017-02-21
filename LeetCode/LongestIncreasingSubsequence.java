public class LongestIncreasingSubsequence {
    public int lengthOfLIS(int[] nums) {
        int[] result = new int[nums.length];
        if (nums.length == 0) {
            return 0;
        }
        
        result[0] = 1;
        
        for (int i = 1; i <= nums.length - 1; i++) {
            int maxSoFar = 0;
            for (int j = i - 1; j >= 0; j--) {
                if (nums[j] < nums[i] && result[j] > maxSoFar) {
                    maxSoFar = result[j];
                }
            }
            result[i] = maxSoFar + 1;
        }
        
        int max = 0;
        for (int i = 0; i <= result.length - 1; i++) {
            if (result[i] > max) {
                max = result[i];
            }
        }
        
        return max;
    }
}