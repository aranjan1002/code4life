import java.util.Scanner;

/**
 * Created by Anshu on 2/11/2017.
 */
public class INVCNT {
    public static void main(String[] args) {
        new INVCNT().solve();
    }

    public void solve() {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            int c = sc.nextInt();
            long[] arr = new long[c];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = sc.nextLong();
            }
            result = 0;
            solve(arr);
            System.out.println(result);
        }
    }

    public void solve(long[] nums) {
        mergeSortWithCount(nums, 0, nums.length - 1);
    }

    void mergeSortWithCount(long[] nums, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSortWithCount(nums, low, mid);
            mergeSortWithCount(nums, mid + 1, high);
            merge(nums, low, mid, high);
        }
    }

    private void merge(long[] nums, int low, int mid, int high) {
        long[] rs = new long[high - low + 1];
        int rs_idx = 0;
        int idx1 = low, idx2 = mid + 1;

        //System.out.println(idx1 + " " + rs_idx + " " + low + " " + mid + " " + high);
        while (rs_idx < rs.length) {
            if (idx1 <= mid && idx2 <= high) {
                if (nums[idx1] <= nums[idx2]) {
                    //System.out.println("not inc");
                    rs[rs_idx] = nums[idx1++];
                } else {
                    rs[rs_idx] = nums[idx2++];
                    //System.out.println("incrementing " + idx1);
                    result += mid - idx1 + 1;
                }
                rs_idx++;
            } else if (idx1 <= mid) {
                while (idx1 <= mid) {
                    rs[rs_idx++] = nums[idx1++];
                }
            } else {
                while (idx2 <= high) {
                    rs[rs_idx++] = nums[idx2++];
                }
            }
        }
        //System.out.println("rs idx" + rs_idx + " " + rs.length);
        rs_idx = 0;
        for (int i = low; i <= high; i++) nums[i] = rs[rs_idx++];
        //System.out.println(result);
    }

    long result = 0;
}
