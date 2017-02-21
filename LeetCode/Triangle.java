public class Triangle {
    public int minimumTotal(List<List<Integer>> triangle) {
        if (triangle.size() == 0) {
            return 0;
        }
        List<List<Integer>> dp = new ArrayList<List<Integer>>();
        dp.add(triangle.get(0));
        
        for (int i = 1; i <= triangle.size() - 1; i++) {
            List<Integer> l = triangle.get(i);
            List<Integer> dp_l = dp.get(i - 1);
            List<Integer> dp_new = new ArrayList<Integer>();
            dp_new.add(l.get(0) + dp_l.get(0));
            for (int j = 1; j <= l.size() - 2; j++) {
                dp_new.add(l.get(j) + Math.min(dp_l.get(j), dp_l.get(j - 1)));
            }
            dp_new.add(l.get(l.size() - 1) + dp_l.get(dp_l.size() - 1));
            dp.add(dp_new);
        }
        
        int max = Integer.MAX_VALUE;
        List<Integer> last = dp.get(dp.size() - 1);
        for (int i = 0; i <= last.size() - 1; i++) {
            if (max > last.get(i)) {
                max = last.get(i);
            }
        }
        
        return max;
    }
}