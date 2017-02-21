public class RotateArray {
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        int[] temp = new int[n];
        
        for (int i = 0; i <= n - 1; i++) {
            temp[(i + k) % n] = nums[i];
        }
        
        for (int i = 0; i <= n - 1; i++) {
            nums[i] = temp[i];
        }
    }
}