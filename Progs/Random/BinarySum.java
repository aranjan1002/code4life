import java.lang.*;
import java.util.*;

class BinarySum {
    String sum(String s1, String s2) {
	StringBuilder s = new StringBuilder();
	int len1 = s1.length();
	int len2 = s2.length();

	char carry = '0';
	int i = 0;
	
	for (i = 0; i <= s1.length() - 1; i++) {
	    if (s2.length() - i - 1 < 0) {
		break;
	    } 

	    char a = s1.charAt(len1 - i - 1);
	    char b = s2.charAt(len2 - i - 1);

	    char sum = getSum(a, b, carry);
	    carry = getCarry(a, b, carry);
	    
	    s.append(Character.toString(sum));
	}
	
	while (len1 - i - 1 >= 0) {
	    char a = s1.charAt(len1 - i - 1);
	    char sum = getSum(a, carry, '0');
	    carry = getCarry(a, carry, '0');

	    s.append(Character.toString(sum));
	    i++;
	}

	while (len2 - i - 1 >= 0) {
	    char a = s2.charAt(len2 - i - 1);
            char sum = getSum(a, carry, '0');
            carry = getCarry(a, carry, '0');

            s.append(Character.toString(sum));
	    i++;
        }

	if (carry == '1') {
	    s.append('1');
	}

	return reverse(s);
    }

    char getSum(char a, char b, char carry) {
	return getSum(getSum(a, b), carry);
    }

    char getSum(char a, char b) {
	if (a == '0') {
	    return b;
	} else {
	    if (b == '1') {
		return '0';
	    } else {
		return '1';
	    }
	}
    }

    char getCarry(char a, char b, char c) {
	if (getCarry(a, b) == '1' ||
	    getCarry(b, c) == '1' ||
	    getCarry(a, c) == '1') {
	    return '1';
	}

	return '0';
    }

    char getCarry(char a, char b) {
	if (a == '0' || b == '0') {
	    return '0';
	}
	return '1';
    }

    String reverse(StringBuilder s) {
	String res1 = s.toString();
	StringBuilder result = new StringBuilder();

	for (int i = res1.length() - 1; i >= 0; i--) {
	    result.append(Character.toString(res1.charAt(i)));
	}

	return result.toString();
    }

    public static void main(String[] args) {
	System.out.println(new BinarySum().sum("11111111", "0"));
    }
}