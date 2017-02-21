public class HappyNumber {
    public boolean isHappy(int n) {
        HashSet<Integer> num = new HashSet<Integer>();
	
    	while (n != 1) {
    		if (num.contains(n) == true) {
    			return false;
    		} else {
    			num.add(n);
    		}
    		int s = 0;
    		while (n != 0) {
    			s += ((n % 10) * (n % 10));
    			n = n / 10;
    		}
    		n = s;
    	}
    
    	return true;
    }
}