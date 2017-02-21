public class RemoveInvalidParentheses {
    public List<String> removeInvalidParentheses(String s) {
        LinkedList<String> q = new LinkedList<String>();
        HashSet<String> set = new HashSet<String>();
        List<String> result = new ArrayList<String>();
        
        q.offer(s);
        boolean found = false;
        while (q.isEmpty() == false) {
            String str = q.poll();
            
            if (isValid(str) == true) {
                    result.add(str);
                    found = true;
            }
                
            if (found == true) {
                    continue;
            }
            
            for (int i = 0; i <= str.length() - 1; i++) {
                if (str.charAt(i) != '(' && str.charAt(i) != ')') {
                    continue;
                }
                
                String newS = new String();
                newS = str.substring(0, i) + str.substring(i + 1);
                
                if (set.contains(newS) == false) {
                    set.add(newS);
                    q.offer(newS);
                }
            }
        }
        
        return result;
    }
    
    boolean isValid(String s) {
        int count = 0;
        
        for (int i = 0; i <= s.length() - 1; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                count++;
            }
            if (c == ')') {
                count--;
                if (count < 0) {
                    return false;
                }
            }
        }
        
        return (count == 0);
    }
}