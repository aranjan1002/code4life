public class ReverseString {
    public static void main(String[] args) {
	new ReverseString().start(); 
    }

    public void start() {
	char[] x = {'a','d','s','a','e','r','s'};
	System.out.println(reverse(x));
    }

    public String reverse(char[] str) {
	if (str == null) {
	    return null;
	}
	int i = 0;
	int j = str.length - 1;
	while (i < j) {
	    char x = str[i];
	    str[i] = str[j];
	    str[j] = x;
	    i++;
	    j--;
	}
	return new String(str);
    }
}