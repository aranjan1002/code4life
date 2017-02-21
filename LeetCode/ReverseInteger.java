public class ReverseInteger {
    public int reverse(int x) {
        long result = 0;
        boolean isMinus = false;
        long a = (long) x;
        
        if (a < 0) {
            isMinus = true;
        }
        
        while (a > 0 || a < 0) {
            result = (result * 10) + (a % 10);
            a = a / 10;
        }
        
        //if (isMinus == true) {
        //    result = result * (-1);
        //}
        
        if (result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
            return (int) result;
        } else {
            return 0;
        }
        
    }
}