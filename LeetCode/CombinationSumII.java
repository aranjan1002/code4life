public class CombinationSumII {
    public List<List<Integer>> combinationSum2(int[] num, int target) {
        int[] sorted_num = sort(num);
        return(combinationSum2(sorted_num, target, 0));
	//return null;
    }
    
    int[] sort(int[] num) {
        for (int i = 0; i < num.length; i++) {
            for (int j = i + 1;  j < num.length; j++) {
                if (num[i] > num[j]) {
                    int temp = num[i];
                    num[i] = num[j];
                    num[j] = temp;
                }
            }
        }
        return num;
    }
    
    public List<List<Integer>> combinationSum2(int[] num, int target, int idx) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        
        if (num[idx] == target) {
            List<Integer> l = new ArrayList<Integer>();
            l.add(new Integer(num[idx]));
            result.add(l);
            return result;
        } else if (target > num[idx]) {
            if (idx <= num.length - 2) {
                List<List<Integer>> result2 = combinationSum2(num, target - num[idx], idx + 1);
                addToResult(result, result2, num[idx]);
            }
        }
        
        // increase idx to find next unique number
        while (idx + 1 <= num.length - 1 && num[idx + 1] == num[idx]) {
            idx++;
        }
        
        if (idx <= num.length - 2) {
            result.addAll(combinationSum2(num, target, idx + 1));
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