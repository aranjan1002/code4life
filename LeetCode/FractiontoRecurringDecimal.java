public class FractiontoRecurringDecimal {
    public String fractionToDecimal(int numerator, int denominator) {
        if (denominator == 0 || numerator == 0) {
            return "0";
        }
        boolean add_minus = false;
        if ((numerator < 0 && denominator > 0) ||
            (denominator < 0 && numerator > 0)) {
                add_minus = true;
        }
        numerator = Math.abs(numerator);
        denominator = Math.abs(denominator);
    
        String result = new String();        
        long quotient = Math.abs((long)numerator / (long)denominator);
        String fractional_part = getFractionalPart((long) Math.abs(numerator), (long) Math.abs(denominator));
        if (numerator % denominator == 0) {
            result = Long.toString(quotient);
        } else {
            result = Long.toString(quotient) + "." + fractional_part;
        }
        
        if (add_minus == true) {
            result = "-" + result;
        }
        
        return result;
    }
    
    public String getFractionalPart(long numerator, long denominator) {
        String result = new String();
        
        Map<Long, Integer> mod_to_index = new HashMap<Long, Integer>();
        int index = 0;
        while(true) {
            long mod = numerator % denominator;
            if (mod == 0) {
                return result;
            }
            Long mod_integer = new Long(mod);
            if (mod_to_index.containsKey(mod_integer) == true) {
                Integer idx = mod_to_index.get(mod_integer);
                result = insertParanthesis(result, idx);
                return result;
            }
            numerator = mod * 10;
            long quot = Math.abs(numerator / denominator);
            result += Long.toString(quot);
            mod_to_index.put(mod_integer, new Integer(index));
            index++;
        }
    }
    
    String insertParanthesis(String str, Integer idx) {
        String result = new String();
        for (int i = 0; i <=  idx - 1; i++) {
            result += Character.toString(str.charAt(i));
        }
        
        result += "(";
        for (int i = idx; i <= str.length() - 1; i++) {
            result += Character.toString(str.charAt(i));
        }
        
        result += ")";
        
        return result;
    }
    
}