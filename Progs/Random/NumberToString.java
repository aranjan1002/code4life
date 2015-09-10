import java.util.*;
import java.lang.*;

class NumberToString {
    public static void main(String[] args) {
	try {
	    System.out.println(new NumberToString().start(args));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    String start(String[] args) 
	throws Exception {
	String n = args[0];
	return numberToString(n);
    }

    String numberToString(String n) 
	throws Exception {
	Map<Integer, String> map = new HashMap<Integer, String>();
	map.put(new Integer(1), "One");
	map.put(new Integer(2), "Two");
	map.put(new Integer(3), "Three");
	map.put(new Integer(4), "Four");
	map.put(new Integer(5), "Five");
	map.put(new Integer(6), "Six");
	map.put(new Integer(7), "Seven");
	map.put(new Integer(8), "Eight");
	map.put(new Integer(9), "Nine");
	map.put(new Integer(10), "Ten");
	map.put(new Integer(11), "Eleven");
	map.put(new Integer(12), "Twelve");
	map.put(new Integer(13), "Thirteen");
	map.put(new Integer(14), "Fourteen");
	map.put(new Integer(15), "Fifteen");
	map.put(new Integer(16), "Sixteen");
	map.put(new Integer(17), "Seventeen");
	map.put(new Integer(18), "Eighteen");
	map.put(new Integer(19), "Nineteen");
	map.put(new Integer(20), "Twenty");
	map.put(new Integer(30), "Thrity");
	map.put(new Integer(40), "Forty");
	map.put(new Integer(50), "Fifty");
	map.put(new Integer(60), "Sixty");
	map.put(new Integer(70), "Seventy");
	map.put(new Integer(80), "Eighty");
	map.put(new Integer(90), "Ninety");
	map.put(new Integer(0), "Zero");
    
	String result = new String();
	int len = n.length();

	if (n == null || n.length() == 0) {
	    return result;
	}

	if (n.length() <= 3) {
	    return getNumberToStringForThreeDigits(n, map);
	}
	else {
	    String last3Digits = n.substring(len - 3);
	    String result1 = getNumberToStringForThreeDigits(last3Digits, map);
	    String first3Digits = n.substring(0, len - 4);
	    String result2 = getNumberToStringForThreeDigits(first3Digits,
							     map);
	    result = result2 + " Thousand";
	    if (result1.equals("") == true) {
		return result;
	    }
	    else {
		return result + ", " + result1;
	    }
	}
    }

    public String getNumberToStringForThreeDigits(String n, 
						  Map<Integer, String> map) 
	throws Exception{
	Integer zero = new Integer(0);
	String result = new String();
    
	//last two digits
	if (n == null || n.equals("")) {
	    return result;
	}
	
	int len = n.length();
	//System.out.println(n + len);
	if (len == 1) {
	    Integer lastDigit = new Integer(n);
	    return map.get(lastDigit);
	}
	Integer last2Digits = new Integer(n.substring(len - 2));
	if (last2Digits.equals(zero) == false) {
	    if (map.containsKey(last2Digits)) {
		result += map.get(last2Digits);
	    } 
	    else {
		int digit1 = last2Digits.intValue() / 10;
		int digit2 = last2Digits.intValue() % 10;
		if (digit1 != 0) {
		    result += map.get(new Integer(digit1 * 10));
		}
		if (digit2 != 0) {
		    result += " " + map.get(new Integer(digit2));
		}
	    }
	}
        
	
	if (len > 2) {   
	    Integer thirdLastDigit = new Integer(n.substring(len - 3,
							     len - 2));
	    //	    System.out.println(thirdLastDigit + n);
	    if (thirdLastDigit.equals(zero) == false) {
		if (map.containsKey(thirdLastDigit)) {
		    String thirdLastWord = map.get(thirdLastDigit);
		    if (result.equals("") == false) {
			result = thirdLastWord +
			    " hundred and " +
			    result;
		    }
		    else {
			result = thirdLastWord +
			    " hundred";
		    }
		}
		else {
		    throw new Exception("Invalid input");
		}
	    }
	}
	return result;
    }
}
	    