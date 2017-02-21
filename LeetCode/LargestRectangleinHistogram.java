public class LargestRectangleinHistogram {
    public int largestRectangleArea(int[] height) {
        if (height.length == 0) return 0;
        Stack<Integer> s = new Stack<Integer>();
        int result = 0;
        for (int i = 0; i <= height.length - 1; i++) {
            if (s.isEmpty() == true || height[s.peek()] < height[i]) {
                s.push(i);
            } else {
                while (s.isEmpty() == false && height[s.peek()] >= height[i]) {
                    result = Math.max(result, popAndGetArea(s, i, height));
                }
                s.push(i);
            }
        }
        int top = 0;
        if (s.isEmpty() == false) {
            top = s.peek();
        }
        while (s.isEmpty() == false) {
            result = Math.max(popAndGetArea(s, top + 1, height), result);
        }
        return result;
    }
    
    int popAndGetArea(Stack<Integer> s, int right, int[] height) {
        int top_idx = s.pop();
        int left = -1;
        if (s.isEmpty() == false) {
            left = s.peek();
        }
        return height[top_idx] * (right - left - 1);
    }
}