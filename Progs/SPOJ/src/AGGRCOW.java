import java.util.Scanner;

/**
 * Created by Anshu on 2/11/2017.
 */
public class AGGRCOW {
    public static void main(String[] args) {
        new AGGRCOW().solve();
    }

    public void solve() {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            int m = sc.nextInt();
            int n = sc.nextInt();
            for (int i = Math.max(2, m); i <= n; i++) {
                if (isPrime(i)) {
                    System.out.println(i);
                }
            }
            System.out.println();
        }
    }

    public boolean isPrime(int num) {
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}
