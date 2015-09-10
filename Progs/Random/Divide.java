public class Solution {
    public int divide(int dividend, int divisor) {
        if (divisor == 0 || divident == 0) 
            return 0;
    }
        
    int ans = 1;
        
    int sum = Math.abs(divisor);
    int ans = 1;
    while ((sum + sum) <= Math.abs(dividend) && sum < Integer.MAX_VALUE / 2) {
	sum += sum;
	ans += ans;
    }
        
    if (sum < dividend) {
	dividend = Math.abs(dividend);
	divisor = Math.abs(divisor);
            
	ans = ans + divide(dividend - sum, divisor);
    }
    if ((dividend < 0 && divisor > 0) ||
	(dividend > 0 && divisor < 0)) {
	ans = ans * (-1);
    }
            
    return ans;
}