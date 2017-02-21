public class NextPermutation {
    public void nextPermutation(int[] num) {
        if (num.length <= 1) {
            return;
        }
        
        int idx_to_change = findIdxToChange(num);
        if (idx_to_change == -1) {
            reverse(num, 0, num.length - 1);
            return;
        }
        
        int next_big_number = findNextBigNumber(num, idx_to_change);
        int temp = num[idx_to_change];
        num[idx_to_change] = next_big_number;
        sort_rest(num, idx_to_change + 1, temp, next_big_number);
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
        
        for (int i = idx + 2; i <= num.length - 1; i++) {
            if (num[i] > num1 && num[i] < num2) {
                num2 = num[i];
            }
        }
        
        return num2;
    }
    
    void sort_rest(int[] num, int idx, int replace_with, int num_to_replace) {
        int n = num.length - 1;
        for (int i = 0; idx + i < n - i; i++) {
            int temp = num[idx + i];
            num[idx + i] = num[n - i];
            num[n - i] = temp;
        }
        
        for (int i = idx; i <= n; i++) {
            if (num[i] == num_to_replace) {
                num[i] = replace_with;
                break;
            }
        }
    }
 }