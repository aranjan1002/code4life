class LongestConsecutiveCharsLength {
    public static void main(String[] args) {
	System.out.println(new LongestConsecutiveCharsLength().solve("axby"));
    }

    int solve(String str) {
	if (str == null) return 0;

	int result = 1, curr_result = 1;

	for (int i = 0; i <= str.length() - 2; i++) {
	    while (i + 1 <= str.length() - 1 && str.charAt(i + 1) == 
		   str.charAt(i) + 1) {
		curr_result++;
		i++;
	    }
	    
	    if (curr_result > 1) {
		if (curr_result > result) {
		    result = curr_result;
		}

		curr_result = 1;
	    }
	}
	return result;
    }	
}
