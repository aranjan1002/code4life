package edu.ufl.cise.cop5618fa12.hw1;

public interface HW1Lock {

    public void lock(int threadID);

    public void lockInterruptibly(int threadID)
	throws InterruptedException;

    public boolean tryLock(int threadID);

    public void unlock(int threadID);

}
