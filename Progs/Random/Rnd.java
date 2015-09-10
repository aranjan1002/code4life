import java.util.*;

public class Rnd {
    public static void main(String[] args) {
	/*int[] x = new int[52];
	for(int i=0; i<52; i++) {
	    x[i] = i;
	}
	Rnd nr = new Rnd();
	nr.RandomGenerator.randomize();
	for(int i=0; i < 52; i++) {
	    System.out.println(x[i]);
	}*/
	new Rnd().start();
	//nr.randomize(x);
    }
    public void start() {
	int[] x = new int[52];
        for(int i=0; i<52; i++) {
            x[i] = i;
        }
	new RandomGenerator().randomize(x);
        for(int i=0; i < 52; i++) {
            System.out.println(x[i]);
        }
    }

    class RandomGenerator {
	Random generator = new Random(1924231);
	void randomize(int[] x) {
	    for (int i=x.length - 1; i > 40; i--) {
		int randomIdx = generator.nextInt(i);
		int temp = x[i];
		x[i] = x[randomIdx];
		x[randomIdx] = temp;
	    }
	}
    }
}

