public class ClimbingStairs {
    public int climbStairs(int n) {
        mem = new int[n];
        return climb_mem(n);
        }
    
    public int climb_mem(int n) {
        if (n == 1 || n == 2 || n == 0) {
            return n;
        }
        
        if (mem[n - 1] == 0) {
            mem[n - 1] = climb_mem(n - 1);
        } 
        if (mem[n - 2] == 0) {
            mem[n - 2] = climb_mem(n - 2);
        }
        return (mem[n-1] + mem[n-2]);
    
    }
    
    int[] mem;
}