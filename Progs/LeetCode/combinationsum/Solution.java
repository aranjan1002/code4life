import java.lang.*;
import java.util.*;

public class Solution {
    public static void main(String[] args) {
	int[] candidates = {2, 3, 6, 7};
	int target = 7;
	System.out.println(new Solution().combinationSum(candidates, target));
    }
    
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        int[] sorted_unique_candidates = sort_unique(candidates);
        System.out.println(sorted_unique_candidates[2]);
        return(combinationSum2(sorted_unique_candidates, target, 0));
	//return null;
    }
    
    int[] sort_unique(int[] num) {
        for (int i = 0; i < num.length; i++) {
            for (int j = i + 1;  j < num.length; j++) {
                if (num[i] > num[j]) {
                    int temp = num[i];
                    num[i] = num[j];
                    num[j] = temp;
                }
            }
        }
	int head = 0;
	int tail = 1;
	while (tail <= num.length - 1) {
	    if (num[head] == num[tail]) {
		tail++;
	    } else {
		head++;
		num[head] = num[tail];
		tail++;
	    }
	}
    
	int[] result = new int[head + 1];
	for (int i = 0; i <= head; i++) {
	    result[i] = num[i];
	}
    
	return result;
    }
    
    List<List<Integer>> combinationSum2(int[] sorted_candidates, int target, 
					int idx) {
        Integer t = new Integer(target);
	System.out.println(target);

        if (mem.containsKey(t) == true) {
            return mem.get(t);
        }
        
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        
        for (int i = idx; i <= sorted_candidates.length - 1; i++) {
	    // System.out.println(sorted_candidates[i] + " " + target);
            if (sorted_candidates[i] == target) {
                List<Integer> l = new ArrayList<Integer>();
                l.add(t);
                result.add(l);
                return result;
            } else if (sorted_candidates[i] < target) {
		// System.out.println(sorted_candidates[i] + " " + target);
                Integer diff = new Integer(target - sorted_candidates[i]);
                List<List<Integer>> temp_result = 
		    combinationSum2(sorted_candidates, 
				    target - sorted_candidates[i], i);
		System.out.println("Result for " + diff + temp_result);
                addToResult(result, temp_result, new Integer(sorted_candidates[i]));
            }
        }
        
        mem.put(t, result);
        return result;
    }
    
    void addToResult(List<List<Integer>> result, List<List<Integer>> temp_result, 
		     Integer diff) {
        for (int i = 0; i < temp_result.size(); i++) {
            List<Integer> temp_list = temp_result.get(i);
            temp_list.add(0, diff);
        }
        
        result.addAll(temp_result);
    }
    
    Map<Integer, List<List<Integer>>> mem = new HashMap<Integer, List<List<Integer>>>();
}