import java.lang.*;
import java.util.*;

public class ThreeSum {
    public static void main(String[] args) {
	int[] S = {0,7,-4,-7,0,14,-6,-4,-12,11,4,9,7,4,-10,8,10,5,4,14,6,0,-9,5,6,6,-11,1,-8,-1,2,-1,13,5,-1,-2,4,9,9,-1,-3,-1,-7,11,10,-2,-4,5,10,-15,-4,-6,-8,2,14,13,-7,11,-9,-8,-13,0,-1,-15,-10,13,-2,1,-1,-15,7,3,-9,7,-1,-14,-10,2,6,8,-6,-12,-13,1,-3,8,-9,-2,4,-2,-3,6,5,11,6,11,10,12,-11,-14};
	System.out.println(new ThreeSum().threeSum(S));
    }

    public List<List<Integer>> threeSum(int[] num) {
        sort(num);
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        for (int i = 0; i <= num.length - 1; i++) {
            threeSum(num, (-1) * num[i], i, result);
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
    
    public void threeSum(int[] num, int target, int exclude_idx, List<List<Integer>> result) {
        int len = num.length;
        int low = 0, high = num.length - 1;
        Integer tar = new Integer((-1) * target);
        
        while (low < high) {
            if (low == exclude_idx) {
                low++;
            } else if (high == exclude_idx) {
                high--;
            } else {
                int sum = num[low] + num[high];
                if (sum == target) {
                    List<Integer> curr_result = new ArrayList<Integer>();
                    if (exclude_idx < low) {
                        curr_result.add(tar);
                    }
                    curr_result.add(new Integer(num[low]));
                    if (exclude_idx > low && exclude_idx < high) {
                        curr_result.add(tar);
                    }
                    curr_result.add(new Integer(num[high]));
                    if (exclude_idx > high) {
                        curr_result.add(tar);
                    }
                    low++;
                    high--;
                    if (result.contains(curr_result) == false) {
                        result.add(curr_result);
                    }
                } else if (sum < target) {
                    low++;
                } else {
                    high--;
                }
            }
        }
    }
}