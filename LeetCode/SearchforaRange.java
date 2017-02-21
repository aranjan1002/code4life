public class SearchforaRange {
    public int[] searchRange(int[] A, int target) {
        int left = binarySearchLeft(A, target, 0, A.length - 1);
        int right = binarySearchRight(A, target, 0, A.length - 1);
        
        int[] result = {left, right};
        return result;
    }
    
    int binarySearchLeft(int[] A, int target, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            
            if (target < A[mid]) {
                int new_right = Math.max(left, mid - 1);
                return binarySearchLeft(A, target, left, new_right);
            } else if (target > A[mid]) {
                return binarySearchLeft(A, target, mid + 1, right);
            } else {
                if (mid > 0) {
                    if (A[mid - 1] == target) {
                        int new_right = Math.max(left, mid - 1);
                        return binarySearchLeft(A, target, left, new_right);
                    } else {
                        return mid;
                    }
                } else {
                    return 0;
                }
            }
        } else if (A[left] == target) {
            return left;
        } else {
            return -1;
        }
    }
    
    int binarySearchRight(int[] A, int target, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            
            if (target < A[mid]) {
                int new_right = Math.max(left, mid - 1);
                return binarySearchRight(A, target, left, new_right);
            } else if (target > A[mid]) {
                return binarySearchRight(A, target, mid + 1, right);
            } else {
                if (mid < A.length - 1) {
                    if (A[mid + 1] == target) {
                        return binarySearchRight(A, target, mid + 1, right);
                    } else {
                        return mid;
                    }
                } else {
                    return A.length - 1;
                }
            }
        } else if (A[left] == target) {
            return left;
        } else {
            return -1;
        }
    }
}