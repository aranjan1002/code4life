package edu.ufl.cise.cop5618fa12.hw1;

public class ReferenceNoOpIncorrectLock implements HW1Lock {
	public ReferenceNoOpIncorrectLock() {
		super();
	}

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#lock(int)
	 */
	@Override
	public void lock(int threadID) {
		return;
	}

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#lockInterruptibly(int)
	 */
	@Override
	public void lockInterruptibly(int threadID) throws InterruptedException {
		return;
	}

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#tryLock(int)
	 */
	@Override
	public boolean tryLock(int threadID) {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#unlock(int)
	 */
	@Override
	public void unlock(int threadID) {
	}
}