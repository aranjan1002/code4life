public class JumpGameII {
    public int jump(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int[] min = new int[nums.length];
        boolean[] flag = new boolean[nums.length];
        LinkedList<Integer> q = new LinkedList<Integer>();
        q.push(0);
        int maxReach = 0;
        while(q.isEmpty() == false) {
            int idx = q.poll();
            if (idx == nums.length - 1) {
                return min[idx];
            }
            for (int i = maxReach + 1; i <= idx + nums[idx] ; i++) {
                //System.out.println(idx + "-" + i);
                if (i <= nums.length - 1 && flag[i] == false) {
                    flag[i] = true;
                    q.offer(i);
                    min[i] = min[idx] + 1;
                    //System.out.println((idx + i) + ":" + min[idx + i] + " " + i + " " + min[idx]);
                }
            }
            if (idx + nums[idx] > maxReach) {
                maxReach = idx + nums[idx];
            }
        }
        
        return -1;
    }
}