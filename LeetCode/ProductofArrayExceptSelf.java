public class ProductofArrayExceptSelf {
    public int[] productExceptSelf(int[] nums) {
        if (nums.length == 0) return new int[0];
        if (nums.length == 1) return new int[]{1};
        int[] prod_left = new int[nums.length];
        int[] prod_right = new int[nums.length];
        prod_left[0] = nums[0];
        prod_right[nums.length - 1] = nums[nums.length - 1];
        
        for (int i = 1; i <= nums.length - 1; i++) {
            prod_left[i] = prod_left[i - 1] * nums[i];
        }
        for (int i = nums.length - 2; i >= 0; i--) {
            prod_right[i] = prod_right[i + 1] * nums[i];
        }
        int[] result = new int[nums.length];
        result[0] = prod_right[1];
        result[nums.length - 1] = prod_left[nums.length - 2];
        for (int i = 1; i <= nums.length - 2; i++) {
            result[i] = prod_left[i - 1] * prod_right[i + 1];
        }
        return result;
    }
}