import java.io.*;
import java.lang.*;
import java.util.*;

public class Solution {
    public static void main(String[] args) {
	int[] num = {0, 0, 0};
	System.out.println(new Solution().threeSum(num));
    }

    public List<List<Integer>> threeSum(int[] num) {
        List <List<Integer>> result = new ArrayList<List<Integer>>();
        num = sort(num);

        int head = 0;
        int tail = num.length - 1;
        int sum = 0, temp = 0;
        List<Integer> temp_list;
        
        while (head < num.length && num[head] <= 0) {
	    // System.out.println(head);
	    tail = num.length - 1;
            while (tail > 0 && num[tail] >= 0) {
                sum = num[head] + num[tail];
		if (find((-1) * sum, num, head + 1, tail - 1) == true) {
                    temp_list = new ArrayList<Integer>();
                    temp_list.add(new Integer(num[head]));
                    temp_list.add(new Integer(sum * (-1)));
                    temp_list.add(new Integer(num[tail]));
                    //System.out.println("(" + num[head] + ", -" + sum + ", " + num[tail] + ")");
                    result.add(temp_list);
                } 
		temp = num[tail];
                while (tail > 0 && num[tail] == temp) {
                    tail--;
                }
            }
            temp = num[head];
            while (head < num.length && num[head] == temp) {
                head++;
            }
        }
        return result;
    }
    
    int[] sort(int[] num) {
        int cmp = 0;
        int temp = 0;
        for (int i = 0; i < num.length; i++) {
            for (int j = i + 1; j < num.length; j++) {
                if (num[i] > num[j]) {
                    temp = num[i];
                    num[i] = num[j];
                    num[j] = temp;
                }
            }
        }
        return num;
    }
    
    boolean find(int n, int[] num, int idx1, int idx2) {
        int i = 0;
        for (i = idx1; i <= idx2; i++) {
            if (num[i] == n) {
                return true;
            } 
        }
        return false;
    }
}
