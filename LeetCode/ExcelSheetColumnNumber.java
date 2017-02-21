public class ExcelSheetColumnNumber {
    public int titleToNumber(String s) {
        int sum = 0;
        int cnt = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            sum = sum + (s.charAt(i) - 64) * (int) Math.pow(26, cnt++);
        }
        return sum;
    }
}