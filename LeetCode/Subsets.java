public class Subsets {
    public List<List<Integer>> subsets(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        result.add(new ArrayList<Integer>());
        for (int n : nums) {
            List<List<Integer>> curr_result = new ArrayList<List<Integer>>();
            for (List<Integer> l : result) {
                List<Integer> new_l = new ArrayList<Integer>();
                for (int v : l) {
                    new_l.add(v);
                }
                new_l.add(n);
                curr_result.add(new_l);
            }
            result.addAll(curr_result);
        }
        return result;
    }
}