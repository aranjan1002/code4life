class prob6 {
    public static void main(String[] args) {
	long sum = 0, product = 0;
	for (int i = 1; i <= 100; i++) {
	    sum = sum + (i * i);
	    product = product + i;
	}
	System.out.println(sum + " " + product);
	product = product * product;
	System.out.println(product - sum);
    }
}