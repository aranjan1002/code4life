import java.lang.*;
import java.util.*;

class CalcStrings {
    public List<String> calculate_words(String digits) {
	List<String> result = new ArrayList<String>();

	if (digits.length() == 0) {
	    result.add(new String());
	    return result;
	}

	if (digits.length() == 1) {
	    result.add(digits);
	    return result;
	}

	char c1 = digits.charAt(0);
	
	List<String> result1 = calculate_words(digits.substring(1, 
								digits.length()));
	if (c1 == '0') {
	    return result1;
	} else {
	    addToResult(Character.toString(c1), result1, result);
	}

	if (digits.length() > 1) {
	    char c2 = digits.charAt(1);
	    if (isWithinRange(c1, c2) == true) {
		if (digits.length() > 2) {
		    List<String> result2 = calculate_words(digits.substring(
									    2,
									    digits.
									    length()
									    ));
		    addToResult(digits.substring(0, 2), result2, result);
		} else {
		    result.add(digits.substring(0, 2));
		}
	    }
	}

	return result;
    }

    private void addToResult(String s, 
			     List<String> toAdd,
			     List<String> result) {
	for (String str : toAdd) {
	    result.add(s + ", " + str);
	}
    }

    private boolean isWithinRange(char c1, char c2) {
	if (c1 == '1') {
	    return true;
	} else if (c1 == '2' && c2 <= '7') {
	    return true;
	}

	return false;
    }

    public static void main(String[] args) {
	String s = "1231243211";
	List<String> result = new CalcStrings().calculate_words(s);
	print (result);
    }

    public static void print(List<String> result) {
	for (String str : result) {
	    System.out.println(str);
	}
    }
}