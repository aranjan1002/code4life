/*asdfasd
 */

public class QuickSort1 {
    public static void main(String[] args) {
	new QuickSort1().start();
    }

    public void start() {
	int x[] = {23, 5, 2, 54, 642, 12, 54, 5753, 11, 32};
	quickSort(x, 0, 9);
	display(x);
    }

    public void quickSort(int[] x, int l, int u) {
	if (l >= u) {
	    return;
	}
	int m = l;
	for (int i = l + 1; i <= u; i++) {
	    if (x[l] > x[i]) {
		int temp = x[++m];
		x[m] = x[i];
		x[i] = temp;
	    }
	}
	int temp = x[m];
	x[m] = x[l];
	x[l] = temp;
	quickSort(x, l, m - 1);
	quickSort(x, m + 1, u);
    }

    public void display(int[] arr) {
	for (int i = 0; i < arr.length; i++) {
	    System.out.print(arr[i] + " ");
	}
	System.out.println();
    }
}
