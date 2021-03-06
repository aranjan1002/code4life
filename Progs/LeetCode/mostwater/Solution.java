public class Solution {
    public static void main(String[] args) {
	int[] h = {3, 7, 7};
	System.out.println(new Solution().maxArea(h));
    }

    public int maxArea(int[] height) {
        return maxArea2(height, 0, height.length - 1);
    }
    
    public int maxArea2(int[] height, int i, int j) {
        if (i >= j) {
            return 0;
        }
        return (max(maxArea2(height, i + 1, j), maxArea3(height, i, j)));
    }
    
    int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
    }
    
    public int maxArea3(int[] height, int i, int j) {
        int max_area = 0;
        for (int k = i + 1; k <= j; k++) {
            int temp_area = calc_area(height[i], i, height[j], j);
            if (temp_area > max_area) {
                max_area = temp_area;
            }
        }
        return max_area;
    }
    
    int calc_area(int h1, int x1, int h2, int x2) {
        int h = min(h1, h2);
        int w = x2 - x1;
        
        return (h * w);
    }
    
    int min(int a, int b) {
        if (a < b) {
            return a;
        }
        return b;
    }
}