import java.util.*;
import java.lang.*;

public class Solution {
    public static void main(String[] args) {
	int[] num = {1, 2};
	System.out.println(new Solution().subsetsWithDup(num));
    }

    public List<List<Integer>> subsetsWithDup(int[] num) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> empty_set = new ArrayList<Integer>();
        result.add(empty_set);
        
        for (int i = 0; i <= num.length - 1;) {
	    System.out.println(num.length);
            int n = num[i];
	    System.out.println(n);
            int count = 0;
            while (count + i <= num.length - 1 && num[count + i] == num[i]) {
                count++;
            }
	    System.out.println(count);
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
	    System.out.println(i);
        }
        
        return result;
    }
}