public class Solution {
    public static void main(String[] args) {
	int[] num = {2,3,1,3,3};

	new Solution().nextPermutation(num);
    }

    public void nextPermutation(int[] num) {
        if (num.length <= 1) {
            return;
        }
        
        int idx_to_change = findIdxToChange(num);
        if (idx_to_change == -1) {
            reverse(num, 0, num.length - 1);
            return;
        }
        display(num);
        int next_big_number = findNextBigNumber(num, idx_to_change);
        int temp = num[idx_to_change];
        num[idx_to_change] = num[next_big_number];
        num[next_big_number] = temp;
	display(num);
	System.ou
        sort_rest(num, idx_to_change + 1);
    }
    
    int findIdxToChange(int[] num) {
        int idx = num.length - 2;
        
        while(idx >= 0) {
            if (num[idx] < num[idx + 1]) {
                return idx;
            }
            idx--;
        }
        
        return -1;
    }
    
    void reverse(int[] num, int i, int j) {
        for (int k = 0; (i + k) < (j - k); k++) {
            int temp = num[i + k];
            num[i + k] = num[j - k];
            num[j - k] = temp;
        }
    }
    
    int findNextBigNumber(int[] num, int idx) {
        int num1 = num[idx];
        int num2 = num[idx + 1];
        int new_idx = idx + 1;
        
        for (int i = idx + 2; i <= num.length - 1; i++) {
            if (num[i] > num1 && num[i] < num2) {
                num2 = num[i];
                new_idx = i;
            }
        }
        
        return new_idx;
    }
    
    void sort_rest(int[] num, int idx) {
	System.out.println(idx);
	display(num);
        int n = num.length - 1;
        for (int i = 0; idx + i < n - i; i++) {
            int temp = num[idx + i];
            num[idx + i] = num[n - i];
            num[n - i] = temp;
        }
    }

    void display(int[] num) {
	for (int i = 0; i <= num.length - 1; i++) {
	    System.out.print(num[i] + " ");
	}
	System.out.println();
    }
}