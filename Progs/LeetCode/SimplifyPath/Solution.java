public class Solution {
    public static void main(String[] args) {
	System.out.println(new Solution().simplifyPath("/..."));
    }

    public String simplifyPath(String path) {
        String[] path_split = path.split("/");
        String[] result = new String[path_split.length];
	display(path_split);
        int idx = 0;
        
        for (int i = 0; i <= path_split.length - 1; i++) {
            String curr_dir = path_split[i];
            if (".".equals(curr_dir)) {
                continue;
            } else if ("..".equals(curr_dir)) {
                if (idx > 0) {
                    idx--;
                }
            } else if (curr_dir.length() > 0) {
                result[idx++] = curr_dir;
		System.out.println(idx + " " + curr_dir.length());
            }
        }
        
        String result_str = new String();
        
        for (int i = 0; i <= idx - 1; i++) {
            result_str += "/" + result[i];
        }
        
        if (result_str.length() > 1 && result_str.charAt(result_str.length() - 1) == '/') {
            return result_str.substring(0, result_str.length() - 1);
        }
        
        if (result_str.length() == 0) {
            return "/";
        }
        return result_str;
    }

    void display(String[] s) {
	for (int i = 0; i <= s.length - 1; i++) {
	    System.out.println(s[i] + ":");
	}
    }
}