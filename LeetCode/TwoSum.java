public class TwoSum {
    public int[] twoSum(int[] numbers, int target) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        int[] result = new int[2];
        
        for (int i = 0; i <= numbers.length - 1; i++) {
            Integer n = new Integer(numbers[i]);
            Integer diff_n = new Integer(target - numbers[i]);
            if (map.containsKey(diff_n)) {
                result[0] = map.get(diff_n).intValue() + 1;
                result[1] = i + 1;
                break;
            }
            map.put(n, new Integer(i));
        }
        
        return result;
    }
}