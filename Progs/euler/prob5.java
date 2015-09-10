public class prob5 {
    public static void main(String[] args) {
	new prob5().start();
    }

    public void start() {
	for (int i = 20; true; i = i + 20) {
	    int j;
	    for (j = 1; j <= 20; j++) {
		if (i % j != 0) {
		    break;
		}
	    }
	    if (j == 21) {
		System.out.println(i);
		return;
	    }
	}
    }
}