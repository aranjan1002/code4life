class Q7 {
    public static void main(String[] args) {
	int[] dom = {25, 10, 5, 1};

	System.out.println(new Q7().solve(25, dom));
	System.out.println();
	System.out.println(new Q7().makeChange(25, 25));
    }

    public int solve(int n, int[] dom) {
	int curr_dom = dom[0];
	if (curr_dom == 1) {
	    return 1;
	}
	int[] rem_dom = getRemainingDom(dom);
	int result = 0;

	for (int i = 0; n >= curr_dom * i; i++) {
	    result += solve(n - curr_dom * i, rem_dom);
	}

	return result;
    }

    public int[] getRemainingDom(int[] dom) {
	int[] result = new int[dom.length - 1];
	for (int i = 1; i <= dom.length - 1; i++) {
	    result[i - 1] = dom[i];
	}
	return result;
    }

    public static int makeChange(int n, int denom) {
	int next_denom = 0;
	switch (denom) {
	    case 25:
	    next_denom = 10;
	    break;
	    case 10:
	    next_denom = 5;
	    break;
	    case 5:
	    next_denom = 1;
	    break;
	    case 1:
		//	System.out.println("1");
	    return 1;
	    }
        int ways = 0;
	for (int i = 0; i * denom <= n; i++) {
	    // System.out.print(denom + " ");
	    ways += makeChange(n - i * denom, next_denom);
	}
	return ways;
   }
}
