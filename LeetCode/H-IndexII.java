public class H-IndexII {
    public int hIndex(int[] citations) {
        for (int i = 1; i <= citations.length; i++) {
            int c = citations[citations.length - i];
            if (c < i) {
                return i - 1;
            }
        }
        return citations.length;
    }
}