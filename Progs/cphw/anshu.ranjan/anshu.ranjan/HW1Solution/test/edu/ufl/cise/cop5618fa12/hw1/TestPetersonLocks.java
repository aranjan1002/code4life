package edu.ufl.cise.cop5618fa12.hw1;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.Timeout;

public class TestPetersonLocks {

	public volatile PrintStream originalOut;
	public volatile PrintStream originalErr;
	
//	@Before
//	public void makeOutErrNull(){
//		originalOut = System.out;
//		originalErr = System.err;
//		PrintStream nullStream = new PrintStream(new OutputStream() {
//			@Override
//			public void write(int b) throws IOException {}
//		});
//		System.setOut(nullStream);
//		System.setErr(nullStream);
//	}
//	
//	@After
//	public void restoreOutErrNull(){
//		System.setOut(originalOut);
//		System.setErr(originalErr);
//	}
	
    @Rule
    public Timeout globalTimeout= new Timeout(100000);
	
	final static int MINTRIES = 10000;
	final static int MAXTRIES = 40000;
	final static int AVGTRIES = 20000;
	final static int LOCKINTTESTAVG = 100;
	final static int LOCKINTTESTMIN = 50;
	final static int LOCKINTTESTMAX = 200;
	final static int TIMEOUTVALUE = 200000; // milliseconds
	
	/***********************************************************************************************/
	
	/**************************************************/
	/* Test PetersonTest.testLock with reference Test */
	/**************************************************/
	
	@Test
	public void testPetersonArrayTestLock3() {
		HW1Test pt = new ReferencePetersonTest();
		HW1Lock paLock = new PetersonArray();
		boolean success = true;
		try{
			 success &= pt.testLock(paLock, AVGTRIES, AVGTRIES);
			 success &= pt.testLock(paLock, MINTRIES, MAXTRIES);
			 success &= pt.testLock(paLock, MAXTRIES, MINTRIES);
		} catch(InterruptedException e){
			fail("Should not throw an interrupted exception!");
		}
		
		if (!success)
			fail("PetersonTest lock method failed!");
	}
	
	@Test
	public void testPetersonTwoVarTestLock3() {
		HW1Test pt = new ReferencePetersonTest();
		HW1Lock paLock = new PetersonTwoVar();
		boolean success = true;
		try{
			success &= pt.testLock(paLock, AVGTRIES, AVGTRIES);
			success &= pt.testLock(paLock, MINTRIES, MAXTRIES);
			success &= pt.testLock(paLock, MAXTRIES, MINTRIES);
		} catch(InterruptedException e){
			fail("Should not throw an interrupted exception!");
		}
		if (!success)
			fail("PetersonTest lock method failed!");
	}

	/*****************************************************/
	/* Test PetersonTest.testTryLock with reference Test */
	/*****************************************************/
	
	@Test
	public void testPetersonArrayTestTrylock3() {
		HW1Test pt = new ReferencePetersonTest();
		HW1Lock paLock = new PetersonArray();
		boolean success = true;
		try{
			 success &= pt.testTrylock(paLock, AVGTRIES, AVGTRIES);
			 success &= pt.testTrylock(paLock, MINTRIES, MAXTRIES);
			 success &= pt.testTrylock(paLock, MAXTRIES, MINTRIES);
		} catch(InterruptedException e){
			fail("Should not throw an interrupted exception!");
		}
		if (!success)
			fail("PetersonTest Trylock method failed!");
	}
	
	
	@Test
	public void testPetersonTwoVarTestTrylock3() {
		HW1Test pt = new ReferencePetersonTest();
		HW1Lock paLock = new PetersonTwoVar();
		boolean success = true;
		try{
			 success &= pt.testTrylock(paLock, AVGTRIES, AVGTRIES);
			 success &= pt.testTrylock(paLock, MINTRIES, MAXTRIES);
			 success &= pt.testTrylock(paLock, MAXTRIES, MINTRIES);
		} catch(InterruptedException e){
			fail("Should not throw an interrupted exception!");
		}
		if (!success)
			fail("PetersonTest Trylock method failed!");
	}
	

	/***************************************************************/
	/* Test PetersonTest.testLockInterruptibly both Reference Test */
	/***************************************************************/
	@Test
	public void testPetersonArrayTestLockInterruptibly3() {
		HW1Test pt = new ReferencePetersonTest();
		HW1Lock paLock = new PetersonArray();
		boolean success = true;
		try{
			 success &= pt.testLockInterruptibly(paLock, LOCKINTTESTAVG, LOCKINTTESTAVG);
			 success &= pt.testLockInterruptibly(paLock, LOCKINTTESTMIN, LOCKINTTESTMAX);
			 success &= pt.testLockInterruptibly(paLock, LOCKINTTESTMAX, LOCKINTTESTMIN);
		} catch(InterruptedException e){
			fail("Should not throw an interrupted exception!");
		}
		if (!success)
			fail("PetersonTest LockInterruptibly method failed!");
	}
	
	
	@Test
	public void testPetersonTwoVarTestLockInterruptibly3() {
		HW1Test pt = new ReferencePetersonTest();
		HW1Lock paLock = new PetersonTwoVar();
		boolean success = true;
		try{
			 success &= pt.testLockInterruptibly(paLock, LOCKINTTESTAVG, LOCKINTTESTAVG);
			 success &= pt.testLockInterruptibly(paLock, LOCKINTTESTMIN, LOCKINTTESTMAX);
			 success &= pt.testLockInterruptibly(paLock, LOCKINTTESTMAX, LOCKINTTESTMIN);
		} catch(InterruptedException e){
			fail("Should not throw an interrupted exception!");
		}
		if (!success)
			fail("PetersonTest LockInterruptibly method failed!");
	}

	
	/***********************************************************************************************/
	
	/* Performance Testing */
	
	@Test 
	public void testSinglePerformance() throws InterruptedException{
		HW1Lock lock2 = new PetersonArray();
		HW1Lock lock1 = new PetersonTwoVar();
		HW1Test pt = new PetersonTest();
		HW1Test ptRef = new ReferencePetersonTest();
		
		long diff2 = pt.comparePerformanceSingleThread(lock1, lock2, AVGTRIES);
		long diff1 = ptRef.comparePerformanceSingleThread(lock1, lock2, AVGTRIES);
		System.out.println("Reference : " + diff1 + ", Tested : " + diff2);
	}
	
	@Test 
	public void testDoublePerformance() throws InterruptedException{
		HW1Lock lock2 = new PetersonArray();
		HW1Lock lock1 = new PetersonTwoVar();
		HW1Test pt = new PetersonTest();
		HW1Test ptRef = new ReferencePetersonTest();
		
		long diff2 = pt.comparePerformanceTwoThread(lock1, lock2, AVGTRIES, AVGTRIES);
		long diff1 = ptRef.comparePerformanceTwoThread(lock1, lock2, AVGTRIES, AVGTRIES);
		System.out.println("Reference : " + diff1 + ", Tested : " + diff2);
	}
	
	/***********************************************************************************************/
	
	/* Test Lock Interruptibly */
	
	@Test(timeout = TIMEOUTVALUE)
	public void testPetersonArrayLockInterruptiblyDirectly1() throws InterruptedException{
		final HW1Lock lock = new PetersonArray();
		final CountDownLatch startThreadLatch = new CountDownLatch(1);
		final boolean[] interrupted = new boolean[1];
		interrupted[0] = false;
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				try{
					startThreadLatch.await();
					lock.lockInterruptibly(0);
				} catch(InterruptedException e){
					interrupted[0] = true;
				}
			}
		};
		
		
		Thread t = new Thread(r);
		t.start();
		t.interrupt();
		startThreadLatch.countDown();
		t.join();
		if (!interrupted[0])
			fail("Lock Interruptible was supposed to throw an interrupted exception");
		
	}
	
	@Test(timeout = TIMEOUTVALUE)
	public void testPetersonTwoVarLockInterruptiblyDirectly1() throws InterruptedException{
		final HW1Lock lock = new PetersonTwoVar();
		final CountDownLatch startThreadLatch = new CountDownLatch(1);
		final boolean[] interrupted = new boolean[1];
		interrupted[0] = false;
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				try{
					startThreadLatch.await();
					lock.lockInterruptibly(0);
				} catch(InterruptedException e){
					interrupted[0] = true;
				}
			}
		};
		
		
		Thread t = new Thread(r);
		t.start();
		t.interrupt();
		startThreadLatch.countDown();
		t.join();
		if (!interrupted[0])
			fail("Lock Interruptible was supposed to throw an interrupted exception");
		
	}
	
	@Test(timeout = TIMEOUTVALUE)
	public void testPetersonArrayLockInterruptiblyDirectly2() throws InterruptedException{
		final HW1Lock lock = new PetersonArray();
		final CountDownLatch startThreadLatch = new CountDownLatch(1);
		final boolean[] interrupted = new boolean[1];
		interrupted[0] = false;
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				try{
					startThreadLatch.await();
					lock.lockInterruptibly(1);
				} catch(InterruptedException e){
					interrupted[0] = true;
				}
			}
		};
		
		try{
		lock.lockInterruptibly(0);
		} catch (InterruptedException e){
			// There should be no exception here.
			fail("Unexpected Thread was interrupted");
		}
		Thread t = new Thread(r);
		t.start();
		t.interrupt();
		startThreadLatch.countDown();
		t.join();
		if (!interrupted[0])
			fail("Lock Interruptible was supposed to throw an interrupted exception");
		
	}
	
	@Test(timeout = TIMEOUTVALUE)
	public void testPetersonTwoVarLockInterruptiblyDirectly2() throws InterruptedException{
		final HW1Lock lock = new PetersonTwoVar();
		final CountDownLatch startThreadLatch = new CountDownLatch(1);
		final boolean[] interrupted = new boolean[1];
		interrupted[0] = false;
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				try{
					startThreadLatch.await();
					lock.lockInterruptibly(1);
				} catch(InterruptedException e){
					interrupted[0] = true;
				}
			}
		};
		
		try{
		lock.lockInterruptibly(0);
		} catch (InterruptedException e){
			// There should be no exception here.
			fail("Unexpected Thread was interrupted");
		}
		Thread t = new Thread(r);
		t.start();
		t.interrupt();
		startThreadLatch.countDown();
		t.join();
		if (!interrupted[0])
			fail("Lock Interruptible was supposed to throw an interrupted exception");
		
	}
}
