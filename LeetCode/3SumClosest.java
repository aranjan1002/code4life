public class 3SumClosest {
     public int threeSumClosest(int[] num, int target) {
        num = sort(num);
        int result = Integer.MAX_VALUE;
        int result_sum = 0;

        for (int i = 0; i < num.length - 2; i++) {
            int new_target = target - num[i];
            int curr_closest_2sum = twoSumClosest(num, i + 1, new_target);
            int curr_closest = Math.abs(new_target - curr_closest_2sum);
            //System.out.println(curr_closest_2sum);
            if (curr_closest < result) {
                result = curr_closest;
                result_sum = curr_closest_2sum + num[i];
            }
            if (result == 0) {
                return target;
            }
        }
        //System.out.println(target);
        return result_sum;
    }

    
    public int[] sort(int[] num) {
        for (int i = 0; i < num.length; i++) {
            for (int j = i + 1; j < num.length; j++) {
                if (num[i] > num[j]) {
                    int temp = num[i];
                    num[i] = num[j];
                    num[j] = temp;
                }
            }
        }
        return num;
    }
    
     public int twoSumClosest(int[] num, int i, int target) {
        int head = i;
        int tail = num.length - 1;
        int result = Integer.MAX_VALUE;
        int result_sum = num[head] + num[tail];

        while(head < tail) {
            int sum = num[head] + num[tail];
            if (sum == target) {
                return sum;
            }
            int diff = Math.abs(target - sum);
            if (diff < result) {
                result = diff;
                result_sum = sum;
            }
            if (sum < target) {
                head++;
            } else if (sum > target) {
                tail--;
            }
        }
        return result_sum;
    }
}