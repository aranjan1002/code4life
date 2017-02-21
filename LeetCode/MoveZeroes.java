public class MoveZeroes {
    public void moveZeroes(int[] nums) {
        if (nums.length == 0) {
            return;
        }
        int ptr1 = 0;
        int ptr2 = 0;
        while (ptr1 <= nums.length - 1 && nums[ptr1] != 0) {
            ptr1++;
        }
        
        ptr2 = ptr1 + 1;
        while (ptr2 <= nums.length - 1) {
            if (nums[ptr2] != 0) {
                nums[ptr1++] = nums[ptr2];
                nums[ptr2] = 0;
            } 
            ptr2++;
        }
    }
}