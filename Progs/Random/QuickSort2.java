class QuickSort2 {
    public static void main(String[] args) {
	int[] num = {};
	
	new QuickSort2().sort(num);
	print(num);
    }

    static void print(int[] num) {
	for (int i = 0; i <= num.length - 1; i++) {
	    System.out.println(num[i]);
	}
    }

    void sort(int[] num) {
	quickSort(num, 0, num.length - 1);
    }

    void quickSort(int[] num, int start, int end) {
	int idx = partition(num, start, end);
	if (idx > start) {
	    quickSort(num, start, idx - 1);
	} 
	if (idx < end) {
	    quickSort(num, idx + 1, end);
	}
    }

    int partition(int[] num, int s, int e) {
	int i = s - 1;
	int val = num[e];

	for (int j = s; j <= e - 1; j++) {
	    if (num[j] <= val) {
		i++;
		int temp = num[i];
		num[i] = num[j];
		num[j] = temp;
	    }
	}
	
	int temp = num[e];
	num[e] = num[i + 1];
	num[i + 1] = temp;
	return (i + 1);
    }
	
}
