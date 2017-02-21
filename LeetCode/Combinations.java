public class Combinations {
    public List<List<Integer>> combine(int n, int k) {
        return combine(1, n, k);
    }
    
    public List<List<Integer>> combine(int i, int n, int k) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (k > n - i + 1 || k < 0 || i > n) {
            return result;
        }
        
        if (k == 1) {
            result.addAll(getItoNList(i, n));
            return result;
        } else {
            List<List<Integer>> temp_result = combine(i + 1, n, k - 1);
            addToResult(result, temp_result, i);
        }
        
        result.addAll(combine(i + 1, n, k));
        return result;
    }
    
    public List<List<Integer>> getItoNList(int i, int n) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        
        for (int j = i; j <= n; j++) {
            List<Integer> l = new ArrayList<Integer>();
            l.add(new Integer(j));
            result.add(l);
        }
        
        return result;
    }
    
    public void addToResult(List<List<Integer>> result, List<List<Integer>> temp_result, int num) {
        for (int i = 0; i < temp_result.size(); i++) {
            List<Integer> temp_list = temp_result.get(i);
            temp_list.add(0, new Integer(num));
        }
        
        result.addAll(temp_result);
    }
}