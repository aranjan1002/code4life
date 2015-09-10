import java.util.*;
import java.lang.*;

public class Solution {
    public static void main(String[] args) {
	System.out.println(new Solution().getPermutation(8, 31492));
    }
    
    public String getPermutation(int n, int k) {
        String str = getFirstString(n);
        
        int cnt = 1;
        while (cnt < k) {
            str = getNextString(str, n);
            cnt++;
        }

	return str;
    }
    
    String getFirstString(int n) {  
        String result = new String();
        for (int i = 1; i <= n; i++) {
            result = result + Integer.toString(i);
        }
        
        return result;
    }
    
    String getNextString(String str, int n) {
	// System.out.println(str);
        if (checkLast(str) == true) {
            return str;
        }
        do {
	    int idx = str.length() - 1;
	    while (idx >= 0 && str.charAt(idx) - '0' == n) {
		idx--;
	    }
	    // System.out.println(idx);
	    if (idx <= str.length() - 2) {
		str = setChar(str, idx, (char) (str.charAt(idx) + 1));
		for (int i = idx + 1; i <= str.length() - 1; i++) {
		    str = setChar(str, i, (char) ('0' + i - idx));
		}
	    } else {
		str = setChar(str, 
			      str.length() - 1, 
			      (char) (str.charAt(str.length() - 1) + 1));
	    }
	    // System.out.println(str);
	}
	while(checkUnique(str) == false);    
	System.out.println(str);
	return str;
    }

    String setChar(String str, int idx, char c) {
	String result = str.substring(0, idx);
	result += Character.toString(c);
	if (idx + 1 <= str.length() - 1) {
	    result += str.substring(idx + 1, str.length());
	}

	return result;
    }

    boolean checkUnique(String str) {
	Map<Character, Boolean> map = new HashMap<Character, Boolean>();
	
	for (int i = 0; i <= str.length() - 1; i++) {
	    Character c = new Character(str.charAt(i));
	    if (map.containsKey(c) == true) {
		return false;
	    } else {
		map.put(c, new Boolean(true));
	    }
	}

	return true;
    }

    boolean checkLast(String str) {
	for (int i = 0; i <= str.length() - 2; i++) {
	    if (str.charAt(i) <= str.charAt(i + 1)) {
		return false;
	    }
	}

	return true;
    }
}