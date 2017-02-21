public class RemoveDuplicatesfromSortedArray {
    public int removeDuplicates(int[] nums) {
        int idx1 = 0; 
        int idx2 = 0;
        
        while (idx2 <= nums.length - 1) {
            while (idx2 <= nums.length - 2 && nums[idx2] == nums[idx2 + 1]) {
                idx2++;
            }
            nums[idx1++] = nums[idx2];
            idx2++;
        }
        return idx1;
    }
}