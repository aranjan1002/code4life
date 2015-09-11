import java.io.*;
import java.lang.*;

public class SumOfSquares {
    public static void main(String[] args) 
	throws IOException {
	new SumOfSquares().start();
    }

    public void start() 
	throws IOException {
	InputStreamReader ir = new InputStreamReader(System.in);
	BufferedReader br = new BufferedReader(ir);
	
	int a = Integer.parseInt(br.readLine());
	int b = Integer.parseInt(br.readLine());
	int sum = 0;
	
	for (int i = a; i <= b; i++) {
 	    sum += ((i%10) * (i%10)) % 10;
	    sum = sum % 10;
	}
	System.out.println("Result: " + sum);
    }
}