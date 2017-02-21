public class ContainsDuplicate {
    public boolean containsDuplicate(int[] nums) {
        if (nums.length == 0) {
            return false;
        }
        HashSet<Integer> set = new HashSet<Integer>();
        
        for (int i = 0; i <= nums.length - 1; i++) {
            if (set.contains(nums[i]) == true) {
                return true;
            }
            set.add(nums[i]);
        }
        return false;
    }
}