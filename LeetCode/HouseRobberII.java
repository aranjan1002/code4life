public class HouseRobberII {
    public int rob(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        int[] r = new int[nums.length];
        int[] nr = new int[nums.length];
        
        // Case when house 1 may be robbed
        r[0] = nums[0];
        nr[0] = 0;
        for (int i = 1; i <= nums.length - 1; i++) {
            r[i] = nr[i - 1] + nums[i];
            nr[i] = Math.max(nr[i - 1], r[i - 1]);
        }
        int result1 = nr[nums.length - 1];
        if (nr[nums.length - 1] >= r[nums.length - 1]) {
            return nr[nums.length - 1];
        } else {
            // Case when houe 1 is not robbed
            r[0] = 0;
            nr[0] = 0;
            for (int i = 1; i <= nums.length - 1; i++) {
                r[i] = nr[i - 1] + nums[i];
                nr[i] = Math.max(nr[i - 1], r[i - 1]);
            }
            return Math.max(Math.max(nr[nums.length - 1], r[nums.length - 1]), result1);
        }
    }
}