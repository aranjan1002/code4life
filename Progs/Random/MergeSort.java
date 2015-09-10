import java.util.*;

class MergeSort {
    public static void main(String[] args) {
	int[] array = {};//{34,5,3,3,5,3234,24,53};
	new MergeSort().mergeSort(array);
	System.out.println(Arrays.toString(array));
    }

    void mergeSort(int[] array) {
	int length = array.length;
	mergeSort(array, 0, array.length - 1);
    }

    void mergeSort(int[] array, int start, int end) {
	if (start < end) {
	    int mid = ((start + end) / 2) + 1;
	    mergeSort(array, start, mid - 1);
	    mergeSort(array, mid, end);
	    merge(array, start, mid, end);
	}
    }

    void merge(int[] array, int start, int mid, int end) {
	int[] temp = new int[end - start + 1];

	int i = start;
	int j = mid;
	int c = 0;
	
	while(i < mid || j <= end) {
	    if (i >= mid) {
		temp[c] = array[j++];
	    }
	    else if (j > end) {
		temp[c] = array[i++];
	    }
	    else if (array[i] >= array[j]) {
		temp[c] = array[i++];
	    }
	    else {
		temp[c] = array[j++];
	    }
	    c++;
	}
	
	for (c = 0; c < temp.length; c++) {
	    array[start++] = temp[c];
	}
    }
}