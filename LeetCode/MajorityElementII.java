public class MajorityElementII {
    public List<Integer> majorityElement(int[] nums) {
        int a, b, n1 = 1, n2 = 1;
        List<Integer> result = new ArrayList<Integer>();
        if (nums.length == 0) {
            return result;
        }
        if (nums.length == 1) {
            result.add(nums[0]);
            return result;
        }
        a = nums[0];
        if (a != nums[1]) {
            b = nums[1];
        } else {
            n1++;
            b = -1;
            n2 = 0;
        }
        for (int i = 2; i <= nums.length - 1; i++) {
            if (a == nums[i]) {
                n1++; continue;
            }
            if (b == nums[i]) {
                n2++; continue;
            }
            if (n1 == 0) {
                a = nums[i]; n1++; continue;
            }
            if (n2 == 0) {
                b = nums[i]; n2++; continue;
            }
            n1--; n2--;
            System.out.println(a + " " + b + " " + n1 + " " + n2);
        }
        int cnt1 = 0, cnt2 = 0;
        for (int n : nums) {
            if (n == a) cnt1++;
            if (n == b) cnt2++;
        }
        if (cnt1 > nums.length / 3) result.add(a);
        if (cnt2 > nums.length / 3) result.add(b);
        return result;
    }
}