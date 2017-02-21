public class GrayCode {
    public List<Integer> grayCode(int n) {
        Integer[] result = new Integer[(int) Math.pow(2, n)];
        result[0] = 0;
        
        for (int i = 1; i <= n; i++) {
            int pow = (int) Math.pow(2, i - 1);
            for (int j = pow - 1; j >=0; j--) {
                result[pow + (pow - 1 - j)] = pow + result[j];
            }
        }
        
        return Arrays.asList(result);
    }
}