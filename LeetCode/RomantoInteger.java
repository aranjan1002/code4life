public class RomantoInteger {
    public int romanToInt(String s) {
        ValueAndIndex thousands = calcThousands(s);
        ValueAndIndex hundreds = calc(s, thousands.index, 'D', 'C', 'M');
        ValueAndIndex tens = calc(s, hundreds.index, 'L', 'X', 'C');
        ValueAndIndex unit = calc(s, tens.index, 'V', 'I', 'X');
        
        return ((1000 * thousands.value) + (100 * hundreds.value) + (10 * tens.value) + unit.value);
    }
    
    ValueAndIndex calcThousands(String s) {
        int cnt = 0;
        for (int i = 0; i <= s.length() - 1; i++) {
            if (s.charAt(i) == 'M') {
                cnt++;
            } else {
                return new ValueAndIndex(cnt, cnt);
            }
        }
        return new ValueAndIndex(cnt, cnt);
    }
    
    ValueAndIndex calc(String s, int index, char bigLetter, char smallLetter, char biggerLetter) {
        if (index > s.length() - 1) {
            return new ValueAndIndex(0, index);
        }
        int cnt = 0;
        if (index + 1 <= s.length() - 1) {
            if (s.charAt(index) == smallLetter && s.charAt(index + 1) == bigLetter) {
                return new ValueAndIndex(4, index + 2);
            } else if (s.charAt(index) == smallLetter && s.charAt(index + 1) == biggerLetter) {
                return new ValueAndIndex(9, index + 2);
            }
        }
        
        if (s.charAt(index) == bigLetter) {
            cnt = 5;
            index++;
        } 
        while(index <= s.length() - 1 && s.charAt(index) == smallLetter) {
            index++;
            cnt++;
        }
            
        return new ValueAndIndex(cnt, index);
    }
    
    class ValueAndIndex {
        int value = 0;
        int index = 0;
        
        ValueAndIndex(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }
}