import java.io.*;
import java.util.*;

public class HotDogs {
    public static void main(String[] args) 
	throws Exception {
	new HotDogs().start();
    }

    ArrayList arr = new ArrayList<PositionDetails>();

    public void start() 
	throws  Exception {
	InputStream is = new FileInputStream(new File("input.in"));
	BufferedReader br = new BufferedReader(
					       new InputStreamReader(is));
	String str = new String();
	str = br.readLine();
	int tests = Integer.parseInt(str);
	int testCount = tests;
	while(true) {
	    while(tests-- > 0) {
		str = br.readLine();
		String[] points_dist = str.split(" ");
		int points = Integer.parseInt(points_dist[0]);
		int distance = Integer.parseInt(points_dist[1]);
		System.out.println(points + " " + distance + "a");
		System.out.println("point = " + points);
		arr.clear();
		for (int i = 0; i < points; i++) {
		    str = br.readLine();
		    String[] point_vendor = str.split(" ");
		    int point = Integer.parseInt(point_vendor[0]);
		    int vendor = Integer.parseInt(point_vendor[1]);
		    System.out.println(point + " " + vendor);
		    arr.add(new PositionDetails(point, vendor));
		}
		System.out.println("Case #" + (testCount - tests + 1) 
				   + ": " 
				   + calc(arr, distance));
	    }
	    break;
	}
    }

    public int calc(ArrayList<PositionDetails> arr, int distance) {
	if(isHighEnough(2.50, arr, distance)) {
	    return 1;
	}
	else {
	    return 0;
	}
	       
    }

    boolean isHighEnough(double t, 
			 ArrayList<PositionDetails> arr, 
			 int distance) {
	double currPos = arr.get(0).position - t;
	System.out.println("curr pos = " + currPos);
	arr.get(0).count--;
	for (int i = 0; i < arr.size(); i++) {
	    for (int j = 0; j < arr.get(i).count; j++) {
		double newPos = getNewPos(currPos + distance, arr.get(i).position, t);
		if (newPos - currPos >= distance) {
		    currPos = newPos;
		}
		else {
		    return false;
		}
	    }
	}
	arr.get(0).count++;
	return true;
    }

    public double getNewPos(double newPos, double oldPos, double time) {
	System.out.println("old pos = " + oldPos + " new pos = " + newPos + " time = " + time);
	if (Math.abs(oldPos - newPos) <= time) {
	    return newPos;
	}
	else if(oldPos < newPos) {
	    return oldPos + time;
	}
	else {
	    return oldPos - time;
	}
    }

    
    
    class PositionDetails {
	double position = 0;
	int count = 0;
	
	PositionDetails(double pos, int cnt) {
	    position = pos;
	    count = cnt;
	}
    }
}
