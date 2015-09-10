import java.io.*;
import java.util.*;
import java.lang.*;
import com.google.common.collect.*;

class ICPCBalloons {
    public static void main(String[] args) 
    throws IOException {
	InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream("Input"));
        BufferedReader br = new BufferedReader(is);
        OutputStreamWriter os =
            new OutputStreamWriter(
                                   new FileOutputStream("Output"));
        BufferedWriter bw = new BufferedWriter(os);
        String line;
        int[] inputs;
        int test = 1;
        while ((line = br.readLine()) != null) {
      	    String[] balloonsCountStr = line.split(" ");
	    int[] balloonCount = new int[balloonsCountStr.length];
	    for (int i=0; i<balloonCount.length; i++) {
		balloonCount[i] = Integer.parseInt(balloonsCountStr[i]);
	    }
	    line = br.readLine();
	    String balloonSize = new String(line);
	    //char[] balloonSize = new char[line.length()];
	    //for (int i=0; i<balloonSize.length; i++) {
	    //	balloonSize[i] = line.charAt(i);
	    //}
	    line = br.readLine();
	    String[] maxAcceptedStr = line.split(" ");
	    //System.out.println(maxAcceptedStr.length);
	    int[] maxAccepted = new int[maxAcceptedStr.length];
	    for (int i=0; i<maxAccepted.length; i++) {
		maxAccepted[i] = Integer.parseInt(maxAcceptedStr[i]);
	    }
	    
	    
	    System.out.println(new ICPCBalloons().minRepaintings(balloonCount,
								  balloonSize,
								  maxAccepted));
	}
	br.close();
	bw.close();
    }

    public int minRepaintings(int[] balloonCount,
			      String balloonSize, 
			      int[] maxAccepted) {
	int repaintSum = 0;
	Arrays.sort(maxAccepted);
	//System.out.println(maxAccepted.length);
	MinMaxPriorityQueue<UsedInteger> L = 
	    MinMaxPriorityQueue.<UsedInteger>create();
	MinMaxPriorityQueue<UsedInteger> M = 
	    MinMaxPriorityQueue.<UsedInteger>create();
	for (int i = 0; i < balloonCount.length; i++) {
	    if (balloonSize.charAt(i) == 'L') {
		if (L.add(new UsedInteger(balloonCount[i])) == false) {
		    throw new RuntimeException("unable to add");
		}
	    } else {
		if (M.add(new UsedInteger(balloonCount[i])) == false) {
                    throw new RuntimeException("unable to add");
                }
	    }
	}

	for (int i = maxAccepted.length - 1; i >= 0; i--) {
	    int l = 0;
	    if (L.peekLast() != null) {
		l = L.peekLast().intValue();
	    }
	    int m = 0;
	    if (M.peekLast() != null) {
		m = M.peekLast().intValue();
	    }
	    if (l < m) {
		int repaintCount = removeFrom(M, maxAccepted[i]);
		System.out.println("removing " + maxAccepted[i] +
				   "from M " + m + "repaint count: " +
				   repaintCount);
		if (repaintCount != -1) {
		    repaintSum += repaintCount;
		}
		else return -1;
	    } else {
		int repaintCount = removeFrom(L, maxAccepted[i]);
		System.out.println("removing " + maxAccepted[i] +
				   "from L " + l);
                if (repaintCount != -1) {
                    repaintSum += repaintCount;
                }
		else return -1;
	    }
	}

	return repaintSum;
    }

    int removeFrom(MinMaxPriorityQueue<UsedInteger> q, int val) {
	int repaintCount = 0;
	boolean firstLoop = true;
	UsedInteger i = q.pollLast();
	while (i != null) {
	    int j = i.intValue();
	    j -= val;
	    if (i.used == true || firstLoop == false) {
		repaintCount += Math.min(val, i.intValue());
	    }
	    if (j > 0) {
		UsedInteger nu = new UsedInteger(j);
		if (i.used == true) {
		    nu.used = true;
		}
		else if (firstLoop == true && i.used == false) {
		    nu.setUsed(true);
		}
		q.add(nu);
		return repaintCount;
	    } else if (j== 0) {
		return repaintCount;
	    } else {
		val = Math.abs(j);
		i = q.pollFirst();
	    }
	    firstLoop = false;
	}
	return -1;
    }

    class UsedInteger implements Comparable {
	Integer i;
	boolean used = false;

	public UsedInteger(int in) {
	    i = new Integer(in);
	}

	public int intValue() {
	    return i.intValue();
	}
	void setUsed(boolean u) {
	    used = u;
	}

	public int compareTo(Object o) {
	    return i.compareTo(((UsedInteger)o).i);
	}
    }
}