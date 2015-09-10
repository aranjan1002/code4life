package edu.ufl.cise.cop5618fa12.hw1;

public interface HW1Test {

    /*tests the lock and unlock methods for correctness.  N0 and N1 may be used
     * to parameterize the test, for example indicating the number of critical
     * section entries for thread0 and thread1 respectively
     * 
     * returns false if an error is detected, true otherwise
     */
    public abstract boolean testLock(HW1Lock lock, int N0, int N1)
	throws InterruptedException;

    /*tests the trylock and unlock methods for correctness.  N0 and N1 may be used
     * to parameterize the test, for example indicating the number of critical
     * section entries for thread0 and thread1 respectively
     * 
     * returns false if an error is detected, true otherwise
     */
    public abstract boolean testTrylock(HW1Lock lock, int N0, int N1)
	throws InterruptedException;

    /*tests the lockInterruptibly and unlock methods for correctness.  N0 and N1 may be used
     * to parameterize the test, for example indicating the number of critical
     * section entries for thread0 and thread1 respectively
     * 
     * returns false if an error is detected, true otherwise
     */
    public abstract boolean testLockInterruptibly(HW1Lock lock, int N0, int N1)
	throws InterruptedException;

    
    /*runs the same test on lock1 and lock2 to compare the performance when 
     * when there is no contention.   N may be used to parameterize the test; 
     * for example indicating the number of critical section entries 
     *   
     * returns elapsed1 - elapsed0 where elapsedi is the
     * elapsed time for the test with locki
     */
    public abstract long comparePerformanceSingleThread(HW1Lock lock1,
							HW1Lock lock2, int N);

    /*runs the same test on lock1 and lock2 to compare the performance when 
     * when there is contention.   N0 and N1 may be used to parameterize the test; 
     * for example indicating the number of critical
     * section entries for thread0 and thread1 respectively 
     *   
     * returns elapsed1 - elapsed0 where elapsedi is the
     * elapsed time for the test with locki
     */
    public abstract long comparePerformanceTwoThread(HW1Lock lock0,
						     HW1Lock lock1, 
						     int N0, 
						     int N1) 
	throws InterruptedException;

}
