public class PlusOne {
    public int[] plusOne(int[] digits) {
        int len = digits.length;
        
        int carry = (digits[len - 1] + 1) / 10;
        digits[len - 1] = (digits[len - 1] + 1) % 10; 
        
        for (int i = len - 2; i >= 0; i--) {
            int temp_carry = (digits[i] + carry) / 10;
            digits[i] = (digits[i] + carry) % 10;
            carry = temp_carry;
        }
        
        if (carry == 0) {
            return digits;
        } else {
            int[] result = new int[len + 1];
            result[0] = carry;
            for (int i = 1; i <= len; i++) {
                result[i] = digits[i - 1];
            }
            return result;
        }
    }
}