public class AdditiveNumber {
    public boolean isAdditiveNumber(String num) {
        if (num == null || num.length() <= 2) {
            return false;
        }

        for (int i = 1; i <= num.length() - 2; i++) {
            if (num.charAt(0) == '0' && i > 1) continue;
            for (int j = i + 1; j <= num.length() - 1; j++) {
                if (num.charAt(i) == '0' && j > i + 1) continue;
                long first = Long.parseLong(num.substring(0, i));
                long second = Long.parseLong(num.substring(i, j));
                int third_idx = j;
                while (true) {
                    long third = first + second;
                    System.out.println(first + " " + second + " " + third_idx);
                    third_idx = parse(num, third, third_idx);
                    if (third_idx == -1) break;
                    else if (third_idx == num.length()) {
                        return true;
                    } else {
                        first = second;
                        second = third;
                    }
                }
            }
        }
        return false;
     }
     
     int parse(String num, long val, int idx) {
         String val_str = Long.toString(val);
         int cnt = 0;
         while (idx <= num.length() - 1 && cnt <= val_str.length() - 1 && num.charAt(idx) == val_str.charAt(cnt)) {
             idx++;
             cnt++;
         }
         //System.out.println(cnt);
         if (cnt == val_str.length()) {
             return idx;
         }
         return -1;
     }
}