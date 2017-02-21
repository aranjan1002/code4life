public class SearchInsertPosition {
    public int searchInsert(int[] A, int target) {
        int idx = 0;
        
        while (idx <= A.length - 1 && A[idx] < target) {
            idx++;
        }
        
        return idx;
    }
}