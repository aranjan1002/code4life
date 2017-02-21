public class UniqueBinarySearchTrees {
    public int numTrees(int n) {
        int result = 0;
        for (int i = 1; i <= n; i++) {
            result += numTreeWithRoot(1, i, n);
        }
        
        return result;
    }
    
    public int numTreeWithRoot(int low, int i, int high) {
        int result1 = 0, result2 = 0;
        if (low == i && i == high) {
            return 1;
        }
        else {
            for (int k = low; k <= i - 1; k++) {
                result1 += numTreeWithRoot(low, k, i - 1);
            } 
            for (int k = i + 1; k <= high; k++) {
                result2 += numTreeWithRoot(i + 1, k, high);
            }
        }
        
        return Math.max(result1, 1) * Math.max(result2, 1);
    }
}