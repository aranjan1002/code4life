public class IntegertoEnglishWords {
    public String numberToWords(int num) {
        if (num == 0) return "Zero";
        init();
        String s = reverse(Integer.toString(num));
        
        String firstThree = getThree(s, 0);
        String result = getSolution(firstThree);
        
        String secondThree = getThree(s, 3);
        if (secondThree.equals("") != true) {
            String result2 = getSolution(secondThree);
            if (result2.equals("") != true) {
                if (result.equals("") != true) {
                    result = " " + result;
                }
                result = result2 + " Thousand" + result;
            }
            String thirdThree = getThree(s, 6);
            if (thirdThree.equals("") != true) {
                String result3 = getSolution(thirdThree);
                if (result3.equals("") != true) {
                    if (result.equals("") != true) {
                        result = " " + result;
                    }
                    result = result3 + " Million" + result;
                }
                if (s.length() == 10) {
                    if (result.equals("") != true) {
                        result = " " + result;
                    }
                    result = unitMap.get(new String(Character.toString(s.charAt(9)))) + " Billion" + result;
                }
            }
        }
        
        return result;
    }
    
    String getSolution(String s) {
        String result = new String();
        s = trim(s);
        if (s.length() == 0) {
            return "";
        }
        if (s.length() == 1) {
            return getSolutionOneLength(s);
        } else if (s.length() == 2) {
            return getSolutionTwoLength(s);
        } else {
            result = getSolutionOneLength(Character.toString(s.charAt(0)));
            String twoLenSol = getSolutionTwoLength(s.substring(1));
            result = result + " Hundred";
            if (twoLenSol.equals("") != true) {
                result = result + " " + twoLenSol;
            }
        }
        
        return result;
    }
    
    String getSolutionOneLength(String s) {
        if(unitMap.containsKey(s)) 
            return unitMap.get(s);
        return "";
    }
    
    String getSolutionTwoLength(String s) {
        s = trim(s);
        if (s.length() == 1) {
            return getSolutionOneLength(s);
        } else if (s.length() == 0) {
            return "";
        }
        String result = new String();
        if (teenMap.containsKey(s)) {
            return teenMap.get(s);
        }
        if (tenMap.containsKey(Character.toString(s.charAt(0))) == true) {
            result = tenMap.get(Character.toString(s.charAt(0)));
        }
        String unitSolution = getSolutionOneLength(Character.toString(s.charAt(s.length() - 1)));
        if (unitSolution.equals("") != true) {
            result = result + " " + unitSolution;
        }
        return result;
    }
    
    String reverse(String str) {
        String result = new String();
        for (int i = str.length() - 1; i >= 0; i--) {
            result = result + Character.toString(str.charAt(i));
        }
        return result;
    }
    
    String getThree(String s, int idx) {
        if (idx >= s.length()) {
            return "";
        }
        String result = new String();
        for (int i = idx; i < s.length() && i < idx + 3; i++) {
            result = result + Character.toString(s.charAt(i));
        }
        
        result = reverse(result);
        
        return result;
    }
    
    String trim(String result) {
        for (int i = 0; i <= result.length() - 1; i++) {
            if (result.charAt(i) != '0') {
                return result.substring(i);
            }
        }
        return "";
    }
    
    private void init() {
        unitMap.put("1", "One");
        unitMap.put("2", "Two");
        unitMap.put("3", "Three");
        unitMap.put("4", "Four");
        unitMap.put("5", "Five");
        unitMap.put("6", "Six");
        unitMap.put("7", "Seven");
        unitMap.put("8", "Eight");
        unitMap.put("9", "Nine");
        
        tenMap.put("1", "Ten");
        tenMap.put("2", "Twenty");
        tenMap.put("3", "Thirty");
        tenMap.put("4", "Forty");
        tenMap.put("5", "Fifty");
        tenMap.put("6", "Sixty"); 
        tenMap.put("7", "Seventy");
        tenMap.put("8", "Eighty");
        tenMap.put("9", "Ninety");
        
        teenMap.put("11", "Eleven");
        teenMap.put("12", "Twelve");
        teenMap.put("13", "Thirteen");
        teenMap.put("14", "Fourteen");
        teenMap.put("15", "Fifteen"); 
        teenMap.put("16", "Sixteen");
        teenMap.put("17", "Seventeen");
        teenMap.put("18", "Eighteen");
        teenMap.put("19", "Nineteen");
    }
    
    HashMap<String, String> unitMap = new HashMap<String, String>();
    HashMap<String, String> tenMap = new HashMap<String, String>();
    HashMap<String, String> teenMap = new HashMap<String, String>();
}