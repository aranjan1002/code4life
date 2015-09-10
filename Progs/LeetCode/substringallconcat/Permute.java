import java.util.*;
import java.lang.*;

public class Permute {
    public static void main(String[] args) {
	new Permute().solve();
    }

    public void solve() {
	List<Integer> l = new ArrayList<Integer>();
	// l.add(new Integer(1));
	// l.add(new Integer(2));
	// l.add(new Integer(3));
	List<List<Integer>> result = getPermutations(l);
	System.out.println(result);
    }

    public List<List<Integer>> getPermutations(List<Integer> index_list) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (index_list.size() == 0) {
            return result;
        }
        permute(index_list, new ArrayList<List<Integer>>(), result);
        return result;
    }
    
    public void permute(List<Integer> curr_index_list, 
                        List<List<Integer>> permutations_so_far, 
                        List<List<Integer>> result) {
        if (curr_index_list.size() == 1) {
            result.addAll(addToList(permutations_so_far, curr_index_list.get(0)));
        } else {
            for (int i = 0; i <= curr_index_list.size() - 1; i++) {
                List<List<Integer>> new_permutation_list = 
		    addToList(permutations_so_far, curr_index_list.get(i));
                List<Integer> new_index_list = getNewIndexList(curr_index_list, i);
                permute(new_index_list, new_permutation_list, result);
            }
        }
    } 
    
    public List<List<Integer>> addToList(List<List<Integer>> permutations_so_far, 
					 Integer i) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        for (int j = 0; j <= permutations_so_far.size() - 1; j++) {
            List<Integer> permutation = new ArrayList(permutations_so_far.get(j));
            permutation.add(i);
            result.add(permutation);
        }
        
        if (result.size() == 0) {
            List<Integer> new_list = new ArrayList<Integer>();
            new_list.add(i);
            result.add(new_list);
        }

	return result;
    }
    
    public List<Integer> getNewIndexList(List<Integer> index_list, int i) {
        List<Integer> new_list = new ArrayList(index_list);
        new_list.remove(i);
        return new_list;
    }
}