public class Solution2 {
    public static void main(String[] args) {
	int[] height = {15000,14999,14998,14997,14996,14995,14994,14993,14992};
	System.out.println(new Solution2().maxArea(height));
    }

    public int maxArea(int[] height) {
        int curr_max_height = 0;
        for (int i = 0; i < height.length; i++) {
            if (height[i] > curr_max_height) {
		System.out.println(height[i] + " " + curr_max_height);
                curr_max_height = height[i];
                for (int j = i + 1; j < height.length; j++) {
                    int curr_area = calc_area(height[i], i, height[j], j);
                    if (curr_area > maxArea) {
                        maxArea = curr_area;
                    }
                }
            }
        }
        
        return maxArea;
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
    
    int maxArea = 0;
}