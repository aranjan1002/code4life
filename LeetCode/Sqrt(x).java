public class Sqrt(x) {
    public int sqrt(int x) {
        int left = 1;
        int right = x;
        int mid = 0, res = 0;
        
        if (x == 1 || x == 0) {
            return x;
        }
        
        while (true) {
            mid = (left + right) / 2;
            int a = x / mid;
            int b = x / (mid + 1);
            
            if (mid <= a && mid + 1 > b) {
                return mid;
            }
            if (mid <= a) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
}