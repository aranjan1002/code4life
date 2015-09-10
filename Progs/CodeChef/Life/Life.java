import java.io.*;

class Life {
    public static void main(String[] args) 
    throws IOException {
	BufferedReader br = 
	    new BufferedReader(
			       new InputStreamReader(System.in));

	int tests = Integer.parseInt(br.readLine());

	while (tests-- > 0) {
	    int num = Integer.parseInt(br.readLine());
	    if (num != 42) {
		System.out.println(num);
	    }
	    else break;
	}
								     
    }
}