import java.util.PriorityQueue;
import java.util.*;
import java.lang.*;
import java.io.*;

class Haircut {
    public static void main(String[] args) throws Exception {
	new Haircut().start();
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
	
	PriorityQueue<BarberInfo> pq = new PriorityQueue<BarberInfo>
	    ((int) B, new BarberComparator());
	if (B >= N) {
	    return Long.toString(N);
	}

	for (int i = 0; i <= B - 1; i++) {
	    pq.add(new BarberInfo(Long.parseLong(line2[i]), i));
	}
	System.out.println(N);
	for (int i = (int) B; i < N - 1; i++) {
	    BarberInfo b = pq.poll();
	    b.time = b.time + Long.parseLong(line2[(int) b.index]);
	    pq.add(b);
	    System.out.println(i);
	}
	
	return Long.toString(pq.poll().index + 1);
    }

    long findBarberForNthCustomer(long N, long[] barbers_time) {
	long time = findTimeForNthCustomer(N, barbers_time);
	return findBarberWithCustomer(time, N, barbers_time);
    }

    long findBarberWithCustomer(long time, long N, long[] barbers_time) {
	long last_N = findNUntil(time - 1);
	for (int i = 0; i <= barbers_time.length - 1; i++) {
	    if ((time - 1) % barbers_time[i] == 0) {
		last_N++;
		if (last_N == N) {
		    System.out.println(i);
		    return i;
		}
	    }
	}
    }

    long findTimeForNthCustomer(long N, long[] barbers_time) {
	long time = 2;
	while (true) {
	    double cnt = findCustomerCountAtTime(time, barbers_time);
	    if (cnt == N) {
		return cnt;
	    } else if (cnt < N) {
		time = 2 * cnt;
	    } else {
		return binarySearch(N, barbers_time, time / 2, time);
	    }
	}
    }

    long binarySearch(long N, long[] barbers_time, long low, long high) {
	while (low < high) {
	    long mid = (low + high) / 2;
	    double cnt = findCustomerCountAtTime(mid, barbers_time);
	    if (cnt == N) {
		return mid;
	    } else if (cnt < N) {
		low = mid + 1;
	    } else {
		high = mid - 1;
	    }
	    return low;
	}
    }

    long findCustomerCountAtTime(long time, long[] barbers_time) {
	long result = barbers_time.length;
        for (int i = 0; i <= barbers_time.length - 1; i++) {
	    result += (time - 1) / barbers_time;
	}

	return result;
    }

    class BarberInfo {
	long time = 0;
	long index = 0;
	BarberInfo(long time, long index) {
	    this.time = time;
	    this.index = index;
	}
    }

    class BarberComparator implements Comparator<BarberInfo> {
	public int compare(BarberInfo b1, BarberInfo b2) {
	    if (b1.time < b2.time) {
		return -1;
	    } else if (b1.time > b2.time) {
		return 1;
	    } else {
		if (b1.index < b2.index) {
		    return -1;
		} else if (b1.index > b2.index) {
		    return 1;
		} 
		return 0;
	    }
	}
    }
}