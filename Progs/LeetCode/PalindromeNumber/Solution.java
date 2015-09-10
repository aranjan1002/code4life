public class Solution {
    public static void main(String[] args) {
	System.out.println(new Solution().isPalindrome(1001));
    }

    public boolean isPalindrome(int x) {
        double x_double = (double) Math.abs((double) x);
        int n = countDigits(x_double);
	// System.out.println(n);
	
        while (n >= 2) {
            double last_digit = x_double % 10;
            double first_digit = x_double / Math.pow(10, n - 1);
	    System.out.println(first_digit + " " + last_digit + " " + n);
            if ((int) last_digit != (int) first_digit) {
                return false;
            }
            x_double = x_double % Math.pow(10, n - 1);
            x_double = (int) x_double / 10;
            n = n - 2;
        }
        
        return true;
    }
    
    int countDigits(double x) {
        int cnt = 0;
        while ((int) x > 0) {
            x = x / 10;
            cnt++;
        }
        
        return cnt;
    }
}