public class QuickSort {
    public static void main(String[] args) {
	new QuickSort().start(); 
    }
    
    public void start() {
	int arr[] = {23, 675, 1 ,6, 12, 9, 1, 435, 6, 567};
	quickSort(arr, 0, 9);
	display(arr);
    }

    public void quickSort(int[] arr, int lower_index, int upper_index) {
	if(lower_index < upper_index) {
	    int index = placeInRightPosition(arr, lower_index, upper_index);
	    quickSort(arr, lower_index, index - 1);
	    quickSort(arr, index + 1, upper_index);
	}
    }

    public int placeInRightPosition(int[] arr, int pivot_index, int upper_index) {
	int lower_index = pivot_index + 1;
	int org_upper_index = upper_index;
	while (lower_index < upper_index) {
	    while (lower_index < org_upper_index && arr[pivot_index] >= arr[lower_index]) {
		lower_index++;
	    }
	    while (upper_index > (pivot_index + 1) && arr[pivot_index] <= arr[upper_index]) {
		upper_index--;
	    }
	    if (lower_index < upper_index) {
		int t = arr[lower_index];
		arr[lower_index] = arr[upper_index];
		arr[upper_index] = t;
	    }
	}
	if (arr[upper_index] < arr[pivot_index]) {
	    int t = arr[upper_index];
	    arr[upper_index] = arr[pivot_index];
	    arr[pivot_index] = t;
	    pivot_index = upper_index;
	}
	return pivot_index;
    }

    public void display(int[] arr) {
	for (int i = 0; i < arr.length; i++) {
	    System.out.println(arr[i]);
	}
    }
}
