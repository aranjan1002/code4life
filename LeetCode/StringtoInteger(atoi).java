public class StringtoInteger(atoi) {
    public int myAtoi(String str) {
        int sum = 0;
        int sign = 1;
        int idx = 0;
        int max = Integer.MAX_VALUE;
        int min = Integer.MIN_VALUE;
        
        while (idx <= str.length() - 1 && str.charAt(idx) == ' ') {
            idx++;
        }
        
        if (idx <= str.length() - 1) {
            if (str.charAt(idx) == '-') {
                sign = -1;
                idx++;
            } else if (str.charAt(idx) == '+') {
                idx++;
            }
            
            while (idx <= str.length() - 1) {
                char c = str.charAt(idx);
                if (c >= '0' && c <= '9') {
                    if (sum > max / 10 || (sum == max / 10 && c > '7')) {
                        if (sign == 1) {
                            return max;
                        }
                        return min;
                    }
                    sum = sum * 10 + str.charAt(idx++) - '0';
                } else {
                    break;
                }
            }
        }
        
        return (sum * sign);
    }
    
    public boolean isNum(char c) {
        return (c >= 48 && c <= 57);
    }
}