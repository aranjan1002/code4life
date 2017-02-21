public class MaximumGap {
    public int maximumGap(int[] nums) {
        if (nums.length <= 1) {
            return 0;
        }
        int min = nums[0];
        int max = nums[0];
        for (int n : nums) {
            min = n < min ? n : min;
            max = max < n ? n : max;
        }
        int gap = (int) Math.ceil(((double) (max - min) / (nums.length - 1)));
        int[] min_bucket = new int[nums.length - 1];
        int[] max_bucket = new int[nums.length - 1];
        Arrays.fill(min_bucket, Integer.MAX_VALUE);
        Arrays.fill(max_bucket, Integer.MIN_VALUE);
        //System.out.println(min + " " + gap);
        for (int n : nums) {
            if (n == min || n == max) continue;
            int idx = (n - min) / gap;
            min_bucket[idx] = Math.min(n, min_bucket[idx]);
            max_bucket[idx] = Math.max(n, max_bucket[idx]);
        }
        int result = 0;
        int prev = min;
        for (int i = 0; i <= nums.length- 2; i++) {
            if (min_bucket[i] != Integer.MAX_VALUE && min_bucket[i] - prev > result) {
                result = min_bucket[i] - prev;
            }
            if (max_bucket[i] != Integer.MIN_VALUE) {
                prev = max_bucket[i];
            }
        }
        return Math.max(result, max - prev);
        
    }
}