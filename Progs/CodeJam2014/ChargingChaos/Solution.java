import java.io.*;

public class Solution {
    public static void main(String[] args) 
    throws IOException {
	new Solution().solve(args[0]);
    }

    public void solve(String file_name) 
    throws IOException {
	readFileAndSolve(file_name);
    }

    void readFileAndSolve(String file_name) 
    throws IOException {
	InputStream is = new FileInputStream(new File(file_name));
        BufferedReader br = new BufferedReader(
                                               new InputStreamReader(is));
	
	String str = br.readLine();
        int tests = Integer.parseInt(str);
        int testCount = tests;
	while(tests-- > 0) {
	    String[] s = br.readLine().split(" ");
	    int n = Integer.parseInt(s[0]);
	    int l = Integer.parseInt(s[1]);
	    String[] outlets = br.readLine().split(" ");
	    String[] required = br.readLine().split(" ");
	    String result = solve(n, l, outlets, required);
	    System.out.println("Case #" + (testCount - tests) + ": " + result);
	}

    }

    String solve(int n, int l, String[] outlets, String[] required) {
	String[] flip_strings = new String[n];
	int result = Integer.MAX_VALUE;

	for (int i = 0; i <= n - 1; i++) {
	    // System.out.println(outlets[0]+ " " + required[i]);
	    flip_strings[i] = findFlipString(outlets[0], required[i]);
	    // System.out.println(flip_strings[i]);
	}

	for (int i = 0; i <= n - 1; i++) {
	    if (flipStringWorks(flip_strings[i], outlets, required) == true) {
		int cnt1 = count1(flip_strings[i]);
		if (cnt1 < result) {
		    result = cnt1;
		}
	    }
	}
	if (result == Integer.MAX_VALUE) {
	    return "NOT POSSIBLE";
	}
	return new Integer(result).toString();
    }

    int count1(String str) {
	int cnt = 0;
	for (int i = 0; i <= str.length() - 1; i++) {
	    if (str.charAt(i) == '1') {
		cnt++;
	    }
	}
	return cnt;
    }

    String findFlipString(String outlets, String required) {
	// System.out.println(outlets.length() + " " + required.length());
	String flip_string = new String();
	for (int i = 0; i <= outlets.length() - 1; i++) {
	    if (outlets.charAt(i) != required.charAt(i)) {
		flip_string += "1";
	    } else {
		flip_string += "0";
	    }
	}
	return flip_string;
    }

    boolean flipStringWorks(String flip_string, 
			    String[] outlets, 
			    String[] required) {
	String[] flipped_strings = flip(required, flip_string);
	// System.out.println(flip_string);
	int[] paired = new int[outlets.length];
	int pairs = 0;
	for (int i = 0; i <= outlets.length - 1; i++) {
	    for (int j = 0; j <= outlets.length - 1; j++) {
		if (paired[j] == 0) {
		    if(outlets[i].equals(flipped_strings[j])) {
			// System.out.println(outlets[i] + " " + required[j]);
			paired[j] = 1;
			pairs++;
			break;
		    }
		}
	    }
	}

	if (pairs == outlets.length) {
	    return true;
	}
	return false;
    }

    String[] flip(String[] str_array, String flip_string) {
	String[] result = new String[str_array.length];
	for (int i = 0; i <= str_array.length - 1; i++) {
	    String temp = new String();
	    for (int j = 0; j <= flip_string.length() - 1; j++) {
		char c = str_array[i].charAt(j);
		if (flip_string.charAt(j) == '0') {
		    temp += Character.toString(c);
		}
		else {
		    temp += getFlippedCharAsString(c);
		}
	    }
	    result[i] = temp;
	}
	return result;
    }

    String getFlippedCharAsString(char c) {
	String a = "0";
	String b = "1";

	if (c == '0') {
	    return b;
	} else {
	    return a;
	}
    }
    
}