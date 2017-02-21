public class SortColors {
    public void sortColors(int[] A) {
        int count0 = 0, count1 = 0, count2 = 0;
        
        for (int i = 0; i <= A.length - 1; i++) {
            if (A[i] == 0) {
                count0++;
            } else if (A[i] == 1) {
                count1++;
            } else {
                count2++;
            }
        }
        
        fillColors(A, count0, 0, 0);
        fillColors(A, count1, 1, count0);
        fillColors(A, count2, 2, count0 + count1);
    }
    
    void fillColors(int[] A, int count, int val, int idx) {
        for (int i = idx; i <= idx + count - 1; i++) {
            A[i] = val;
        }
    }
}