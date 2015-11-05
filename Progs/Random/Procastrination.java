class Procrastination {
    public long findFinalAssignee(long n) {
	long time = 2;
	long emp = n;

	while(time <= emp/2) {
	    if (emp % time == 0) {
		emp++;
	    } else if ((emp - 1) % time == 0) {
		emp--;
	    }
	    time++;
	}
	return emp;
    }

    public static void main(String[] args) {
	System.out.println(new Procrastination().findFinalAssignee(196248));
    }
}
