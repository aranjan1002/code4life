import java.io.*;
import java.lang.*; 

class ChefRecipe {
    public static void main(String[] args)
	throws IOException {
        BufferedReader br =
            new BufferedReader(
                               new InputStreamReader(System.in));

        int tests = Integer.parseInt(br.readLine());

        while (tests-- > 0) {
            String line1 = br.readLine();
	    String line2 = br.readLine();
	    System.out.println(new ChefRecipe().calc(line1, line2));
        }
    }

    public long calc(String line1, String line2) {
	int N = Integer.parseInt(line1);
	String[] ing = line2.split(" ");
	long sum = 0;
	long min = Long.MAX_VALUE;
	for (int i = 1; i <= N; i++) {
	    if (ing[i - 1].equals("1")) {
		return -1;
	    }
	    long q = Long.parseLong(ing[i - 1]);
	    if (q < min) {
		min = q;
	    } 
	    sum += q;
	}

	return (sum - min + 2);
    }
}