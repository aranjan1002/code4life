public class CombinationSumIII {
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (n > 45) {
            return result;
        }
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 9; i >= 1; i--) {
            list.add(i);
        }
        return combination3(k, n, list, 0);
    }
    
    List<List<Integer>> combination3(int k, int n, List<Integer> list, int idx) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (k == 0 && n == 0 && idx == list.size()) {
            result.add(new ArrayList<Integer>());
            return result;
        } else if (idx == list.size()) {
            return result;
        }
        List<List<Integer>> result1 = combination3(k - 1, n - list.get(idx), list, idx + 1);
        List<List<Integer>> result2 = combination3(k, n, list, idx + 1);
        for (List<Integer> l : result1) {
            l.add(list.get(idx));
        }
        result.addAll(result1);
        result.addAll(result2);
        
        return result;
    }
}