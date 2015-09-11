class consecutives {
    public static void main(String[] args) {
	int x[] = {1,2,3,4,5,6,7};
	new consecutives().increment(x, 6);
    }
    
    public void increment (int[] x, int pos) {
        if (pos < 0) {
	    return;
	}
	int val = x[pos];
	int i = 0;
	for (i = pos; i < x.length; i++) {
	    if (val >= 9) {
		increment(x, pos - 1);
		return;
	    }
	    else x[i] = ++val;
	}
	display(x);
	increment(x, x.length - 1);
    }

    public void display(int[] x) {
	for (int i=0; i<x.length; i++) {
	    System.out.print(x[i]);
	}
	System.out.println();
    }
}