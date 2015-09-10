class ReverseStringWords {
    public static void main(String[] args) {
	char[] abc = "Quick Brown Fox".toCharArray();
	new ReverseStringWords().reverseWordsInSentence(abc);
	System.out.println(abc);
    }

    void reverseWordsInSentence(char[] abc) {
	reverse(abc);
	reverseWords(abc);
    }

    void reverse(char[] abc) {
	int len = abc.length;
    
	for (int i = 0; i < len/2; i++) {
	    char temp = abc[i];
	    abc[i] = abc[len - i - 1];
	    abc[len - i - 1] = temp;
	}
    }

    void reverseWords(char[] abc) {
	int len = abc.length;
    
	for (int i = 0; i < len; i++) {
	    int j = findLastCharOfWordAt(abc, i);
	    for (int k = i, cnt = 0; k < (i + ((j - i + 1) / 2)); k++, cnt++) {
		swap(abc, k, j - cnt);
	    }
	    i = j + 1;
	}
    }

    int findLastCharOfWordAt(char[] abc, int i) {
	if (abc.length <= i) {
	    return -1;
	}
	for (int j = i; j < abc.length; j++) {
	    if (abc[j] == ' ') {
		return j - 1;
	    } else if (j == abc.length - 1) {
		return j;
	    }
	}
	return -1;
    }

    void swap(char[] a, int i, int j) {
	char temp = a[i];
	a[i] = a[j];
	a[j] = temp;
    }
}