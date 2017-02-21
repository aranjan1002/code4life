public class SummaryRanges {
    public List<String> summaryRanges(int[] nums) {
        List<String> result = new ArrayList<String>();
        
        if (nums.length == 0) {
            return result;
        }
        
        int idx = 0;
        while (idx <= nums.length - 1) {
            int start = nums[idx];
            while (idx <= nums.length - 2 && nums[idx + 1] == nums[idx] + 1) {
                idx++;
            }
            
            if (nums[idx] == start) {
                result.add(Integer.toString(start));
            } else {
                result.add(Integer.toString(start) + "->" + Integer.toString(nums[idx]));
            }
            idx++;
        }
        
        return result;
    }
}