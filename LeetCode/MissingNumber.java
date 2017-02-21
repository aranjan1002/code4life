public class MissingNumber {
    public int missingNumber(int[] nums) {
        int length = nums.length;
        int sum = (length * (length + 1)) / 2;
        int sum_nums = 0;
        for (int i = 0; i <= nums.length - 1; i++) {
            sum_nums += nums[i];
        }
        
        return sum - sum_nums;
    }
}