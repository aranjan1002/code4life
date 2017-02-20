import java.util.Scanner;

/**
 * Created by Anshu on 2/19/2017.
 */
public class GSS1 {
    public static void main(String[] args) {
        new GSS1().solve();
    }

    public void solve() {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] nums = new int[N];
        for (int i = 0; i < N; i++) nums[i] = sc.nextInt();
        int M = sc.nextInt();
        while (M-- > 0) {
            int result = 0, curr_result = 0;
            int x = sc.nextInt();
            int y = sc.nextInt();

            for (int i = x - 1; i <= y - 1; i++) {
                if (nums[i] + curr_result >= 0) curr_result += nums[i];
                result = Math.max(result, curr_result);
            }
            System.out.println(result);
        }
    }
}
