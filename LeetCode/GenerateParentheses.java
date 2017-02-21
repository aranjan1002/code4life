public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<String>();
        if (n == 1) {
            String str = "()";
            result.add(str);
        } else {
            result = addOnePair(generateParenthesis(n - 1));
        }
        
        return result;
    }
    
    public List<String> addOnePair(List<String> par) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i <= par.size() - 1; i++) {
            String curr_par = par.get(i);
            for (int k = 0; k <= curr_par.length(); k++) {
                for (int j = k + 1; j <= curr_par.length() + 1; j++) {
                    String new_par = addToPar(curr_par, k, j);
                    if (result.contains(new_par) == false) {
                        result.add(new_par);
                    }
                }
            }
        }
        
        return result;
    }
    
    public String addToPar(String curr_par, int i, int j) {
        String result = new String();
        if (i == 0) {
            result = "(" + curr_par;
        } else {
            result = curr_par.substring(0, i) + "(" + curr_par.substring(i, curr_par.length());
        }
        
        if (j == curr_par.length() + 1) {
            result = result + ")";
        } else {
            String temp_result = result.substring(0, j + 1) + ")";
            result = temp_result + result.substring(j + 1, result.length());
        }
        
        return result;
    }
}