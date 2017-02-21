public class ZigZagConversion {
    public String convert(String s, int nRows) {
        List<String> zigzag = new ArrayList<String>();
        
        int idx = 0;
        
        for (int i = 0; i <= nRows - 1; i++) {
            zigzag.add(new String());
        }
        while (idx <= s.length() - 1) {
            for (int i = 0; idx <= s.length() - 1 && i <= nRows - 1; i++) {
                String curr_str = zigzag.get(i);
                zigzag.set(i, curr_str + Character.toString(s.charAt(idx++)));
            }
            
            for (int i = nRows - 2; i >= 1 && idx <= s.length() - 1; i--) {
                String curr_str = zigzag.get(i);
                zigzag.set(i, curr_str + Character.toString(s.charAt(idx++)));
            }
        }
        
        String result = new String();
        
        for (int i = 0; i <= nRows - 1; i++) {
            result = result + zigzag.get(i);
        }
        
        return result;
    }
}