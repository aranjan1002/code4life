public class MaximalRectangle {
    public int maximalRectangle(char[][] matrix) {
        int result = 0;
        if (matrix.length == 0 || matrix[0].length == 0) return 0;
        int row = matrix.length;
        int col = matrix[0].length;
        int[] hist = new int[col];
        
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j <= col - 1; j++) {
                if (matrix[i][j] == '1') {
                    hist[j]++;
                } else {
                    hist[j] = 0;
                }
               // System.out.println(j + ": " + hist[j]);
            }
            result = Math.max(result, maxRectInHistogram(hist));
        }
        return result;
    }
    
    public int maxRectInHistogram(int[] nums) {
        if (nums.length == 0) return 0;
        Stack<Integer> s = new Stack<Integer>();
        int result = 0;
        
        for (int i = 0; i <= nums.length - 1; i++) {
            if (s.isEmpty() == true || nums[s.peek()] <= nums[i]) {
                s.push(i);
            } else {
                while (s.isEmpty() == false && nums[s.peek()] > nums[i]) {
                    result = Math.max(popAndGetArea(nums, s, i - 1), result);
                }
                s.push(i);
            }
        }
        
        while (s.isEmpty() == false) {
            result = Math.max(popAndGetArea(nums, s, s.peek()), result);
        }
        return result;
    }
    
    public int popAndGetArea(int[] nums, Stack<Integer> s, int right) {
        int top_idx = s.pop();
        int left = top_idx - 1;
        if (s.isEmpty() == false) {
            int cnt = 0;
            Stack<Integer> s2 = new Stack<Integer>();
            while (s.isEmpty() == false && nums[s.peek()] == nums[top_idx]) {
                s2.push(s.pop());
                cnt++;
            }
            if (s.isEmpty() == true) {
                left = -1;
            } else {
                left = s.peek();
            }
            while (s2.isEmpty() == false) {
                s.push(s2.pop());
            }
        }
        int top_idx_area = nums[top_idx] * (right - left);
        return top_idx_area;
    }
}