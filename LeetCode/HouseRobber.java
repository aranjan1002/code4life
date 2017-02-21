public class HouseRobber {
    public int rob(int[] nums) {
        if (nums.length == 0) return 0;
        int[] maxIncluding = new int[nums.length];
        int[] maxExcluding = new int[nums.length];
        
        maxIncluding[0] = nums[0];
        maxExcluding[0] = 0;
        for (int i = 1; i <= nums.length - 1; i++) {
            maxIncluding[i] = maxExcluding[i - 1] + nums[i];
            maxExcluding[i] = Math.max(maxIncluding[i - 1], maxExcluding[i - 1]);
        }
        return Math.max(maxIncluding[nums.length - 1], maxExcluding[nums.length - 1]);
    }
}