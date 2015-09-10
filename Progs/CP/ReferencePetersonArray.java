package edu.ufl.cise.cop5618fa12.hw1;

import java.util.concurrent.atomic.AtomicIntegerArray;



public class ReferencePetersonArray implements HW1Lock {
	
	final AtomicIntegerArray flag = new AtomicIntegerArray(2);
	volatile int turn;

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#lock(int)
	 */
	@Override
	public void lock(int threadID) {
		flag.set(threadID, 1);
		int otherThreadID = threadID ^ 1;
		turn = otherThreadID;
		while(flag.get(otherThreadID) == 1 && turn == otherThreadID) {Thread.yield();}
		return;
	}

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#lockInterruptibly(int)
	 */
	@Override
	public void lockInterruptibly(int threadID) throws InterruptedException {
		if (Thread.currentThread().interrupted()) throw new InterruptedException();
		flag.set(threadID, 1);
		int otherThreadID = threadID ^ 1;
		turn = otherThreadID;
		while(flag.get(otherThreadID) == 1 && turn==otherThreadID && !Thread.currentThread().isInterrupted()) {/*spin*/}
		if (Thread.interrupted()) { flag.set(threadID,  0); throw new InterruptedException(); }
		return;
	}

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#tryLock(int)
	 */
	@Override
	public boolean tryLock(int threadID) {
		flag.set(threadID, 1);
		int otherThreadID = threadID ^ 1;
		turn = otherThreadID;
		if (flag.get(otherThreadID) == 1  && turn==otherThreadID) {flag.set(threadID, 0); return false;}
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.ufl.cise.cop5618fa12.h1.HW1Lock#unlock(int)
	 */
	@Override
	public void unlock(int threadID) {
		flag.set(threadID, 0);
	}

}
