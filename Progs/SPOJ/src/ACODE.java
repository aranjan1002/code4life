import java.util.Scanner;

/**
 * Created by Anshu on 2/19/2017.
 */
public class ACODE {
    public static void main(String[] args) {
        new ACODE().solve();
    }

    public void solve() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String str = sc.next();
            if ("0".equals(str)) return;
            long[] dp = new long[str.length() + 1];
            dp[0] = 1;
            dp[1] = 1;
            for (int i = 2; i < dp.length; i++) {
                char prev_c = str.charAt(i - 2);
                char c = str.charAt(i - 1);
                //System.out.println("C:" + c);
                dp[i] = dp[i - 1];
                if (c != '0') {
                    if (prev_c == '1' || (prev_c == '2' && c < '7')) {
                        if (i == dp.length - 1 || (str.charAt(i) != '0'))
                            dp[i] += dp[i - 2];
                    }
                }
                //System.out.println(dp[i]);
            }
            System.out.println(dp[str.length()]);
        }
    }
}
