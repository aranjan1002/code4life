public class RegularExpressionMatching {
    public boolean isMatch(String s, String p) {
        Pattern pat = new Pattern();
        if (p.length() == 0) {
            if (s.length() > 0) {
                return false;
            }
            return true;
        }
        PatternType pat_type = pat.getNextPatternType(p); 
            if (pat_type == PatternType.DOT) {
                if (s.length() == 0) {
                    return false;
                } else {
                    return isMatch(s.substring(1), p.substring(1));
                }
            } else if (pat_type == PatternType.LETTER) {
                if (s.length() == 0) {
                    return false;
                } else {
                    if (s.charAt(0) != p.charAt(0)) {
                        return false;
                    }
                    return isMatch(s.substring(1), p.substring(1));
                }
            } else {
                char letter = p.charAt(0);
                boolean result = false;
                if (p.length() > 2) {
                    result = isMatch(s, p.substring(2));
                    if (result == true) {
                        return true;
                    }
                } else {
                    if (pat_type == PatternType.DOTSTAR) {
                        return true;
                    }
                    for (int i = 0; i <= s.length() - 1; i++) {
                        if (s.charAt(i) != letter) {
                            return false;
                        }
                    }
                    return true;
                }
                int i = 0;
                while (i <= s.length() - 1) {
                    if (s.charAt(i) == letter || pat_type == PatternType.DOTSTAR) {
                        result = isMatch(s.substring(i + 1), p.substring(2));
                        if (result == true) {
                            return true;
                        }
                    } else {
                        break;
                    }
                    i++;
                }
                
                return false;
            }
        
    }
    
    class Pattern {
        PatternType getNextPatternType(String p) {
            if (p.charAt(0) == '.') {
                if (p.length() == 1 || p.charAt(1) != '*') {
                    return PatternType.DOT;
                } else {
                    return PatternType.DOTSTAR;
                }
            }
            if (p.length() == 1) {
                return PatternType.LETTER;
            } else {
                if (p.charAt(1) == '*') {
                    return PatternType.STAR;
                } else {
                    return PatternType.LETTER;
                }
            }
        }
    }
    
    enum PatternType {
        LETTER,
        DOT,
        STAR,
        DOTSTAR
    }
}