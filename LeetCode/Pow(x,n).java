public class Pow(x,n) {
    public double pow(double x, int n) {
        if (n == 0) {
            return 1;
        } 
        if (n == 1) {
            return x;
        }
        if (n == 2) {
            return x * x;
        }
        if (n < 0) {
            n = n * -1;
            x = 1 / x;
        }
        
        double result = Math.abs(pow(pow(x, n/2), 2) * pow(x, n % 2));
        if (x < 0) {
            if (n % 2 == 1) {
                result = result * -1;
            }
        }
        
        return result;
    }
}