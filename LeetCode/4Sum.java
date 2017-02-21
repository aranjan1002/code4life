public class 4Sum {
    public List<List<Integer>> fourSum(int[] num, int target) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        int[] sorted_num = sort(num);
        
        int i = 0;
        List <Integer> prefix = new ArrayList<Integer>();
        while (i <= sorted_num.length - 4) {
            int new_target = target - num[i];
            prefix.clear();
            prefix.add(new Integer(num[i]));
            List<List<Integer>> result_curr = threeSum(num, new_target, i + 1, prefix);
            result.addAll(result_curr);
            int temp = sorted_num[i];
            while(i <= sorted_num.length - 4 && sorted_num[i] == temp) {
                i++;
            }
        }
        return result;
    }
    
    public List<List<Integer>> threeSum(int[] num, int target, int head, List<Integer> prefix) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        
        int i = head;
        while (i <= num.length - 3) {
            if (prefix.size() > 1) {
                prefix.remove(1);
            }
            prefix.add(num[i]);
            int new_target = target - num[i];
            List<List<Integer>> result_curr = twoSum(num, new_target, i + 1, prefix);
            result.addAll(result_curr);
            int temp = num[i];
            while(i <= num.length - 3 && num[i] == temp) {
                i++;
            }
        }
        return result;
    }
    
    public List<List<Integer>> twoSum(int[] num, int target, int head, List<Integer> prefix) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        int tail = num.length - 1;
        int temp;
        
        List<Integer> curr_result;
        while (head < tail) {
            int sum = num[head] + num[tail];
            if (sum == target) {
                curr_result = new ArrayList<Integer>();
                curr_result.addAll(prefix);
                curr_result.add(new Integer(num[head]));
                curr_result.add(new Integer(num[tail]));
                result.add(curr_result);
                temp = num[head];
                while (head <= num.length - 1 && temp == num[head]) {
                    head++;
                }
                temp = tail;
                while (tail >= 0 &&  temp == num[tail]) {
                    tail--;
                }
            } else if (sum < target) {
                temp = num[head];
                while (head <= num.length - 1 && temp == num[head]) {
                    head++;
                }
            } else {
                temp = num[tail];
                while (tail >= 0 && temp == num[tail]) {
                    tail--;
                }
            }
        }
        
        return result;
    }
    
    int[] sort(int[] num) {
        int cmp = 0;
        int temp = 0;
        for (int i = 0; i < num.length; i++) {
            for (int j = i + 1; j < num.length; j++) {
                if (num[i] > num[j]) {
                    temp = num[i];
                    num[i] = num[j];
                    num[j] = temp;
                }
            }
        }
        return num;
    }
}