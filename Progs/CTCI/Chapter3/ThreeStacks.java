// These are actually three queues
class ThreeStacks {
    void push(int stackNum, int value) {
	if (checkFull(stackNum) == true) {
	    throw new RunTimeException();
	} else {
	    int indexToInsert = 0;
	    if (stackNum == 1) {
		indexToInsert = s1_idx2;
		s1_idx2 = (s1_idx2 + 1) % STACKSIZE;
	    } else if (stackNum == 2) {
		indexToInsert = s2_idx2 + STACKSIZE;
		s2_idx2 = (s2_idx2 + 1) % STACKSIZE;
	    } else {
		indexToInsert = s3_idx3 + (2 * STACKSIZE);
		s3_idx3 = (s3_idx3 + 1) % STACKSIZE;
	    }
	    list[indexToInsert] = value;
	}
    }
    
    boolean checkFull(int stackNum) {
	int idx1 = 0, idx2 = 0;
	if (stackNum == 1) {
	    idx1 = s1_idx1;
	    idx2 = s2_idx2;
	} else if (stackNum == 2) {
	    idx1 = s2_idx1;
	    idx2 = s2_idx2;
	} else {
	    idx1 = s3_idx1;
	    idx2 = s3_idx2;
	}

	return ((idx2 + 1) % N == idx1);
    }

    boolean checkEmpty(int stackNum) {
	if (stackNum == 1) {
	    return s1_idx1 == s1_idx2;
	} else if (stackNum == 2) {
	    return s2_idx1 == s2_idx2;
	} else {
	    return s3_idx1 == s3_idx2;
	}
    }

    int pop(int stackNum) {
	if (checkEmpty(stackNum) == true) {
	    throw new RunTimeException();
	} else {
	    int result = 0;
	    if (stackNum == 1) {
		result = list[s1_idx1];
		s1_idx1 = (s1_idx1 + 1) % STACKSIZE;
	    } else if (stackNum == 2) {
		result = list[s2_idx1 + STACKSIZE];
		s2_idx1 = (s2_idx2 + 1) % STACKSIZE;
	    } else {
		result = list[s3_idx1 + (2 * STACKSIZE)];
		s3_idx1 = (s3_idx3 + 1) % STACKSIZE;
	    }
	    return result;
	}
    }

    int[] list = new int[3 * STACKSIZE];
    int s1_idx1 = 0, s1_idx2 = 0;
    int s2_idx1 = 0, s2_idx2 = 0;
    int s3_idx1 = 0, s3_idx3 = 0;

    public static final int STACKSIZE = 5;
}