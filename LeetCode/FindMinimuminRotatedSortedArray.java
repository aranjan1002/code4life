public class FindMinimuminRotatedSortedArray {
    public int findMin(int[] num) {
        int min = Integer.MAX_VALUE;
        
        for (int i = 0; i <= num.length - 1; i++) {
            if (num[i] < min) {
                min = num[i];
            }
        }
        return min;
    }
}