import java.lang.*;
import java.util.*;
import java.io.*;

class CounterCulture {
    public static void main(String[] args) 
    throws Exception {
	new CounterCulture().start(args[0]);
	//new CounterCulture().test();
    }

    public void test() {
	System.out.println(first_half(123));
	System.out.println(second_half(123));
	System.out.println(first_half(1234));
	System.out.println(second_half(1234));
	System.out.println(first_half(100));
	System.out.println(second_half(100));
    }

    public void start(String fileName)
        throws Exception {
        InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream(fileName));
        BufferedReader br = new BufferedReader(is);
        OutputStreamWriter os =
            new OutputStreamWriter(
                                   new FileOutputStream("Output"));
        BufferedWriter bw = new BufferedWriter(os);
        String line;

        int test = 1;
        line = br.readLine();
        int tot_test = Integer.parseInt(line);
        while (tot_test >= test) {
            System.out.println("Case #" + test++ + ": " + calc(br));
        }
    }

    String calc(BufferedReader br) throws Exception {
	long n = Long.parseLong(br.readLine());
	long target = 0, counter = 0;
	long result = 0;
	boolean flag = false;
	if (n <= 19) {
            return Long.toString(n);
        }

	if (n % 2 == 0 && n % 5 == 0) {
	    flag = true;
	    n--;
	}

	if (n <= 99) {
	    target = n;
	} else {
	    target = 99;
	}

	counter = 10;
	result = 10;
	
	while (counter != n) {
	    //System.out.println(first_half(counter) + " " +
	    //		       first_half(target));
	    while (first_half(counter) != first_half(target)) {
		//System.out.println(target + " " + counter);
		if (second_half(counter) == first_half(target)) {
		    counter = reverse(counter);
		    result++;
		} else {
		    counter++;
		    result++;
		}
	    }
	    
	    result += (target - counter);
	    counter = target;
	    long  temp_target = target * 10 + 9;
	    if (temp_target > n) {
		target = n;
	    } else {
		target = temp_target;
	    }
	    if (counter != n) {
		counter++;
		result++;
	    }

	    //System.out.println(counter);
	}
	if (flag == true) {
	    result++;
	}

	return Long.toString(result);
    }

    long first_half(long num) {
	String str = Long.toString(num);
	int len = str.length();
	String first_half_str = str.substring(0, len / 2);
	return Long.parseLong(first_half_str);
    }

    long second_half(long num) {
	String str = Long.toString(num);
	int len = str.length();
	int start = len % 2 == 1 ? len / 2 + 1 : len / 2;

	String second_half_str = reverse(str.substring(start, len));
	return Long.parseLong(second_half_str);
    }

    String calc2(BufferedReader br) throws Exception {
	long n = Long.parseLong(br.readLine());
	
	long counter = 10;
	long cnt = 10;
	long target = 99;
	if (n <= 10) {
	    return Long.toString(n);
	} else {
	    if (n <= 19) {
		target = n;
	    } 
	}
	
	while (counter != n) {
	    while (counter != target) {
		System.out.println(counter);
		// System.out.println(first_half(target) + 
		// " " + second_half(counter));
		while (first_half(target) != second_half(counter) || 
		       isPalindrome(counter) == true) {
		    System.out.println(target + " " + counter);
		    System.out.println("First Half: " + 
				       first_half(target) + " " + 
				       "Second Half: " + 
				       second_half(counter));
		    if (counter == n) {
			return Long.toString(cnt);
		    } else if (counter == target) {
			break;
		    }
		    counter++;
		    cnt++;
		}
		if (counter == n) {
		    return Long.toString(cnt);
		}
		counter = reverse(counter);
		cnt++;
	    }
	    long temp = counter * 10 + 9;
	    if (temp < n) {
		target = temp;
	    } else {
		target = n;
	    }
	}
	
	return Long.toString(cnt);
    }

    /*    long first_half(long n) {
	long digits_cnt = num_of_digits(n);
	long remove_cnt = digits_cnt / 2;
	if (digits_cnt % 2 == 1) {
	    remove_cnt++;
	}
	
	while (remove_cnt > 0) {
	    n = n / 10;
	    remove_cnt--;
	}	
	return n;
	}*/

    boolean isPalindrome(long n) {
	String s = Long.toString(n);
	int len = s.length();
	for (int i = 0; i <= (len - 1) / 2; i++) {
	    if (s.charAt(i) != s.charAt(len - 1 - i)) {
		return false;
	    }
	}
	return true;
    }

    /*long second_half(long n) {
	long digits_cnt = num_of_digits(n);
        long remove_cnt = digits_cnt / 2;
	if (digits_cnt % 2 == 1) {
	    remove_cnt++;
	}
	long result = 0;

        while (remove_cnt > 0) {
	    result = result * 10 + (n % 10);
            n = n / 10;
            remove_cnt--;
        } 
        return result;
    }

    long num_of_digits(long n) {
	long cnt = 0;
	while (n > 0) {
	    n = n / 10;
	    cnt++;
	}

	return cnt;
    }
    */
    long reverse(long n) {
	String num_str = Long.toString(n);
	num_str = reverse(num_str);
	return Long.parseLong(num_str);
    }

    String reverse(String str) {
	int len = str.length();
	String result = new String();
	//System.out.println(str);

	for (int i = len - 1; i >= 0; i--) {
	    result += Character.toString(str.charAt(i));
	}

	return result;
    }
}