package edu.ufl.cise.cop5618fa12.hw1;

public class Main {
    public static void main(String[] args) {
	new Main().start();
    }

    public void start() {
	// HW1LockImpl lock = new HW1LockImpl();
	// lock.monitor();
	HW1Lock lock = new PetersonArray();
	HW1Test test = new PetersonTest();
	boolean success;
	System.out.println("Test PetersonArray");
	try {
	    success = test.testLock(lock, 50, 50);
	    System.out.println("TestLock: " + success);
	    success = test.testTrylock(lock, 50, 50);
	    System.out.println("TestTryLock: " + success);
	    success = test.testLockInterruptibly(lock, 50, 50);
            System.out.println("TestLockInterruptibly: " + success);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	HW1Lock lock2 = new PetersonTwoVar();
	System.out.println("Test PetersonTwoVar");
        try {
            success = test.testLock(lock2, 50, 50);
            System.out.println("TestLock: " + success);
            success = test.testTrylock(lock2, 50, 50);
            System.out.println("TestTryLock: " + success);
            success = test.testLockInterruptibly(lock2, 50, 50);
            System.out.println("TestLockInterruptibly: " + success);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	System.out.println("Single Thread (without contention)");
	System.out.print("Time taken by TwoVarLock - Time taken by " + 
			 "ArrayLock: ");
	System.out.println(test.comparePerformanceSingleThread(lock, lock2, 50));
	try {
	    System.out.println("Two threads (with contention)");
	    System.out.print("Time taken by TwoVarLock - Time taken by " +
			     "ArrayLock: ");
	    System.out.println(test.comparePerformanceTwoThread(lock, 
								lock2, 
								50, 
								50));
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
}
