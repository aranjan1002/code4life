public class MajorityElement {
    public int majorityElement(int[] num) {
        Map<Integer, Integer> num_frequency = new HashMap<Integer, Integer>();
        int n = num.length;
        
        for (int i = 0; i <= n - 1; i++) {
            int frequency = 1;
            if (num_frequency.containsKey(num[i])) {
                frequency = num_frequency.get(num[i]).intValue();
                frequency++;
            } 
            
            if (frequency >= n/2 + 1) {
                return num[i];
            }
            num_frequency.put(num[i], frequency);
        }
        
        return 0;
    }
}