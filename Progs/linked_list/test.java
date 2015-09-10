import java.io.*;
import java.util.*;

class test {
    public static void main(String[] args) {
	new test().start_test();
    }

    public void start_test() {
	LinkedList l = new LinkedList();
	int ch = 0;
	
	System.out.println("1. Add 2. Delete 3.Display:");
	InputStreamReader iStream = new InputStreamReader(System.in);
	BufferedReader buffReader = new BufferedReader(iStream);
	try {
	do {
	    ch = Integer.parseInt(buffReader.readLine());
	    if(ch == 1)  {
		int val = Integer.parseInt(buffReader.readLine());
		l.insert(val);
	    }
	    else if(ch == 2) {
		int val = Integer.parseInt(buffReader.readLine());
		l.delete(val);
	    }
	    else if(ch == 3) {
		l.display();
	    }
	}while(ch != 0);
	} catch(IOException e) {
	    System.out.println(e.getMessage());
	}
    }
}
