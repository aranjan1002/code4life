public class GasStation {
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
            } else if ((i + j) % gas.length < i) {
                return -1;
            } else {
                i = (i + j) % gas.length;
            }
        }
        
        return -1;
    }
}