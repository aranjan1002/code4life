public class Solution {
    public static void main(String[] args) {
	int[] gas = {2, 2, 3};
	int[] cost = {1, 2, 4};
	System.out.println(new Solution().canCompleteCircuit(gas, cost));
    }

    public int canCompleteCircuit(int[] gas, int[] cost) {
        for (int i = 0; i <= gas.length - 1; i++) {
            int sum = 0;
            int j = i;
            for (j = 0; j <= gas.length; j++) {
                sum += gas[(i + j) % gas.length];
                if (sum < cost[(i + j) % gas.length]) {
                    break;
                } else {
                    sum -= cost[(i + j) % gas.length];
                }
            }
            
            if (j == gas.length + 1) {
                return i;
            }
        }
        
        return -1;
    }
}