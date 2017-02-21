public class BasicCalculatorII {
    public int calculate(String s) {
        Stack<Integer> stack = new Stack<Integer>();
        char sign = '+';
        int idx = 0;
        int sum = 0;
        int num = 0;
        while (idx <= s.length() - 1) {
            while (idx <= s.length() - 1 && isDigit(s.charAt(idx)) == true) {
                num = num * 10 + (s.charAt(idx) - '0');
                idx++;
            }
            if ((idx <= s.length() - 1 && s.charAt(idx) != ' ') || idx >= s.length() - 1) {
                if (sign == '+') {
                    stack.push(num);
                } else if (sign == '-') {
                    stack.push((-1) * num);
                } else if (sign == '*') {
                    stack.push(num * stack.pop());
                } else if (sign == '/') {
                    stack.push(stack.pop() / num);
                }
                num = 0;
                if (idx < s.length()) {
                    sign = s.charAt(idx++);
                }
            } else {
                idx++;
            }
        }
        while (stack.isEmpty() == false) {
            sum += stack.pop();
        }
        return sum;
    }
    
    boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }
}