import java.util.Arrays;

class ReplaceSpace {
    public static void main(String[] args) {
	new ReplaceSpace().replaceSpace("dear friends ");
    }

    public void replaceSpace(String str) {
	char[] s = str.toCharArray();
	int sc = 0;
	for(int i=0; i<s.length; i++) {
	    if(s[i] == ' ') sc++;
	}
	int nl = s.length + sc*2;
	char[] s2 = new char[s.length + sc*2 + 1];
	s2[nl--] = '\0';
	for(int i=s.length - 1; i>=0; i--) {
	    if(s[i] != ' ') {
		s2[nl--] = s[i];
	    }else {
		s2[nl] = '0';
		s2[nl-1] = '2';
		s2[nl-2] = '%';
		nl = nl - 3;
	    }
	    System.out.println(s2[nl+1]);
	}
	System.out.println(Arrays.toString(s2));
    }
}
