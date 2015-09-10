public class MergeSort {
    public static void main(String[] args) {
	new MergeSort().start();
    }

    public void start() {
	int x[] = {23, 45, 232, 43, 1, 64, 1, 76, 76, 3};
	mergeSort(x, 0, 9);
	display(x);
    }

    public void mergeSort(int[] arr, int l, int u) {
	if (l < u) {
	    int mid = (l + u) / 2;
	    mergeSort(arr, l, mid);
	    mergeSort(arr, mid + 1, u);
	    System.out.println(l + " " + mid + " " + u);
	    merge(arr, l, mid, u);
	}
    }

    public void merge(int[] arr, int l, int m, int u) {
	int[] arr2 = new int[u - l + 1];
	int p1 = l;
	int p2 = m + 1;
	for (int i = l; i <= u; i++) {
	    if (p1 <= m && (p2 > u || arr[p1] <= arr[p2])) {
		arr2[i - l] = arr[p1++];
	    }
	    else if (p2 <= u && (p1 > m || arr[p2] < arr[p1])) {
		arr2[i - l] = arr[p2++];
	    }
	}
	for (int i = l; i <= u; i++) {
	    arr[i] = arr2[i-l];
	}
    }

    public void display(int[] arr) {
	for (int i = 0; i < arr.length; i++) {
	    System.out.println(arr[i]);
	}
    }
}