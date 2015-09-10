public class RemoveDupInPlace {
    public static void main(String[] args) {
        char[] arr = {'a', 'c', 'd', 'f', 'g', 'e'};
        
        int tail = 0;
        int i, j;
        for (i = 0; i <= arr.length - 1; i++) {
            for (j = 0; j <= arr.length - 1; j++) {
                if (arr[i] == arr[j] && i != j) {
                    break;
                }
            }
            if (j == arr.length) {
                char temp = arr[tail];
                arr[tail++] = arr[i];
                arr[i] = temp;
            }     
        }
	if (tail <= arr.length - 1) {
	    arr[tail] = 0;
	}
        for (i = 0; i <= arr.length - 1; i++) {
            System.out.println(arr[i]);
        }
    }
}