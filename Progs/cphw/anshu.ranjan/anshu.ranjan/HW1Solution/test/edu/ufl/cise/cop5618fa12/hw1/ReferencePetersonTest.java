package edu.ufl.cise.cop5618fa12.hw1;

import java.util.concurrent.CountDownLatch;

public class ReferencePetersonTest implements HW1Test {
	long counter = 0;
	int interruptCounter = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ufl.cise.cop5618fa12.hw1.HW1Test#testLock(edu.ufl.cise.cop5618fa12
	 * .hw1.HW1Lock, int, int)
	 */
	@Override
	public boolean testLock(final HW1Lock lock, final int N0, final int N1)
			throws InterruptedException {
		counter = 0;

		interruptCounter = 0;
		final CountDownLatch latch = new CountDownLatch(1);
		counter = 0;

		interruptCounter = 0;
		Thread t0 = new Thread(new Runnable() {
			public void run() {
				try {
					latch.await();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				for (int i = 0; i < N0; i++) {
					lock.lock(0);
					counter++;
					lock.unlock(0);
				}
			}
		});

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < N1; i++) {
					try {
						latch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lock.lock(1);
					counter++;
					lock.unlock(1);
				}
			}
		});

		t0.start();
		t1.start();
		latch.countDown();
		t0.join();
		t1.join();
		if (counter != N0 + N1)
			System.err.println("testLockUnlock: counter = " + counter
					+ ", expected " + (N0 + N1));
		return counter == N0 + N1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ufl.cise.cop5618fa12.hw1.HW1Test#testTrylock(edu.ufl.cise.cop5618fa12
	 * .hw1.HW1Lock, int, int)
	 */
	@Override
	public boolean testTrylock(final HW1Lock lock, final int N0, final int N1)
			throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		counter = 0;
		Thread t0 = new Thread(new Runnable() {
			public void run() {
				try {
					latch.await();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				for (int i = 0; i < N0; i++) {
					while (!lock.tryLock(0)) {
						Thread.yield();
					}
					counter++;
					lock.unlock(0);
				}
			}
		});

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					latch.await();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				for (int i = 0; i < N1; i++) {
					while (!lock.tryLock(1)) {
						Thread.yield();
					}
					counter++;
					lock.unlock(1);
				}
			}
		});

		t0.start();
		t1.start();
		latch.countDown();
		t0.join();
		t1.join();
		if (counter != N0 + N1)
			System.err.println("testTrylockUnlock: counter = " + counter
					+ ", expected = " + (N0 + N1));
		return counter == N0 + N1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ufl.cise.cop5618fa12.hw1.HW1Test#testLockInterruptibly(edu.ufl.cise
	 * .cop5618fa12.hw1.HW1Lock, int, int)
	 */
	@Override
	public boolean testLockInterruptibly(final HW1Lock lock, final int N0,
			final int N1) throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		counter = 0;
		interruptCounter = 0;
		Thread t0 = new Thread(new Runnable() {
			public void run() {
				try {
					latch.await();
				} catch (InterruptedException e) {
					System.out.println("Thread 0 interrupted in latch");
				}
				for (int i = 0; i < N0; i++) {
					try {
						lock.lockInterruptibly(0);
						counter++;
					} catch (InterruptedException e) {
						// System.out.println("Thread 0 interrupted in lock");
						interruptCounter++;
					}

					lock.unlock(0);
				}
			}
		});

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < N1; i++) {
					try {
						latch.await();
					} catch (InterruptedException e) {
						System.out.println("Thread 1 interrupted in latch");
					}
					try {
						lock.lockInterruptibly(1);
					} catch (InterruptedException e) {
						System.out.println("Thread 1 interrupted in lock");
					}
//					try {
//						Thread.sleep(10);
						counter++;
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					lock.unlock(1);
				}
			}
		});

		t0.start();
		t1.start();
		latch.countDown();
		Thread.sleep(5);
		t0.interrupt();
		t0.join();
		t1.join();
		if (counter + interruptCounter != N0 + N1)
			System.err.println("testLockInterruptiblyUnlock: counter = "
					+ counter + ", interruptCounter = " + interruptCounter
					+ ", expected " + (N0 + N1));
		return counter + interruptCounter == N0 + N1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ufl.cise.cop5618fa12.hw1.HW1Test#comparePerformanceSingleThread(edu
	 * .ufl.cise.cop5618fa12.hw1.HW1Lock, edu.ufl.cise.cop5618fa12.hw1.HW1Lock,
	 * int)
	 */
	@Override
	public long comparePerformanceSingleThread(HW1Lock lock1, HW1Lock lock2,
			int N) {
		counter = 0;
		long start1 = System.nanoTime();
		for (int i = 0; i < N; i++) {
			lock1.lock(0);
			counter++;
			lock1.unlock(0);
		}
		long elapsed1 = System.nanoTime() - start1;
		counter = 0;
		long start2 = System.nanoTime();
		for (int i = 0; i < N; i++) {
			lock2.lock(0);
			counter++;
			lock2.unlock(0);
		}
		long elapsed2 = System.nanoTime() - start2;
		return elapsed2 - elapsed1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ufl.cise.cop5618fa12.hw1.HW1Test#comparePerformanceTwoThread(edu.
	 * ufl.cise.cop5618fa12.hw1.HW1Lock, edu.ufl.cise.cop5618fa12.hw1.HW1Lock,
	 * int, int)
	 */
	@Override
	public long comparePerformanceTwoThread(final HW1Lock lock0,
			final HW1Lock lock1, final int N0, final int N1)
			throws InterruptedException {
		counter = 0;
		final CountDownLatch latch = new CountDownLatch(1);
		counter = 0;

		interruptCounter = 0;
		Thread t0 = new Thread(new Runnable() {
			public void run() {
				try {
					latch.await();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				for (int i = 0; i < N0; i++) {
					lock0.lock(0);
					counter++;
					lock0.unlock(0);
				}
			}
		});

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < N1; i++) {
					try {
						latch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lock0.lock(1);
					counter++;
					lock0.unlock(1);
				}
			}
		});

		t0.start();
		t1.start();
		long start0 = System.nanoTime();
		latch.countDown();
		t0.join();
		t1.join();
		long elapsed0 = System.nanoTime() - start0;
		assert (counter != N0 + N1) : "testLockUnlock: counter = " + counter
				+ ", expected " + (N0 + N1);

		// run the test with lock1
		counter = 0;
		final CountDownLatch latch1 = new CountDownLatch(1);
		counter = 0;

		interruptCounter = 0;
		t0 = new Thread(new Runnable() {
			public void run() {
				try {
					latch1.await();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				for (int i = 0; i < N0; i++) {
					lock1.lock(0);
					counter++;
					lock1.unlock(0);
				}
			}
		});

		t1 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < N1; i++) {
					try {
						latch1.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lock1.lock(1);
					counter++;
					lock1.unlock(1);
				}
			}
		});

		t0.start();
		t1.start();
		long start1 = System.nanoTime();
		latch1.countDown();
		t0.join();
		t1.join();
		long elapsed1 = System.nanoTime() - start1;
		assert (counter != N0 + N1) : "testLockUnlock: counter = " + counter
				+ ", expected " + (N0 + N1);

		return elapsed1 - elapsed0;
	}
}
