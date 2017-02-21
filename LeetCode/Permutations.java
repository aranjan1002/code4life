public class Permutations {
    public List<List<Integer>> permute(int[] nums) {
        return permute(nums, 0);
    }
    
    List<List<Integer>> permute(int[] nums, int idx) {
        List<List<Integer>> list = new ArrayList<List<Integer>>();
        if (idx == nums.length) {
            list.add(new ArrayList<Integer>());
            return list;
        }
        List<List<Integer>> list2 = permute(nums, idx + 1);
        Integer val = new Integer(nums[idx]);
        for (List<Integer> l : list2) {
            for (int i = 0; i <= l.size(); i++) {
                List<Integer> new_l = copy(l);
                new_l.add(i, val);
                list.add(new_l);
            }
        }
        return list;
    }
    
    List<Integer> copy(List<Integer> list) {
        List<Integer> copy_list = new ArrayList<Integer>();
        for (Integer i : list) {
            copy_list.add(i);
        }
        return copy_list;
    }
}