public class Solution3 {
    public static void main(String[] args) {
	int[] height = {15000,14999,14998,14997,14996,14995,14994,14993,14992};
	System.out.println(new Solution3().maxArea(height));
    }

    public int maxArea(int[] height) {
        int i = 0;
	int j = height.length - 1;
	int max_area = 0;

	while (i < j) {
	    int curr_area = calc_area(height[i], i, height[j], j);
	    if (curr_area > max_area) {
		max_area = curr_area;
	    }
	    if (height[i] < height[j]) {
		i++;
	    } else if (height[i] > height[j]) {
		j--;
	    } else {
		i++;
		j--;
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
    
    int maxArea = 0;
}