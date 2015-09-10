public class Solution {
    public static void main(String[] args) {
	System.out.println(new Solution().divide(-1010369383, -2147483648));
	System.out.println(-1010369383 / -2147483648);
    }

    public int divide(int dividend, int divisor) {
	int result = 0;
        boolean isMinus = false;
        if ((dividend < 0 && divisor >= 0) || (dividend >= 0 && divisor < 0)) {
            isMinus = true;
        }
        
	double n = (double) dividend;
	double d = (double) divisor;

        d = abs(d);
        n = abs(n);
        
        while (n >= d) {
            double temp_divisor = d;
            int cnt = 1;
            while (n - temp_divisor >= 0) {
                result += cnt;
		cnt += cnt;
                n -= temp_divisor;
                temp_divisor += temp_divisor;
		// System.out.println(cnt);
            }
            temp_divisor = d;
        }
        
        if (isMinus == true) {
            return result - result - result;
        } else {
            return result;
        }
    }

    double abs(double a) {
        if (a >= 0) {
            return a;
        } else {
            return a - a - a;
        }
    }

    public int dividew(int dividend, int divisor) {
        if (divisor == 0) {
            return Integer.MAX_VALUE;
        } 
        if (dividend == 0) {
            return 0;
        }
        
        int dividend_org = dividend;
        int divisor_org = divisor;
        
        dividend = Math.abs(dividend);
        divisor = Math.abs(divisor);
        
        int cnt = 1;
        int prod = divisor;
        
        while (prod < dividend) {
            prod += prod;
            cnt += cnt;
        }
        while (prod > dividend) {
            prod -= divisor;
            cnt--;
        }
        
        if (dividend_org < 0 && divisor_org > 0) {
            cnt *= -1;
        }
        if (dividend_org > 0 && divisor_org < 0) {
            cnt *= -1;
        }
        
        return cnt;
    }
}