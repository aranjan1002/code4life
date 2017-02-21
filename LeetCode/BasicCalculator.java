public class BasicCalculator {
public int calculate(String s) {
    if (s == null || s.length() == 0) return 0;
    return rec(s);
}
public int rec(String s){
    int curNum = 0;
    int preNum = 0;
    char oper = ' ';
    int i = 0;
    while (i < s.length()){
        char c = s.charAt(i);
        if (c == ' ') {
            i++;
            continue;
        }
        else if (c <= '9' && c >= '0') {
            curNum = curNum * 10 + c - '0';
            i++;
            continue;
        }
        else if (c == '+' || c == '-'){
            if (oper != ' '){
                if (oper == '+') preNum = curNum + preNum;
                else preNum = preNum - curNum;
            }
            else preNum = curNum;
            curNum = 0;
            oper = c;
            i++;
        }
        else {
            int index = i + 1;
            int count = 0;
            while (s.charAt(index) != ')' || count > 0) {
                if (s.charAt(index) == '(') count++;
                if (s.charAt(index) == ')') count--;
                index++;
            }
            curNum = rec(s.substring(i + 1, index));
            i = index + 1;
        }
    }
    if (oper == ' ') return curNum;
    if (oper == '+') return preNum + curNum;
    else return preNum - curNum;
}
}