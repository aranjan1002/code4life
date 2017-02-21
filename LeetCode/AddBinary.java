public class AddBinary {
    public String addBinary(String a, String b) {
        int l1 = a.length() - 1;
        int l2 = b.length() - 1;
        String temp = new String();
        int t = 0;
        
        if (l1 < l2) {
            temp = a;
            a = b;
            b = temp;
            t = l1;
            l1 = l2;
            l2 = t;
        }
        
        String result = new String();
        BinarySum s = new BinarySum();
        
        while(l2 >= 0) {
            s = add(intValue(a.charAt(l1)), 
                    intValue(b.charAt(l2)),
                    s.carry);
            result = (s.sum + result);
            l1--;
            l2--;
        }
        
        while(l1 >= 0) {
            s = add(intValue(a.charAt(l1)), s.carry, 0);
            result = s.sum + result;
            l1--;
        }
        
        if (s.carry == 1) {
            result = s.carry + result;
        }
        
        return result;
    }
    
    int intValue(char a) {
        if (a == '0') {
            return 0;
        }
        return 1;
    }
    
    BinarySum add(int a, int b, int c) {
        int carry = 0;
        BinarySum s1 = add(a,b);
        BinarySum s2 = add(s1.sum, c);
        
        if (s1.carry == 1 || s2.carry == 1) {
            s2.carry = 1;
        }
        return s2;
    }
    
    BinarySum add(int a, int b) {
        if (a == 0) {
            if (b == 0) {
                return new BinarySum(0, 0);
            } else {
                return new BinarySum(1, 0);
            }
        } else {
            if (b == 0) {
                return new BinarySum(1, 0);
            } else {
                return new BinarySum(0, 1);
            }
        }
    }
    
    class BinarySum {
        BinarySum() {}
        
        BinarySum(int sum, int carry) {
            this.sum = sum;
            this.carry = carry;
        }
        public int sum = 0;
        public int carry = 0;
    }
}