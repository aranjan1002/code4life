import java.lang.*;
import java.io.*;
import java.util.*;

class Barber {
    public static void main(String[] args) 
    throws Exception {
	new Barber().start();
    }

    public void start2() {
	long[] barbers_time = {13, 22, 19, 7, 2};
	long N = 229887443;

	System.out.println(findBarberForNthCustomer(N, barbers_time));
    }

    public void start()
        throws Exception {
        InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream("Input2"));
        BufferedReader br = new BufferedReader(is);
        OutputStreamWriter os =
            new OutputStreamWriter(
                                   new FileOutputStream("Output"));
        BufferedWriter bw = new BufferedWriter(os);
        String line;

        int test = 1;
        line = br.readLine();
        int tot_test = Integer.parseInt(line);
        while (tot_test >= test) {
            System.out.println("Case #" + test++ + ": " + calc(br));
        }
    }

    public String calc(BufferedReader br) throws IOException {
        String[] line1 = br.readLine().split(" ");
        String[] line2 = br.readLine().split(" ");
        long B = Long.parseLong(line1[0]);
        long N = Long.parseLong(line1[1]);
	long[] barbers_time = new long[line2.length];
        if (B >= N) {
	    //System.out.println("returning now");
            return Long.toString(N);
        }
	
        for (int i = 0; i <= B - 1; i++) {
            barbers_time[i] = Long.parseLong(line2[i]);
	}

	return Long.toString(findBarberForNthCustomer(N, barbers_time) + 1);
    }
    
    

    void print(long[] num) {
	for (int i = 0; i <= num.length - 1; i++) {
	    System.out.println(num[i]);
	}
    }

    long findBarberForNthCustomer(long N, long[] barbers_time) {
        long time = findTimeForNthCustomer(N, barbers_time);
	//System.out.println(time);
        return findBarberWithCustomer(time, N, barbers_time);
	//System.out.println(findCustomerCountAtTime(7, barbers_time));
	//return 0;
    }

    long findBarberWithCustomer(long time, long N, long[] barbers_time) {
        long last_N = findCustomerCountAtTime(time - 1, barbers_time);
	//System.out.println(last_N);
        for (int i = 0; i <= barbers_time.length - 1; i++) {
            if ((time - 1) % barbers_time[i] == 0) {
                last_N++;
                if (last_N == N) {
		    //System.out.println("returning" + i);
                    return i;
                }
            }
        }
	return barbers_time[barbers_time.length - 1];
    }

    long findTimeForNthCustomer(long N, long[] barbers_time) {
        long time = 2;
        while (true) {
            long cnt = findCustomerCountAtTime(time, barbers_time);
	    // System.out.println(cnt + " " + time);
            if (cnt == N) {
                return cnt;
            } else if (cnt < N) {
                time = 2 * time;
            } else {
                return binarySearch(N, barbers_time, time / 2, time);
            }
        }
    }
    
    long binarySearch(long N, long[] barbers_time, long low, long high) {
        while (low < high) {
            long mid = (low + high) / 2;
            long cnt = findCustomerCountAtTime(mid, barbers_time);
	    // System.out.println(mid + " " + cnt + " " + low + " " + high);
            if (cnt == N) {
                high = mid;
            } else if (cnt < N) {
                low = mid + 1;
            } else {
                high = mid;
            }
	}
	return low;
    }

    long findCustomerCountAtTime(long time, long[] barbers_time) {
        long result = barbers_time.length;
        for (int i = 0; i <= barbers_time.length - 1; i++) {
            result += ((time - 1) / barbers_time[i]);
        }

        return result;
    }
}
