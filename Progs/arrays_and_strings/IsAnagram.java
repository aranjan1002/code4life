class IsAnagram {
    public static void main(String[] args) {
	if(args.length == 2) 
	    System.out.println(new IsAnagram().isAnagram(args[0], args[1]));
    }

    public boolean isAnagram(String str1, String str2) {
	char[] s1 = str1.toCharArray();
	char[] s2 = str2.toCharArray();
	
	for(int i=0; i<26; i++) {
	    if(countLetter(i, s1) != countLetter(i, s2)) {
		return false;
	    }
	}
	return true;
    }

    public int countLetter(int l, char[] s2) {
	int cnt = 0;
	for (int i=0; i<s2.length; i++) {
	    if(s2[i] == 65 + l || s2[i] == 97+l) cnt++;
	}
	return cnt;
    }
}
