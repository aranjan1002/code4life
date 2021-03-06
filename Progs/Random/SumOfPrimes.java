// Find out the numbers whioh cannot be sum of two prime numbers

import java.util.*;

public class SumOfPrimes {
    public static void main(String[] args) {
	for (int i = 3; i <= 198; i++) {
	    boolean flag = true;
	    for (int j = 2; j <= i/2; j++) {
		if ((isPrime(j) && isPrime(i - j))) {
		    flag = false;
		}
	    }
	    if (flag == true) {
		// System.out.println("Found: " + i);
		list.add(new Integer(i));
	    }
	}

	/*for (int num : list) {
	    for (int i = 2; i <= 99; i++) {
		for (int j = 2; j <= 99; j++) {
		    int cnt1 = 0;
		    if (num == i + j) {
			int prod = i * j;
			int cnt2 = 0;
			for (int k = 2; k <= 99; k++) {
			    for (int l = 2; l <= 99; k++) {
				if (k + l == prod && list.contains(k + l)) {
				    cnt2++;
				}
			    }
			}
			
			}*/

	for (int i = 2; i <= 99; i++) {
	    for (int j = 2; j <= 99; j++) {
		int prod = i * j;
		int cnt = 0;
		for (int k = 2; k <= 99; k++) {
		    for (int l = k; l <= 99; l++) {
			if (k * l == prod && list.contains(new Integer(k + l))) {
			    cnt++;
			    //System.out.println(k + " " + l);
			}
		    }
		}
		if (cnt == 1) {
		    System.out.println("Found: " + prod + " " + i + " " + j + " " + cnt);
		    list2.add(prod);
		}
	    }
	}

	for (int num : list) {
	    int cnt = 0;
	    for (int i = 2; i <= 99; i++) {
		for (int j = i; j <= 99; j++) {
		    if (i + j == num && list2.contains(i * j)) {
			System.out.println(i + " " + j);
			cnt++;
		    }
		}
	    }
	    if (cnt == 1) {
		System.out.println("Found possible result: " + num + " " + cnt);
	    }
	}
    }

    public static boolean isPrime(int i) {
	if (i == 1) {
	    return false;
	}
	else {
	    for (int j = 2; j <= Math.sqrt(i); j++) {
		if (i % j == 0) {
		    return false;
		}
	    }
	    return true;
	}
    }

    static List<Integer> list = new ArrayList<Integer>();
    static List<Integer> list2 = new ArrayList<Integer>();
}