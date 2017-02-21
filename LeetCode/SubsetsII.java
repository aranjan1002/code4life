public class SubsetsII {
    public List<List<Integer>> subsetsWithDup(int[] num) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> empty_set = new ArrayList<Integer>();
        result.add(empty_set);
        sort(num);
        
        for (int i = 0; i <= num.length - 1;) {
            int n = num[i];
            int count = 0;
            while (count + i <= num.length - 1 && num[count + i] == num[i]) {
                count++;
            }
            List<List<Integer>> temp_result = new ArrayList<List<Integer>>();
            for (int j = 0; j <= result.size() - 1; j++) {
                List<Integer> curr_subset = new ArrayList(result.get(j));
                for (int k = 1; k <= count; k++) {
                    curr_subset.add(new Integer(n));
                    temp_result.add(curr_subset);
                    curr_subset = new ArrayList(curr_subset);
                }
            }
            result.addAll(temp_result);
            i = i + count;
        }
        
        return result;
    }
    
    void sort(int[] num) {
        for (int i = 0; i <= num.length - 1; i++) {
            for (int j = i + 1; j <= num.length - 1; j++) {
                if (num[i] > num[j]) {
                    int temp = num[i];
                    num[i] = num[j];
                    num[j] = temp;
                }
            }
        }
    }
}