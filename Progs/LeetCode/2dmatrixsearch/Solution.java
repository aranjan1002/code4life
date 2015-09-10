import java.util.*;

public class Solution {
    public static void main(String[] args) {
	int[][] matrix = {{-10,-8},{-6,-5},{-2,-2},{-1,0},{3,4},{7,7},{8,9},{10,10},{11,11},{12,14},{15,16},{17,19},{20,21},{22,22},{25,27},{28,30},{32,32},{35,36}};
	System.out.println(new Solution().searchMatrix(matrix, 16));
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int row = binarySearchRow(matrix, target);
        if (row == -1) {
            return false;
        } else {
	    System.out.println(row);
            return binarySearchCol(matrix[row], target);
        }
    }
    
    int binarySearchRow(int[][] matrix, int target) {
        int row = matrix.length;
        if (row == 0) {
            return -1;
        } 
        int col = matrix[0].length;
        if (col == 0) {
            return -1;
        }
     
	return binarySearchRow(matrix, 0, row - 1, target);   
    }
    
    int binarySearchRow(int[][] matrix, int low, int high, int target) {
	System.out.println(low + " " + high);
        if (low < high) {
            int mid = (low + high) / 2;
            int val = matrix[mid][0];
	    System.out.println(val + " " + target+ " " + low + " " + high);
            if (val < target) {
                return binarySearchRow(matrix, mid + 1, high, target);
            } else if (val > target) {
                return binarySearchRow(matrix, low, Math.max(low, mid - 1), target);
            } else {
                return mid;
            }
        } else if (low == high) {
	    int n = matrix[low].length;
            if (target < matrix[low][0]) {
		return low - 1;
	    } else if (target > matrix[low][n - 1]) {
		return low + 1;
	    } else {
		return low;
	    }
        } else {
            return -1;
        }
    }
    
    boolean binarySearchCol(int[] row, int target) {
        int n = row.length;
        return binarySearchCol(row, 0, n - 1, target);
    }
    
    boolean binarySearchCol(int[] row, int low, int high, int target) {
        if (low < high) {
            int mid = (low + high) / 2;
            if (row[mid] < target) {
                return binarySearchCol(row, mid + 1, high, target);
            } else if (row[mid] > target) {
                return binarySearchCol(row, low, mid - 1, target);
            } else {
                return true;
            }
        } else {
            if (low == high) {
                return (row[low] == target);
            } else {
                return false;
            }
        }
    }
}