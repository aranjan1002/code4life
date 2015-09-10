public class CheckUnique {
    public static void main(String[] args) {
	new CheckUnique().start();
    }

    public void start() {
	System.out.println(checkUnique("abcaersd"));
	System.out.println(checkUnique("asdwerkl"));
    }

    public boolean checkUnique(String str) {
	int flag_bits = 0;
	for (int i = 0; i < str.length(); i++) {
	    int bit = str.charAt(i) - 'a';
	    if ((flag_bits & (1 << bit)) > 0) {
		return false;
	}
	flag_bits |= 1 << bit;
	}
	return true;
    }
}