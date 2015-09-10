// Generates numbers between 1 and 52 in random order

import java.util.Random;

class JumbleNumbers {
    public static void main(String[] args) {
	int[] x = new int[52];
	new JumbleNumbers().jumbleTheNumbers(x);
	if (x != null)
	for (int i = 0; i < x.length; i++) {
	    System.out.println(x[i]);
	}
    }

    void jumbleTheNumbers(int[] x) {
	// x = new int[52];
	Random r = new Random();
	for (int i = 0; i < 52; i++) {
	    x[i] = i + 1;
	}
    
	for (int i = 50; i > 0; i--) {
	    int randomIndex = r.nextInt(i + 1);
	    int temp = x[randomIndex];
	    x[randomIndex] = x[i + 1];
	    x[i + 1] = temp;
	}   
    }
}