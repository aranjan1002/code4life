public class EvaluateReversePolishNotation {
    public int evalRPN(String[] tokens) {
        Stack s = new Stack(tokens.length);
        
        for (int i = 0; i <= tokens.length - 1; i++) {
            String str = tokens[i];
            if (isOperator(str) == false) {
                s.push(new Integer(str).intValue());
            } else {
                int b = s.pop();
                int a = s.pop();
                s.push(performOperation(str, a, b));
            }
        }
        
        return s.pop();
    }
    
    boolean isOperator(String s) {
        for (int i = 0; i <= operators.length - 1; i++) {
            if (operators[i].equals(s) == true) {
                return true;
            }
        }
        return false;
    }
    
    int performOperation(String op, int a, int b) {
        if ("+".equals(op)) {
            return a + b;
        } else if ("-".equals(op)) {
            return a - b;
        } else if ("*".equals(op)) {
            return a * b;
        } else {
            if (b == 0) {
                return 0;
            }
            return a / b;
        }
    }
    
    String[] operators = {"+", "-", "*", "/"};
    
    class Stack {
        Stack(int size) {
            stack = new int[size];
        }
        
        void push(int val) {
            stack[idx++] = val;
        }
        
        int pop() {
            return stack[--idx];
        }
        
        int idx = 0;
        int[] stack;
    }
}