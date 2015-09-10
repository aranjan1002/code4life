import java.util.*;
import java.lang.*;

public class Solution {
    public static void main(String[] args) {
	int[] n = {1, 2, 3, 5, 6};
	int target = 11;

	new Solution().twoSum(n, target);
    }

    public int[] twoSum(int[] numbers, int target) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        int[] result = new int[2];
        
        for (int i = 0; i <= numbers.length - 1; i++) {
            Integer n = new Integer(numbers[i]);
            map.put(n, new Integer(i));;
        }
        
        for (int i = 0; i <= numbers.length - 1; i++) {
            Integer n = new Integer(target - numbers[i]);
            if (map.containsKey(n)) {
		//System.out.println(i);
                result[0] = i + 1;
                result[1] = map.get(n).intValue() + 1;
		break;
            }
        }
	
	System.out.println(result[0] + " " + result[1]);
        
        return result;
    }

    public int[] twoSum2(int[] numbers, int target) {
        int head = 0;
        int tail = numbers.length - 1;
        int[] result = new int[2];
        sort(numbers);
        
        while (head < tail) {
            int sum = numbers[head] + numbers[tail];
            if (sum == target) {
                result[0] = head;
                result[1] = tail;
                break;
            } else if (sum < target) {
                head++;
            } else {
                tail--;
            }
        }
        
	System.out.println(numbers[result[0]] + " " + numbers[result[1]]);
        return result;
    }
    
    void sort(int[] numbers) {
        for (int i = 0; i <= numbers.length - 1; i++) {
            for (int j = i + 1; j <= numbers.length - 1; j++) {
                if (numbers[i] > numbers[j]) {
                    int temp = numbers[i];
                    numbers[i] = numbers[j];
                    numbers[j] = temp;
                }
            }
        }
    }
}