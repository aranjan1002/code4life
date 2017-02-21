public class DivideTwoIntegers {
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

    if (result == Integer.MAX_VALUE + 1 && isMinus == false) {
        return Integer.MAX_VALUE;
    }
    
    if (isMinus == true) {
            return result - result - result;
        } else {
            return result;
        }
    }

    double abs(double a) {
        if (a >= 0) {
            return (int) a;
        } else {
            return (int) a - a - a;
        }
    }

}