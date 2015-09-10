import java.io.*;
import java.util.*;
import java.lang.*;

class NoisyNeighbors {
    public static void main(String[] args) 
	throws Exception {
	new NoisyNeighbors().start(args[0]);
    }

    public void start(String fileName)
        throws Exception {
        InputStreamReader is =
            new InputStreamReader(
                                  new FileInputStream(fileName));
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
            System.out.println("Case #" + test++ + ": " + start2(br));
        }
    }


    public String start2(BufferedReader br) 
    throws Exception {
	String[] line = br.readLine().split(" ");
	int R = Integer.parseInt(line[0]), 
	    C = Integer.parseInt(line[1]), 
	    N = Integer.parseInt(line[2]);
	if (R == 0 || C == 0 || N == 0) {
	    return "0";
	}
	List<String> comb = combinations(R * C, N);

	int min = Integer.MAX_VALUE;
	for (int i = 0; i <= comb.size() - 1; i++) {
	    int curr_result = calc(comb.get(i), R, C);
	    if (curr_result < min) {
		min = curr_result;
		//System.out.println(curr_result);
	    }
	}
	return Integer.toString(min);
    }
    
    int calc(String comb, int R, int C) {
	int[][] mat = new int[R][C];
	String[] cells = comb.trim().split(" ");
	//System.out.println(cells.length);
	
	for (int i = 0; i <= R - 1; i++) {
	    for (int j = 0; j <= C - 1; j++) {
		mat[i][j] = 0;
	    }
	}
	for (int i = 0; i <= cells.length - 1; i++) {
	    int cell = new Integer(cells[i]).intValue() - 1;
	    int row = cell / C;
	    int col = cell % C;
	    mat[row][col] = 1;
	}

	int result = 0;
	for (int i = 0; i <= R - 1; i++) {
	    for (int j = 0; j <= C - 1; j++) {
		if (j < C - 1) {
		    if (mat[i][j] == 1 && mat[i][j + 1] == 1) {
			result++;
		    }
		}
		if (i < R - 1) {
		    if (mat[i][j] == 1 && mat[i + 1][j] == 1) {
			result++;
		    }
		}
	    }
	}
	//System.out.println(comb);
	//print(mat);
	//System.out.println();
	return result;
    }

    void print(int[][] mat) {
	int R = mat.length;
	int C = mat[0].length;

	for (int i = 0; i <= R - 1; i++) {
	    for (int j = 0; j <= C - 1; j++) {
		//System.out.print(mat[i][j] + " ");
	    }
	    //System.out.println();
	}
    }

    List<String> combinations(int a, int b) {
	List<String> result = new ArrayList<String>();
	combinations(a, b, "", result);
	return result;
    }

    void combinations(int a, int b, String s, List<String> result) {
	if (b == 0) {
	    result.add(s);
	    // System.out.println(s);
	} else if (a < b) {
	    return;
	}
	else {
	    combinations(a - 1, b - 1, s + " " + Integer.toString(a), result);
	    combinations(a - 1, b, s, result);
	}
    }
}