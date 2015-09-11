package edu.ufl.cise.cop5618fa12.hw1;

public class Main {
    public static void main(String[] args) {
	new Main().start();
    }

    final static int MINTRIES = 5000;
    final static int MAXTRIES = 7500;
    final static int AVGTRIES = 10000;
    final static int LOCKINTTESTAVG = 200;
    final static int LOCKINTTESTMIN = 100;
    final static int LOCKINTTESTMAX = 400;
    final static int TIMEOUTVALUE = 2000000;

    public void start() {
	HW1Test pt = new ReferencePetersonTest();
	HW1Lock paLock = new PetersonTwoVar();
	boolean success = true;
	try{
	    success &= pt.testLockInterruptibly(paLock, LOCKINTTESTAVG, LOCKINTTESTAVG);
	    success &= pt.testLockInterruptibly(paLock, LOCKINTTESTMIN, LOCKINTTESTMAX);
	    success &= pt.testLockInterruptibly(paLock, LOCKINTTESTMAX, LOCKINTTESTMIN);
	} catch(InterruptedException e){
	    System.out.println("Should not throw an interrupted exception!");
	}
	if (!success)
	    System.out.println("PetersonTest LockInterruptibly method failed!");


	
	// HW1LockImpl lock = new HW1LockImpl();
	// lock.monitor();
	/*HW1Lock lock = new PetersonArray();
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
	System.out.println(test.comparePerformanceSingleThread(lock, lock2, 20000));
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
	HW1Lock lock3 = new PetersonArray();
	HW1Lock lock1 = new PetersonTwoVar();
	HW1Test pt = new PetersonTest();
	HW1Test ptRef = new ReferencePetersonTest();
	HW1Lock lock4 = new ReferencePetersonArray();
	HW1Lock lock5 = new ReferencePetersonTwoVar();
	long diff2 = 0, diff1 = 0;
	//diff2 = pt.comparePerformanceSingleThread(lock1, lock3, 20000);
	diff1 = pt.comparePerformanceSingleThread(lock1, lock3, 20000);
	diff2 = ptRef.comparePerformanceSingleThread(lock1, lock3, 20000);
	System.out.println("Reference : " + diff1 + ", Tested : " + diff2);
	*/
    }
}