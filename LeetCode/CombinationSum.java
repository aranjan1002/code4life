public class CombinationSum {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        solve(candidates, 0, target, result, new ArrayList<Integer>());
        return result;
    }
    
    void solve(int[] candidates, int idx, int target, List<List<Integer>> result, List<Integer> curr_list) {
        if (target == 0) {
            result.add(curr_list);
            return;
        }
        if (idx > candidates.length - 1 || target < candidates[idx]) {
            return;
        }
        List<Integer> copy_list = copyAndAdd(curr_list, candidates[idx]);
        solve(candidates, idx, target - candidates[idx], result, copy_list);
        solve(candidates, idx + 1, target, result, curr_list);
    }
    
    List<Integer> copyAndAdd(List<Integer> curr_list, int x) {
        List<Integer> list = new ArrayList<Integer>();
        for (Integer i : curr_list) list.add(i);
        list.add(x);
        return list;
    }
}