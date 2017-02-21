public class ValidParentheses {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();
        
        for (int i = 0; i <= s.length() - 1; i++) {
            char c = s.charAt(i);
            switch(c) {
                case '(':
                case '{':
                case '[':
                    stack.push(c);
                    break;
                case ')':
                case '}':
                case ']':
                    if (stack.isEmpty() == false) {
                        char c2 = stack.pop();
                        if (c2 != reverse(c)) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                    break;
                default:
                        break;
            }
        }
        
        return stack.isEmpty();
    }
    
    char reverse(char c) {
        switch(c) {
            case ')':
                return '(';
            case '}':
                return '{';
            case ']':
                return '[';
            default:
                return 'x';
        }
    }
}