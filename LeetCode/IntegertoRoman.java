public class IntegertoRoman {
    public String intToRoman(int num) {
        init();
        String s = Integer.toString(num);
        String result = calcThousands(s);
        int len = s.length();
        if (s.length() >= 3) {
            result += hundredsMap.get(new Character(s.charAt(len - 3)));
        }
        if (s.length() >= 2) {
            result += tensMap.get(new Character(s.charAt(len - 2)));
        }
        if (s.length() >= 1) {
            result += unitMap.get(new Character(s.charAt(len - 1)));
        }
        
        return result;
    }
    
    private String calcThousands(String s) {
        String result = "";
        if (s.length() >= 4) {
            char c = s.charAt(0);
            for (int i = 1; i <= c - 48; i++) {
                result = result + "M";
            }
        }
        
        return result;
    }
    
    void init() {
        unitMap.put(new Character('0'), new String(""));
        unitMap.put(new Character('1'), new String("I"));
        unitMap.put(new Character('2'), new String("II"));
        unitMap.put(new Character('3'), new String("III"));
        unitMap.put(new Character('4'), new String("IV"));
        unitMap.put(new Character('5'), new String("V"));
        unitMap.put(new Character('6'), new String("VI"));
        unitMap.put(new Character('7'), new String("VII"));
        unitMap.put(new Character('8'), new String("VIII"));
        unitMap.put(new Character('9'), new String("IX"));
        
        tensMap.put(new Character('0'), new String(""));
        tensMap.put(new Character('1'), new String("X"));
        tensMap.put(new Character('2'), new String("XX"));
        tensMap.put(new Character('3'), new String("XXX"));
        tensMap.put(new Character('4'), new String("XL"));
        tensMap.put(new Character('5'), new String("L"));
        tensMap.put(new Character('6'), new String("LX"));
        tensMap.put(new Character('7'), new String("LXX"));
        tensMap.put(new Character('8'), new String("LXXX"));
        tensMap.put(new Character('9'), new String("XC"));
        
        hundredsMap.put(new Character('0'), new String(""));
        hundredsMap.put(new Character('1'), new String("C"));
        hundredsMap.put(new Character('2'), new String("CC"));
        hundredsMap.put(new Character('3'), new String("CCC"));
        hundredsMap.put(new Character('4'), new String("CD"));
        hundredsMap.put(new Character('5'), new String("D"));
        hundredsMap.put(new Character('6'), new String("DC"));
        hundredsMap.put(new Character('7'), new String("DCC"));
        hundredsMap.put(new Character('8'), new String("DCCC"));
        hundredsMap.put(new Character('9'), new String("CM"));
    }
    
    Map<Character, String> unitMap = new HashMap<Character, String>();
    Map<Character, String> tensMap = new HashMap<Character, String>();
    Map<Character, String> hundredsMap = new HashMap<Character, String>();
}