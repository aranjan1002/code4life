class Div {
    public static void main(String[] args) {
	System.out.println(new Div().div(17,3));
    }

    public int div(int a, int b) {
	if(b > a) {
	    return 0;
	}
	else {
            int result = 0;
            int currentQuotient = 1;
            int currentProduct = b;
            int prevQuotient = 1;
            int prevProduct = 1;
            while(currentProduct < a) {
                prevQuotient = currentQuotient;
                prevProduct = currentProduct;
                currentProduct += currentProduct;
                currentQuotient += currentQuotient;
            }
            
            if(currentQuotient > prevQuotient) {
                prevQuotient += div(a - prevProduct, b);
                return prevQuotient;
            }
            return currentQuotient;
        }
    }
}