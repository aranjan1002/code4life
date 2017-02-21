public class MaximumSubarray {
    public int maxSubArray(int[] A) {
        int max_sum, curr_sum;
        
        if (A.length == 0) {
            return 0;
        }
        
        max_sum = A[0];
        curr_sum = A[0];
        
        for (int i = 1; i <= A.length - 1; i++) {
            if (curr_sum < 0 || curr_sum + A[i] < 0) {
                curr_sum = A[i];
            } else {
                curr_sum = curr_sum + A[i];
            }
             
            if (curr_sum > max_sum) {
                max_sum = curr_sum;
            }
         }
        
        return max_sum;
    }
}