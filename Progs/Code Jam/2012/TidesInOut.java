import java.io.*;
import java.util.*;

class TidesInOut {
    public static void main(String[] args)
        throws Exception {
        new TidesInOut().start();
    }

    int[][] ceiling;
    int[][] floor;
    double[][] status;

    void start()
        throws Exception {
        InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream("Input"));
        BufferedReader br = new BufferedReader(is);
        OutputStreamWriter os =
            new OutputStreamWriter(
                                   new FileOutputStream("Output"));
        BufferedWriter bw = new BufferedWriter(os);
        String line;
        
        int test = 1;
        line = br.readLine();
        while ((line = br.readLine()) != null) {
	    String[] inp = line.split(" ");
	    int h = Integer.parseInt(inp[0]);
	    int n = Integer.parseInt(inp[1]);
	    int m = Integer.parseInt(inp[2]);
	    row = n;
	    col = m;
	    ceiling = new int[n][m];
	    floor = new int[n][m];
	    status = new double[n][m];
	    wl = h;
	    status[0][0] = 0;
	    
	    initialize(status, n, m);
	    readHeight(ceiling, n, br);
	    readHeight(floor, n, br);
	    
	    String toWrite = "Case #" + test + ": ";
	    System.out.print("Case #" + test++ + ": ");
	    // printInput();
	    bw.write(toWrite, 0, toWrite.length());
	    double result = 0;
	    move();
	    result = status[row - 1][col - 1];
	    System.out.println(result);
            String res = Double.toString(result);
            bw.write(res, 0, res.length());
            bw.newLine();
	}
	br.close();
        bw.close();
    }

    void printInput() {
	System.out.println(row + " " + col);
	for (int i = 0; i < row; i++) {
	    for (int j = 0; j < col; j++) {
		System.out.print(ceiling[i][j] + " ");
	    }
	    System.out.println();
	}
	System.out.println();
	for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(floor[i][j] + " ");
            }
            System.out.println();
        }
    }

    boolean isDirectionAllowed(direction dir, cellType ct) {
	//System.out.println("dir: " + dir + "ct: " + ct);
	if (dir == direction.North) {
	    return (ct != cellType.topRow &&
		    ct != cellType.nwCorner &&
		    ct != cellType.neCorner);
	}
	else if (dir == direction.South) {
	    return (ct != cellType.bottomRow &&
		    ct != cellType.swCorner &&
		    ct != cellType.seCorner);
	}
	else if (dir == direction.East) {
	    return (ct != cellType.neCorner &&
		    ct != cellType.rightCol &&
		    ct != cellType.seCorner);
	}
	else if (dir == direction.West) {
	    return (ct != cellType.nwCorner &&
		    ct != cellType.leftCol &&
		    ct != cellType.swCorner);
	}
	else {
	    System.out.println("Invalid direction: " + dir);
	    System.exit(1);
	    return false;
	}
    }

    void move() {
       move(0, 0, 0, wl);
    }

    void printStatus() {
	for (int i = 0; i < row; i++) {
	    for (int j = 0; j < col; j++) {
		System.out.print(status[i][j] + " ");
	    }
	    System.out.println();
	}
    }

    void move(int i, 
	      int j,
	      double score,
	      int waterLevel) {
	status[i][j] = score;
	cellType ct = getCellType(i, j);
	int ni = i - 1;
        int nj = j;
        int si = i + 1;
        int sj = j;
        int ei = i;
        int ej = j + 1;
        int wi = i;
        int wj = j - 1;
	// System.out.println("Moved to " + i + " " + j + " score: " + score);
	// printStatus();

	if (isDirectionAllowed(direction.North, ct) &&
	    withinLimits(ni, nj)) {
	    checkDirectionAndRecurse(i,
				     j,
				     ni,
				     nj,
				     waterLevel);
	}
	if (isDirectionAllowed(direction.South, ct) &&
	    withinLimits(si, sj)) {
            checkDirectionAndRecurse(i,
                                     j,
                                     si,
                                     sj,
				     waterLevel);
        }
	if (isDirectionAllowed(direction.East, ct) &&
	    withinLimits(ei, ej)) {
            checkDirectionAndRecurse(i,
                                     j,
                                     ei,
                                     ej,
				     waterLevel);
        }
	if (isDirectionAllowed(direction.West, ct) &&
	    withinLimits(wi, wj)) {
            checkDirectionAndRecurse(i,
                                     j,
                                     wi,
                                     wj,
				     waterLevel);
        }
    }

    boolean withinLimits(int i, int j) {
	//System.out.print("Limits of: " + i + " " + j +
	//		 "Row: " + row + " " + col);
	if (i >= 0 &&
	    i < row &&
	    j >= 0 &&
	    j < col) {
	    // System.out.println(": True");
	    return true;
	}
	//System.out.println(": False");
	return false;
    }

    boolean isBlocked(int fromI, 
		      int fromJ,
		      int toI,
		      int toJ,
		      int waterLevel) {
	/*System.out.println("Checking blockage: waterLevel: " +
			   waterLevel + " toFloor: " +
			   floor[toI][toJ] + " toCeiling: " +
			   ceiling[toI][toJ] + " fromFloor: " +
			   floor[fromI][fromJ] + " fromCeiling: " +
			   ceiling[fromI][fromJ]);*/
	// In case the to block is too small
	if (ceiling[toI][toJ] -
	    floor[toI][toJ] < 50) {
	    return true;
	}
	if (ceiling[toI][toJ] - waterLevel < 50 ||
	    ceiling[toI][toJ] - floor[fromI][fromJ] < 50 ||
	    ceiling[fromI][fromJ] - floor[toI][toJ] < 50) {
	    return true;
	}
	return false;
    }

    void checkDirectionAndRecurse(int fromI, 
				  int fromJ,
				  int toI,
				  int toJ,
				  int waterLevel) {
	// System.out.println("From " + fromI + " " + fromJ +
	//		   "To " + toI + " " + toJ);
	double toStatus = status[toI][toJ];
	double fromStatus = status[fromI][fromJ];
	if (status[toI][toJ] != 0) { // not already visited the block
	    // before water stopped dropping
	    boolean blck = isBlocked(fromI, fromJ, toI, toJ, waterLevel);
	    // System.out.println("Blocked: " + blck);
	    if (blck == false) {
		/*System.out.println("Blocked false");
		System.out.println(ceiling[fromI][fromJ] + " " +
				   floor[fromI][fromJ] + " " +
				   ceiling[toI][toJ] + " " +
				   floor[toI][toJ] + " " +
				   waterLevel);*/
		if (fromStatus == 0) {
		    try {
			move(toI, toJ, 0, waterLevel);
		    } catch (Exception e) {
			System.out.println(e.getMessage());
		    }
		}
		else {
		    double newToStatus = 0.0;
		    int newWaterLevel = waterLevel;
		    if (waterLevel - floor[fromI][fromJ] >= 20) {
			newToStatus = fromStatus + 1;
			newWaterLevel -= 10;
		    }
		    else {
			newToStatus = fromStatus + 10;
			newWaterLevel -= 100;
		    }
		    if (newToStatus < toStatus ||
			toStatus == -1) {
			move(toI, toJ, newToStatus, 
			     newWaterLevel);
		    }
		}
	    }
	    else {
		if (ceiling[toI][toJ] - floor[toI][toJ] >= 50 &&
		    ceiling[fromI][fromJ] - floor[toI][toJ] >= 50 &&
		    ceiling[toI][toJ] - floor[fromI][fromJ] >= 50) {
		    // adjacent square is accessible
		    double timeToUnBlock = getTimeToUnblock(fromI, 
							    fromJ,
							    toI,
							    toJ,
							    waterLevel);
		    // System.out.println("Time to unblock: " + timeToUnBlock);
		    double newToStatus = fromStatus + timeToUnBlock;
		    int newWaterLevel = waterLevel - (int) (timeToUnBlock * 10);
		    if (newWaterLevel - floor[fromI][fromJ] >= 20) {
			newToStatus += 1;
			newWaterLevel -= 10;
		    }
		    else {
			newToStatus += 10;
			newWaterLevel -= 100;
		    }
		    if (newToStatus < toStatus ||
			toStatus == -1) {
			move(toI, toJ, newToStatus,
			     newWaterLevel);
		    }
		}
	    }	 
	}
    }
    
    double getTimeToUnblock(int fromI,
			    int fromJ,
			    int toI,
			    int toJ,
			    int waterLevel) {
	int diff1 = 100, diff2 = 100;
	if (ceiling[fromI][fromJ] - floor[fromI][fromJ] < 50 ||
	    ceiling[toI][toJ] - floor[fromI][fromJ] < 50 ||
	    ceiling[fromI][fromJ] - floor[toI][toJ] < 50 ||
	    ceiling[toI][toJ] - floor[fromI][fromJ] < 50) {
	    System.out.println("Error in input: " + fromI + fromJ + toI + toJ);
            System.exit(1);
        }
	if (waterLevel > floor[toI][toJ]) {
	    diff1 = ceiling[fromI][fromJ] -
		waterLevel;
	}
	if (waterLevel > floor[fromI][fromJ]) {
	    diff2 = ceiling[toI][toJ] -
		waterLevel;
	}

	if (diff1 < 50 && diff2 < 50) {
	    System.out.println("Error in input: " + fromI + fromJ + toI + toJ);
	    System.exit(1);
	}
	else if (diff1 < 50) {
	    int waterDiff = 50 - diff1;
	    if (ceiling[toI][toJ] - waterLevel + waterDiff < 50) {
		waterDiff += 50 - ceiling[toI][toJ] + waterLevel - waterDiff;
	    }
	    return waterDiff / 10.0;
	}
	else if (diff2 < 50) {
	    int waterDiff = 50 - diff2;
	    if (ceiling[toI][toJ] - waterLevel + waterDiff < 50) {
		waterDiff += 50 - ceiling[toI][toJ] + waterLevel - waterDiff;
	    }
	    return waterDiff / 10.0;
	}
	return 0;
    }

    cellType getCellType(int r, int c) {
	cellType ct;
	if (r == 0) {
	    if (c == 0) {
	        ct = cellType.nwCorner;
	    }
	    else if (c == col - 1) {
		ct = cellType.neCorner;
	    }
	    else {
		ct = cellType.topRow;
	    }
	}
	else if (c == 0) {
	    if (r == row - 1) {
		ct = cellType.swCorner;
	    }
	    else {
		ct = cellType.leftCol;
	    }
	}
	else if (r == row - 1) {
	    if (c == col - 1) {
		ct = cellType.seCorner;
	    }
	    else {
		ct = cellType.bottomRow;
	    }
	}
	else if (c == col - 1) {
	    ct = cellType.rightCol;
	}
	else {
	    ct = cellType.middle;
	}
	return ct;
    }

    void initialize(double[][] status, int r, int c) {
	for (int i = 0; i < r; i++) {
	    for (int j = 0; j <  c; j++) {
		status[i][j] = -1;
	    }
	}
    }

    void readHeight(int[][] x, int n, BufferedReader br) 
	throws Exception {
	String line;
	//System.out.print ln(n);
	for (int i = 0; i < n; i++) {
	    line = br.readLine();
	    String[] inp = line.split(" ");
	    int j = 0;
	    //System.out.println(line);
	    for (String s : inp) {
		x[i][j++] = Integer.parseInt(s);
	    }
	}
    }

    int wl = 0;
    boolean tidesOut = false;
    int row = 0, col = 0;
    enum cellType {leftCol, rightCol, bottomRow, topRow, 
	    nwCorner, neCorner, swCorner, seCorner, middle};
    enum direction {North, South, East, West};
    static List<RowCol> rcForMiddle = new ArrayList<RowCol>();
    static List<RowCol> rcForNWCorner = new ArrayList<RowCol>();
    static List<RowCol> rcForSWCorner = new ArrayList<RowCol>();
    static List<RowCol> rcForNECorner = new ArrayList<RowCol>();
    

    static {
	rcForMiddle.add(new RowCol(-1, 0));
	rcForMiddle.add(new RowCol(0, -1));
	rcForMiddle.add(new RowCol(1, 0));
	rcForMiddle.add(new RowCol(0, 1));
	rcForNWCorner.add(new RowCol(0, 1));
	rcForNWCorner.add(new RowCol(1, 0));
	rcForSWCorner.add(new RowCol(-1, 0));
	rcForSWCorner.add(new RowCol(0, 1));
	rcForNECorner.add(new RowCol(0, -1));
	rcForNECorner.add(new RowCol(1, 0));
    };
}