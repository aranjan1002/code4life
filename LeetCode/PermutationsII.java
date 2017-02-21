public class PermutationsII {
    public List<List<Integer>> permuteUnique(int[] nums) {
        //Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> new_result = new ArrayList<Integer>();
        result.add(new_result);
        for (int n : nums) {
            List<List<Integer>> curr_result = new ArrayList<List<Integer>>();
            for (List<Integer> l : result) {
                for (int i = 0; i <= l.size(); i++) {
                    List<Integer> new_l = new ArrayList<Integer>();
                    for (Integer it : l) {
                        new_l.add(it);
                    }
                    new_l.add(i, n);
                    //System.out.println(new_l);
                    if (curr_result.contains(new_l) == false) {
                        //System.out.println("adding" + new_l);
                        curr_result.add(new_l);
                    }
                }
            }
            result = curr_result;
        }
        return result;
    }
}