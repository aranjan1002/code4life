public class MergeSortedArray {
    public void merge(int A[], int m, int B[], int n) {
        int[] C = new int[m + n];
        
        int a_idx = 0;
        int b_idx = 0;
        for (int i = 0; i <= m + n - 1; i++) {
            if (a_idx <= m - 1 && b_idx <= n -1) {
                if (A[a_idx] < B[b_idx]) {
                    C[i] = A[a_idx++];
                } else if (B[b_idx] < A[a_idx]) {
                    C[i] = B[b_idx++];
                } else {
                    C[i++] = A[a_idx];
                    C[i] = A[a_idx];
                    a_idx++;
                    b_idx++;
                }
            } else {
                while (a_idx <= m - 1) {
                    C[i++] = A[a_idx++];
                }
                while (b_idx <= n - 1) {
                    C[i++] = B[b_idx++];
                }
                break;
            }
        }
        
        for (int i = 0; i <= m + n - 1; i++) {
            A[i] = C[i];
        }
    }
}