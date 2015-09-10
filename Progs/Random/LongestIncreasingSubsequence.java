public class LongestIncreasingSubsequence {
    public int[] LIS(int[] arr) {
        int[] dp = new int[arr.length];
        
        if (arr.length == 0) {
            return new int[0];
        }
        
        dp[0] = 1;
        
        int maxLength = 1;
        int maxIdx = 0;
        for (int i = 1; i <= arr.length - 1; i++) {
            int prevIdx = findPrev(arr, dp, i);
	    // System.out.println(prevIdx);
            if (prevIdx >= 0) {
                dp[i] = dp[prevIdx] + 1;
            } else {
                dp[i] = 1;
            }
	    // System.out.println(dp[i]);
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                maxIdx = i;
            }
        }
        
        int[] result = new int[maxLength];
        for (int i = maxLength - 1; i >= 0; i--) {
	    //System.out.println(maxLength);
            result[i] = arr[maxIdx];
            maxIdx = findPrev(arr, dp, maxIdx);
        }
        
        return result;
    }
    
    int findPrev(int[] arr, int[] dp, int idx) {
        int val = arr[idx];
	int max = 0, max_idx = -1;
        for (int i = idx - 1; i >= 0; i--) {
            if (arr[i] < val) {
		if (dp[i] > max) {
		    max = dp[i];
		    max_idx = i;
		}
            }
        }
        
        return max_idx;
    }

    public static void main(String[] args) {
	int[] arr = {1, 2, 4, 5, 1, 4 ,5, 23, 2, 2, 2342, 2};
	int[] result = new LongestIncreasingSubsequence().LIS(arr);
	for (int i = 0; i <= result.length - 1; i++) {
	    System.out.println(result[i]);
	}
    }
}