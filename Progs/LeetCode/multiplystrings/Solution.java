import java.lang.*;

public class Solution {
    public static void main(String[] args) {
	System.out.println(new Solution().multiply("9369162965141127216164882458728854782080715827760307787224298083754", "7204554941577564438"));
    }

    public String multiply(String num1, String num2) {
	String[] products = multiplyEachDigit(num1, num2);
	addTrailingZeroes(products);
	String result = addAll(products);
	return result;
    }
    
    String[] multiplyEachDigit(String num1, String num2) {
	String[] result = new String[num2.length()];
        
	for (int i = 0; i <= result.length - 1; i++) {
	    result[num2.length() - 1 - i] = multiplyWithDigit(num1, num2.charAt(i));
	    // System.out.println(result[num2.length() - 1 - i]);
	}
	return result;
    }
    
    String multiplyWithDigit(String num1, char digit) {
	String result = new String();
	int dig1 = digit - 48;
	int carry = 0, prod, dig2;
	for (int i = num1.length() - 1; i >= 0; i--) {
	    dig2 = num1.charAt(i) - 48;
	    prod = (dig2 * dig1) + carry;
	    result = Integer.toString(prod % 10) + result;
	    carry = prod / 10;
	}

	if (carry > 0) {
	    result = Integer.toString(carry) + result;
	}

	return result;
    }
    
    void addTrailingZeroes(String[] products) {
	for (int i = 0; i <= products.length - 1; i++) {
	    for (int j = 1; j <= i; j++) {
		products[i] = products[i] + "0";
	    }
	}
    }
    
    String addAll(String[] products) {
	String result = new String();
	for (int i = 0; i <= products.length - 1; i++) {
	    result = add(result, products[i]);
	}
        
	return result;
    }
    
    String add(String a, String b) {
	int idx1 = a.length() - 1, idx2 = b.length() - 1;
	String result = new String();
	int carry = 0;
        
	while (idx1 >= 0 && idx2 >= 0) {
	    int dig1 = a.charAt(idx1) - 48;
	    int dig2 = b.charAt(idx2) - 48;
            
	    int sum = dig1 + dig2 + carry;
	    result = Integer.toString(sum % 10) + result;
	    carry = sum / 10;
	    idx1--;
	    idx2--;
	}
        
	while (idx1 >= 0) {
	    int dig1 = a.charAt(idx1--) - 48;
	    int sum = dig1 + carry;
	    result = Integer.toString(sum % 10) + result;
	    carry = sum / 10;
	}
        
	while (idx2 >= 0) {
	    int dig1 = b.charAt(idx2--) - 48;
	    int sum = dig1 + carry;
	    result = Integer.toString(sum % 10) + result;
	    carry = sum / 10;
	}
        
	if (carry > 0) {
	    result = Integer.toString(carry) + result;
	}
        
	return result;
    }
}