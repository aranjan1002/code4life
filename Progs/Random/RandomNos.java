import java.util.Random;

class RandomNos {
    public static void main(String[] args) {
	int[] x = new RandomNos().randomize(52);
	for(int i=1; i<x.length; i++) {
	    System.out.println(x[i]);
	}
    }
    
    public int[] randomize(int n) {
	int[] arr = new int[n+1];
	Random generator = new Random(12345);
	for(int i=1; i<n; i++) arr[i] = i;
	for(int i=52; i>1; i--) {
	    int r = generator.nextInt(i+1);
	    int temp = arr[r];
	    arr[r] = arr[i];
	    arr[i] = temp;
	}
	return arr;
    }
}
