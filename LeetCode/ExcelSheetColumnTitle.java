public class ExcelSheetColumnTitle {
    public String convertToTitle(int n) {
        String result = new String();
        if (n == 0) {
            return result;
        }
        while (n > 0) {
            int rem = n % 26;
            n = n / 26;
            result = addToResult(result, rem);
            if (rem == 0) {
                n--;
            }
        }
        
        return result;
    }
    
    String addToResult(String res, int i) {
        if (i == 0) {
            i = 26;
        }
        res = Character.toString((char) (64 + i)) + res;
        return res;
    }
}