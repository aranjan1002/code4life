public class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (k == 0) {
            return new int[0];
        }
        int result_length = Math.max(nums.length - k + 1, 0);
        int[] result = new int[result_length];
        if (nums.length == 0) {
            return result;
        }
        LinkedList<Integer> dq = new LinkedList<Integer>();
        int idx = 0;
        int window_idx = 0;
        
        while (idx < result_length) {
            while (dq.isEmpty() == false && dq.peekFirst() < window_idx - k + 1) {
                dq.pollFirst();
            }
            if (dq.isEmpty() == true || nums[dq.peekFirst()] <= nums[window_idx]) {
                dq.push(window_idx);
            } else {
                while (dq.isEmpty() == false && nums[dq.peekLast()] <= nums[window_idx]) {
                    dq.pollLast();
                }
                dq.offer(window_idx);
            }
            if (window_idx >= k - 1) {
                result[idx++] = nums[dq.peekFirst()];
            }
            
            window_idx++;
        }
        
        return result;
    }
}