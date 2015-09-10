package edu.ufl.cise.cop5618fa12.hw1;

import java.util.concurrent.atomic.*;

public class PetersonArray implements HW1Lock {

    PetersonArray() {
	flag = new AtomicIntegerArray(2);
    }

    public void lock(int threadID) {
	int otherThreadID = 1 - threadID;
	setFlagAndTurn(threadID);
	while (flag.get(otherThreadID) == 1 &&
	       turn == otherThreadID) {
	    Thread.yield();
	}
    }

    public void lockInterruptibly(int threadID)
        throws InterruptedException { 
	setFlagAndTurn(threadID);
	do {
	    if (Thread.interrupted()) {
		throw new InterruptedException();
	    }
	    Thread.yield();
	} while (!isLockAvailable(threadID));
    }

    public boolean tryLock(int threadID) {
	setFlagAndTurn(threadID);
	boolean avail = isLockAvailable(threadID);
	if (avail == false) {
	    revertFlagAndTurn(threadID);
	}
	return avail;
    }

    private boolean isLockAvailable(int threadID) {
	int otherThreadID = 1 - threadID;
	if (flag.get(otherThreadID) == 0 ||
            turn != otherThreadID) {
            return true;
        }
        return false;
    }

    private void setFlagAndTurn(int threadID) {
	int otherThreadID = 1 - threadID;
        flag.set(threadID, 1);
        turn = otherThreadID;
    }

    private void revertFlagAndTurn(int threadID) {
        flag.set(threadID, 0);
        turn = threadID;
    }

    public void unlock(int threadID) {
	flag.set(threadID, 0);
    }

    public void monitor() {
        System.out.println("Flags 0 and 1: " + flag.get(0)
                           + " " + flag.get(1));
    }

    private AtomicIntegerArray flag;
    private volatile int turn = 0;
}
