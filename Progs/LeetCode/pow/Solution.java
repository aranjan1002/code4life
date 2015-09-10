import java.lang.*;
import java.util.*;

public class Solution {
    public static void main(String[] args) {
	// System.out.println(new Solution().pow(.00001, 2147483647));
	System.out.println(Math.pow(2, -2));
    }

    public double pow(double x, int n) {
        double result = 1;
        if (n == 0) {
            return 1;
        } else {
            for (int i = 1; i <= n; i = i + 10000) {
		System.out.println(Integer.MAX_VALUE);
                result = result * x;
            }
        }
        
        return result;
    }
}