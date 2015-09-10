package edu.ufl.cise.cop5618fa12.hw1;

public class Main {
    final static int MINTRIES = 5000;
    final static int MAXTRIES = 7500;
    final static int AVGTRIES = 10000;
    final static int LOCKINTTESTAVG = 200;
    final static int LOCKINTTESTMIN = 100;
    final static int LOCKINTTESTMAX = 400;
    final static int TIMEOUTVALUE = 2000000; // milliseconds

    public static void main(String[] args) {
	new Main().start();
    }

    public void start() {
	// HW1LockImpl lock = new HW1LockImpl();
	// lock.monitor();
	HW1Test pt = new PetersonTest();
	HW1Lock paLock = new ReferenceNoOpIncorrectLock();
	boolean failed = true;
	try{
	    failed &= pt.testLockInterruptibly(paLock, LOCKINTTESTAVG
					       , LOCKINTTESTAVG);
	    failed &= pt.testLockInterruptibly(paLock, LOCKINTTESTMIN
					       , LOCKINTTESTMAX);
	    failed &= pt.testLockInterruptibly(paLock, LOCKINTTESTMAX
					       , LOCKINTTESTMIN);
	} catch(InterruptedException e){
	    System.out.println("Should not throw an interrupted exception!");
	}
	if (!failed)
	    System.out.println("Incorrect PetersonTest LockInterruptibly method passed!");



	/*

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
	    }*/
    }
}
