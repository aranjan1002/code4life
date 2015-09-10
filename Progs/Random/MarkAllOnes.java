import java.io.*;

class MarkAllOnes {
    public static void main(String[] args) 
    throws Exception {
	new MarkAllOnes().start();
    }

    public void start() throws Exception {
	InputStreamReader ir = new InputStreamReader(System.in);
	BufferedReader br = new BufferedReader(ir);
	String str;
	str = br.readLine();
	String[] y = str.split(" ");
        r = Integer.parseInt(y[0]);
	c = Integer.parseInt(y[1]);
	arr = new int[r][c];

	for(int i=0; i<r; i++) {
	    str=br.readLine();
	    String[] nos = str.split(" ");
	    for(int j=0; j<c; j++) {
		arr[i][j]=Integer.parseInt(nos[j]);
	    }
	}
	
	print(arr);
	markRowCols(0, 0);
	print(arr);
    }
    int r,c;
    int arr[][];
    void markRowCols(int i, int j) {
	int temp = arr[i][j];
	if(arr[i][j]==1) {
	    arr[i][j]=2;
	}
	if(j<c-1) {
	    markRowCols(i,j+1);
	}
	else if(i < r-1) {
	    markRowCols(i+1, 0);
	}
	if(temp == 1) {
	    for(int k=0; k<r; k++) {
		if(arr[k][j]==2 && k!=i) break;
		arr[k][j]=1;
	    }
	    for(int l=0; l<c; l++) {
		if(arr[i][l]==2 && l!=j) break;
		arr[i][l]=1;
	    }
	    arr[i][j]=1;
	}
    }
    
    void print(int[][] arr) {
	for(int i=0; i<r; i++) {
	    for(int j=0; j<r; j++) {
		System.out.print(arr[i][j] + " ");
	    }
	    System.out.println();
	}
    }
}
