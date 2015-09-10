public class Sqrt {
    double sqrt(double n, double precision) {
	double low = 1.0, high = 1.0;
    
	if (n < 1) {
	    low = n;
	} else {
	    high = n;
	}
    
	while (high - low > precision) {
	    double mid = (high + low) / 2.0;
	    double sq_mid = mid * mid;
        
	    if (sq_mid < n) {
		low = mid;
	    } else if (sq_mid > n) {
		high = mid;
	    }
	}
    
	return (low + high) / 2.0;
    }

    public static void main(String[] args) {
	System.out.println(new Sqrt().sqrt(-5, 0.0001));
    }
}