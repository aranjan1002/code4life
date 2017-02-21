public class Searcha2DMatrix {
    public boolean searchMatrix(int[][] matrix, int target) {
        int row = binarySearchRow(matrix, target);
        if (row == -1 || row >= matrix.length) {
            return false;
        } else {
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
        if (low < high) {
            int mid = (low + high) / 2;
            int val = matrix[mid][0];
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