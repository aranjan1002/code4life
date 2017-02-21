public class MaximumProductSubarray {
    public int maxProduct(int[] nums) {
        int[] min = new int[nums.length];
        int[] max = new int[nums.length];
        int m;
        if (nums.length == 0) {
            return 0;
        }
        min[0] = nums[0];
        max[0] = nums[0];
        m = nums[0];
        for (int i = 1; i <= nums.length - 1; i++) {
            if (nums[i] >= 0) {
                max[i] = Math.max(max[i - 1] * nums[i], nums[i]);
                min[i] = Math.min(nums[i], nums[i] * min[i - 1]);
            } else {
                max[i] = Math.max(min[i - 1] * nums[i], nums[i]);
                min[i] = Math.min(max[i - 1] * nums[i], nums[i]);
            }
            m = (max[i] > m) ? max[i] : m;
        }
        
        return m;
    }
}