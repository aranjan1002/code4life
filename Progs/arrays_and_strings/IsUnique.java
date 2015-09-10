class IsUnique {
    public static void main(String[] args) {
	System.out.println(new IsUnique().isUnique("hello world"));
	System.out.println(new IsUnique().isUnique("abcdefghij"));
    }

    public boolean isUnique(String str) {
	boolean[] flag = new boolean[255];
	for(int i=0; i<str.length(); i++) {
	    // System.out.println(str);
	    if(flag[str.charAt(i)]) {
		return false;
	    }
	    flag[str.charAt(i)] = true;
	}
	return true;
    }
}
