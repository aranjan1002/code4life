public class One {
    public static void main(String[] args) {
	int sum=0;
	/*for(int i=3; i<=1000; i++) {
	    if(i%3==0 || i%5==0) {
		sum += i;
		//if(i%3==0 && i%5==0) sum+=i;
		System.out.println(sum);
	    }
	    }*/
	
	System.out.println(findMaxPrimeFactor(600851475143L));
    }

    public static double fibSum(long a, long b, long sum) {
	if (a <= 4000000 && a%2==0) sum += a;
	System.out.println(sum);
	if(b <= 4000000) {
	    return fibSum(b, a+b, sum);
	}
	else return sum;
    }
    
    public static long findMaxPrimeFactor(long n) {
	long limit = (long)Math.sqrt(n);
	long result = n;
	for(long i=2; i<=limit; i++) {
	    if(n%i == 0 && isPrime(i)==true) {
		result = i;
		if(isPrime(n/i)==true) result = i;
		System.out.println(i);
	    }
	}
	return result;
    }

    public static boolean isPrime(long n) {
	long limit = (long) Math.sqrt(n);
	for(long i=2; i<=limit; i++) {
	    if(n%i == 0) return false;
	}
	return true;
    }
}