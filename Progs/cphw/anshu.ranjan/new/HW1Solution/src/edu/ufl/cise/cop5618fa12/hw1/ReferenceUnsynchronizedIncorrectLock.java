package edu.ufl.cise.cop5618fa12.hw1;


public class ReferenceUnsynchronizedIncorrectLock implements HW1Lock {
	public ReferenceUnsynchronizedIncorrectLock() {
		super();
		flag[0] = 0;
		flag[1] = 0;
	}

	long flag[] = new long[2];
	long turn;

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#lock(int)
	 */
	@Override
	public void lock(int threadID) {
		flag[threadID] = 1;
		int otherThreadID = threadID ^ 1;
		turn = otherThreadID;
		while(flag[otherThreadID] == 1 && turn == otherThreadID) {}
		return;
	}

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#lockInterruptibly(int)
	 */
	@Override
	public void lockInterruptibly(int threadID) throws InterruptedException {
		if (Thread.currentThread().isInterrupted()) throw new InterruptedException();
		flag[threadID] = 1;
		int otherThreadID = threadID ^ 1;
		turn = otherThreadID;
		while(flag[otherThreadID] == 1 && turn==otherThreadID && !Thread.currentThread().isInterrupted()) {/*spin*/}
		if (Thread.interrupted()) { flag[threadID] = 0; throw new InterruptedException(); }
		return;
	}

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#tryLock(int)
	 */
	@Override
	public boolean tryLock(int threadID) {
		flag[threadID] = 1;
		int otherThreadID = threadID ^ 1;
		turn = otherThreadID;
		if (flag[otherThreadID] == 1  && turn==otherThreadID) {flag[threadID] = 0; return false;}
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#unlock(int)
	 */
	@Override
	public void unlock(int threadID) {
		flag[threadID] = 0;
	}
}
