public class MinimumSizeSubarraySum {
    public int minSubArrayLen(int s, int[] nums) {
        if (nums.length == 0 || s == 0) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        int ptr1 = 0;
        int ptr2 = 0;
        int curr_sum = 0;
        while (ptr2 <= nums.length - 1) {
            curr_sum += nums[ptr2];
            if (curr_sum >= s) {
                while (curr_sum - nums[ptr1] >= s && ptr1 < ptr2) {
                    curr_sum = curr_sum - nums[ptr1++];
                };
                if (ptr2 - ptr1 + 1 < min) {
                    min = ptr2 - ptr1 + 1;
                }
            }
            ptr2++;
        }
        if (min == Integer.MAX_VALUE) {
            return 0;
        }
        return min;
    }
}