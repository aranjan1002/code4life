/* The isBadVersion API is defined in the parent class VersionControl.
      boolean isBadVersion(int version); */

public class FirstBadVersion extends VersionControl {
    public int firstBadVersion(int n) {
        long low = 1;
        long high = (long) n;
        while (low < high) {
            long mid = (low + high) / 2;
            if (isBadVersion((int) mid) == false) {
                low = mid + 1;
            } else {
                high = mid;
            }
            System.out.println(low + " " + mid + " " + high);
        }
        return (int) low;
    }
}