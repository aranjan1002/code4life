public class KthLargestElementinanArray {
    public int findKthLargest(int[] nums, int k) {
        int rank = -1;
        int low = 0; 
        int high = nums.length - 1;
        while (rank != k) {
            rank = partition(nums, low, high);
            //System.out.println(rank);
            if (rank < k) {
                low = rank;
            } else if (rank > k) {
                high = rank - 2;
            } else {
                return nums[rank - 1];
            }
        }
        return nums[rank - 1];
    }
    
    int partition(int[] nums, int low, int high) {
        int ptr1 = low + 1;
        int ptr2 = high;
        //System.out.println(low + " " + high);
        
        while (ptr1 <= ptr2) {
            while (ptr1 <= ptr2 && nums[ptr1] >= nums[low]) {
                ptr1++;
            }
            
            while (ptr2 >= ptr1 && nums[ptr2] <= nums[low]) {
                ptr2--;
            }
            //System.out.println(ptr1 + " " + ptr2);
            if (ptr1 < ptr2) {
                swap(nums, ptr1, ptr2);
            }
            //System.out.println(ptr1 + " " + ptr2);
        }
        
        if (ptr2 > low) {
            swap(nums, low, ptr2);
            return ptr2 + 1;
        }
        
        return low + 1;
    }
    
    void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}