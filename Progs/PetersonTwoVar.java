package edu.ufl.cise.cop5618fa12.hw1;

import java.util.concurrent.atomic.*;

public class PetersonTwoVar implements HW1Lock {

    PetersonTwoVar() {
    }

    public void lock(int threadID) {
        setFlagAndTurn(threadID);
        while (isLockAvailable(threadID) == false) {
	    // System.out.println("Still trying: " + threadID + flag1 + flag2 +
	    //		       turn);
	}
    }

    public void lockInterruptibly(int threadID)
        throws InterruptedException {
        setFlagAndTurn(threadID);
        do {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        } while (!isLockAvailable(threadID));
    }

    public boolean tryLock(int threadID) {
        setFlagAndTurn(threadID);
        return isLockAvailable(threadID);
    }

    private boolean isLockAvailable(int threadID) {
        int otherThreadID = 1 - threadID;
	boolean isOtherFlagSet = (threadID == 0 ? flag2 : flag1);

        if (isOtherFlagSet == false ||
            turn != otherThreadID) {
            return true;
        }
        return false;
    }

    private void setFlagAndTurn(int threadID) {
        int otherThreadID = 1 - threadID;
	if (threadID == 0) {
	    flag1 = true;
	}
	else {
	    flag2 = true;
	}
	turn = otherThreadID;
    }

    public void unlock(int threadID) {
        if (threadID == 0) {
            flag1 = false;
        }
        else {
            flag2 = false;
        }
    }

    public void monitor() {
        
    }

    private volatile boolean flag1 = false, flag2 = false;
    private volatile int turn = 0;
}
