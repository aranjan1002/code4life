public class Pow {
    public double myPow(double x, int n) {
	System.out.println(Math.abs(Integer.MIN_VALUE));
	int sign = 1;
	if (x == 0) {
	    return 0;
	}
	if (n == 0) {
	    return 1;
	}
	if (n < 0) {
	    return (1 / myPow(x, Math.abs(n)));
	}
	if (x < 0 && (Math.abs(n) % 2 == 1)) {
	    sign = -1;
	}
	int sum = 1;
	double prod = x;
	while (sum < (Integer.MAX_VALUE / 2) && sum + sum <= n) {
	    System.out.println(sum + " " + n);
	    prod = prod * prod;
	    sum = sum + sum;
	}
        
	if (sum < n) {
	    return (sign * prod * myPow(x, n - sum));
	}
	return (sign * prod);
    }

    public double pow(double n, int p) {
	// System.out.println(n + " " +  p);
	if (p == 1) {
	    return n;
	}
	
	int q = 1;
	double ans = n;
	while ((q + q) <= p) {
	    ans = ans * ans;
	    q = q + q;
	}

	if (q == p) {
	    return ans;
	} else {
	    return ans * pow(n, p - q);
	}
    }

    public static void main(String[] args) {
	System.out.println(new Pow().myPow(1, -2147483648));
    }

	public double pow2(double x, int n) {
	    System.out.println(x + " " + n);
	    if (n == 0) {
		return 1;
	    } else if (n == 1) {
		return x;
	    } else if (n == -1) {
		return 1/x;
	    }
	    double ans = x;
	    int q = 1;
        
	    while (q + q <= Math.abs(n) && q <= Integer.MAX_VALUE / 2) {
		ans = ans * ans;
		q = q + q;
	    }
        
	    if (n < 0) {
		ans = 1 / ans;
	    }
        
	    if (q == n) {
		return ans;
	    } else {
		return ans * pow2(x, n < 0 ? q - n : n - q);
	    }
	}
}