public class IncreasingTripletSubsequence {
    public boolean increasingTriplet(int[] nums) {
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
        
        for (int n : nums) {
            if (n <= min1) min1 = n;
            else if (n <= min2) min2 = n;
            else return true;
        }
        return false;
    }
}