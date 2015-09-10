import java.lang.*;

public class Solution {
    public static void main(String[] args) {
	System.out.println(Integer.MAX_VALUE);
	System.out.println(new Solution().trailingZeroes3(2147483647));
    }
    public int trailingZeroes(int n) {
        int count5 = n / 5;
        int cnt = 1;
        for (int i = 25; i <= n; i = i * 5) {
            count5 += cnt;
            cnt++;
	    System.out.println(i);
        }
        
        return count5;
    }

    public int trailingZeroes2(int n) {
        int count5 = n / 5;
        int cnt = 1;
        for (int i = n; i >= 25; i = i / 5) {
            count5 += cnt;
            cnt++;
            System.out.println(i);
        }

        return count5;
    }

    public int trailingZeroes3(int n) {
        int count5 = n / 5;
        int cnt = 1;
        for (long i = 5; i <= n; i = i + 5) {
	    System.out.println(i);
            long temp = i;
            while (temp >= 5) {
                temp = temp / 5;
                count5++;
            }
        }
        
        return count5;
    }
}