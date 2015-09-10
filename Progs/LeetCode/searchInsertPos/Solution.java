public class Solution {
    public static void main(String[] args) {
	int[] A = {1,4,6,7,8,9};
	System.out.println(new Solution().searchInsert(A, 6));
    }

    public int searchInsert(int[] A, int target) {
        if (A.length == 0) {
            return 0;
        }
        int idx = find(A, target, 0, A.length - 1);
        
	
        return idx;
    }
    
    int find(int[] A, int target, int head, int tail) {
        if (head < tail) {
	    System.out.println(head + " " + tail + " ");
            int mid = (head + tail) / 2;
            if (A[mid] == target) {
                return mid;
            } else if (A[mid] <= target) {
                return find(A, target, mid + 1, tail);
            } else {
                return find(A, target, head, mid - 1);
            }
        } else {
            return head;
        }
    }
}