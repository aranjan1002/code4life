public class H-Index {
    public int hIndex(int[] citations) {
        sort(citations);
        
        for (int i = 1; i <= citations.length; i++) {
            int c = citations[i - 1];
            if (c < i) {
                return i - 1;
            }
        }
        return citations.length;
    }
    
    void sort(int[] citations) {
        int i, j;
        for (i = 1; i <= citations.length - 1; i++) {
            int temp = citations[i];
            for (j = i - 1; j >= 0 && citations[j] < temp; j--) {
                citations[j + 1] = citations[j];
            }
            citations[j + 1] = temp;
        }
    }
}