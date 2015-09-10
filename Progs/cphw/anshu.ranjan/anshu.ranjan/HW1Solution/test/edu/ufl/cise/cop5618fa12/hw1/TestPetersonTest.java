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
import org.junit.rules.Timeout;


public class TestPetersonTest {

	final static int MINTRIES = 10000;
	final static int MAXTRIES = 40000;
	final static int AVGTRIES = 20000;
	final static int LOCKINTTESTAVG = 500;
	final static int LOCKINTTESTMIN = 250;
	final static int LOCKINTTESTMAX = 1000;
//	final static int TIMEOUTVALUE = 2000; // milliseconds
	
	
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
    public Timeout globalTimeout= new Timeout(2000000);
			
	/**********************************************/
	/* Test PetersonTest.testLock with both locks */
	/**********************************************/
	
	@Test
	public void testPetersonArrayTestLock1() {
		HW1Test pt = new PetersonTest();
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
	public void testPetersonTwoVarTestLock1() {
		HW1Test pt = new PetersonTest();
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

	/*************************************************/
	/* Test PetersonTest.testTryLock with both locks */
	/*************************************************/
	
	@Test
	public void testPetersonArrayTestTrylock1() {
		HW1Test pt = new PetersonTest();
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
	public void testPetersonTwoVarTestTrylock1() {
		HW1Test pt = new PetersonTest();
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
	

	/***********************************************************/
	/* Test PetersonTest.testLockInterruptibly with both locks */
	/***********************************************************/
	@Test
	public void testPetersonArrayTestLockInterruptibly1() {
		HW1Test pt = new PetersonTest();
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
	public void testPetersonTwoVarTestLockInterruptibly1() {
		HW1Test pt = new PetersonTest();
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
	
	/********************************************************/
	/* Test PetersonTest.testLock with both reference locks */
	/********************************************************/
	
	@Test
	public void testPetersonArrayTestLock2() {
		HW1Test pt = new PetersonTest();
		HW1Lock paLock = new ReferencePetersonArray();
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
	public void testPetersonTwoVarTestLock2() {
		HW1Test pt = new PetersonTest();
		HW1Lock paLock = new ReferencePetersonTwoVar();
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

	/***********************************************************/
	/* Test PetersonTest.testTryLock with both reference locks */
	/***********************************************************/
	
	@Test
	public void testPetersonArrayTestTrylock2() {
		HW1Test pt = new PetersonTest();
		HW1Lock paLock = new ReferencePetersonArray();
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
	public void testPetersonTwoVarTestTrylock2() {
		HW1Test pt = new PetersonTest();
		HW1Lock paLock = new ReferencePetersonTwoVar();
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
	

	/*********************************************************************/
	/* Test PetersonTest.testLockInterruptibly with both Reference locks */
	/*********************************************************************/
	@Test
	public void testPetersonArrayTestLockInterruptibly2() {
		HW1Test pt = new PetersonTest();
		HW1Lock paLock = new ReferencePetersonArray();
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
	public void testPetersonTwoVarTestLockInterruptibly2() {
		HW1Test pt = new PetersonTest();
		HW1Lock paLock = new ReferencePetersonTwoVar();
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
	
	/********************************************************/
	/* Test PetersonTest.testLock with incorrect ref. locks */
	/********************************************************/
	
	@Test
	public void testPetersonIncorrectTestLock1() {
		HW1Test pt = new PetersonTest();
		HW1Lock paLock = new ReferenceNoOpIncorrectLock();
		boolean failed = false;
		try{
			failed |= !pt.testLock(paLock, AVGTRIES, AVGTRIES);
			failed |= !pt.testLock(paLock, MINTRIES, MAXTRIES);
			failed |= !pt.testLock(paLock, MAXTRIES, MINTRIES);
		} catch(InterruptedException e){
			fail("Should not throw an interrupted exception!");
		}
		if (!failed)
			fail("Incorrect PetersonTest lock method passed!");
	}

	/***********************************************************/
	/* Test PetersonTest.testTryLock with incorrect ref. locks */
	/***********************************************************/
	
	
	@Test
	public void testPetersonIncorrectTestTrylock1() {
		HW1Test pt = new PetersonTest();
		HW1Lock paLock = new ReferenceNoOpIncorrectLock();
		boolean failed = false;
		try{
			failed |= !pt.testTrylock(paLock, AVGTRIES, AVGTRIES);
			failed |= !pt.testTrylock(paLock, MINTRIES, MAXTRIES);
			failed |= !pt.testTrylock(paLock, MAXTRIES, MINTRIES);
		} catch(InterruptedException e){
			fail("Should not throw an interrupted exception!");
		}
		if (!failed)
			fail("Incorrect PetersonTest Trylock method passed!");
	}
	

	/*********************************************************************/
	/* Test PetersonTest.testLockInterruptibly with incorrect ref. locks */
	/*********************************************************************/
	@Test
	public void testPetersonIncorrectTestLockInterruptibly1() {
		HW1Test pt = new ReferencePetersonTest();
		HW1Lock paLock = new ReferenceNoOpIncorrectLock();
		boolean failed = false;
		try{
			failed |= !pt.testLockInterruptibly(paLock, LOCKINTTESTAVG, LOCKINTTESTAVG);
			failed |= !pt.testLockInterruptibly(paLock, LOCKINTTESTMIN, LOCKINTTESTMAX);
			failed |= !pt.testLockInterruptibly(paLock, LOCKINTTESTMAX, LOCKINTTESTMIN);
		} catch(InterruptedException e){
			fail("Should not throw an interrupted exception!");
		}
		if (!failed)
			fail("Incorrect PetersonTest LockInterruptibly method passed!");
	}

}
