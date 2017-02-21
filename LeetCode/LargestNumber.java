public class LargestNumber {
    public String largestNumber(int[] num) {
        int[] sorted_num = sort(num);
        String result = new String();
        for (int i = 0; i < sorted_num.length; i++) {
            result += Integer.toString(new Integer(sorted_num[i]));
        }
        return(trim(result));
    }
    
    public int[] sort(int[] num) {
        int temp = 0;
        for (int i = 0; i < num.length; i++) {
            for (int j = i + 1; j < num.length; j++) {
                if (shouldSwap(num[i], num[j]) == true) {
                    temp = num[i];
                    num[i] = num[j];
                    num[j] = temp;
                }
            }
        }
        return num;
    }
    
    public boolean shouldSwap(int a, int b) {
        String a1 = Integer.toString(new Integer(a));
        String b1 = Integer.toString(new Integer(b));
        
        String ab = a1 + b1;
        String ba = b1 + a1;
        
        for (int i = 0; i < ab.length(); i++) {
            if (ab.charAt(i) < ba.charAt(i)) {
                return true;
            } else if (ab.charAt(i) > ba.charAt(i)) {
                return false;
            }
        }
        return false;
    }
    
    public String trim(String result) {
        if (result.length() > 0) {
            if (result.charAt(0) != '0' || result.length() == 1) {
                return result;
            }
            for (int i = 0; i < result.length(); i++) {
                if (result.charAt(0) != '0') {
                    return result.substring(i);
                }
            }
        }
        return result.substring(result.length() - 1);
    }
}