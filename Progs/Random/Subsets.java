import java.lang.*;
import java.util.*;

public class Subsets {
    List<List<Integer>> subsetsOfLength(int[] n, int len) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        result.add(new ArrayList<Integer>());
        
        for (int i = 0; i <= n.length - 1; i++) {
            addToResult(result, n[i], len);
        }
        
        for (int i = 0; i <= result.size() - 1; i++) {
            List<Integer> curr_list = result.get(i);
            if (curr_list.size() < len) {
                result.remove(i--);
            } else {
		System.out.println(curr_list.size());
	    }
        }
        
        return result;
    }
    
    void addToResult(List<List<Integer>> result, int num, int len) {
	int size = result.size();
        for (int i = 0; i <= size - 1; i++) {
            List<Integer> curr_list = result.get(i);
            if (curr_list.size() < len) {
                List<Integer> new_list = copy(curr_list);
                new_list.add(new Integer(num));
                result.add(new_list);
            }
        }
    }

    List<Integer> copy(List<Integer> list) {
	List<Integer> new_list = new ArrayList<Integer>();
	for (int i = 0; i <= list.size() - 1; i++) {
	    new_list.add(list.get(i));
	}

	return new_list;
    }

    public static void main(String[] args) {
	int[] n = {1, 2, 3, 4, 5};

	System.out.println(new Subsets().subsetsOfLength(n, 3));
    }
}