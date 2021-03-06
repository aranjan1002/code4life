public class Sqrt_int {
    public double sqrt_int(int a) {
	int low = 1;
	int high = a / 2;

	if (a == 1) {
	    return a;
	} else if (a < 1) {
	    return 0;
	}

	while(low < high) {
	    int mid = (low + high) / 2;
	    if (mid  < a / mid) {
		low = mid + 1;
	    } else if (low == mid) {
		return mid - 1;
	    } else {
		high = mid - 1;
	    }
	}

	return low;
    }

    public static void main(String[] args) {
	System.out.println(new Sqrt_int().sqrt_int(15));
    }
}