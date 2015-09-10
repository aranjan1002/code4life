package edu.ufl.cise.cop5618fa12.hw1;



public class ReferencePetersonTwoVar implements HW1Lock {
	

	volatile int turn;
	volatile int vflag0;
	volatile int vflag1;
	
	@Override
	public void lock(int threadID){
		if (threadID == 0) vflag0 = 1; else vflag1 = 1;		
		int otherThreadID = threadID ^ 1;
		turn = otherThreadID;
		while( (threadID==0 ? vflag1 : vflag0)==1 && turn == otherThreadID) {Thread.yield();}
		return;
	}	

	public void unlock(int threadID) {
		if (threadID == 0) vflag0 = 0; else vflag1 = 0;		
	}

	@Override
	public void lockInterruptibly(int threadID) throws InterruptedException {
		if (Thread.currentThread().isInterrupted()) throw new InterruptedException();
		if (threadID == 0) vflag0 = 1; else vflag1 = 1;		
		int otherThreadID = threadID ^ 1;
		turn = otherThreadID;
		while((threadID==0 ? vflag1 : vflag0)==1 && turn==otherThreadID && !Thread.currentThread().isInterrupted()) {/*spin*/}
		if (Thread.interrupted()) throw new InterruptedException();
		return;
	}

	@Override
	public boolean tryLock(int threadID) {
		if (threadID == 0) vflag0 = 1; else vflag1 = 1;	
		int otherThreadID = threadID ^ 1;
		turn = otherThreadID;
		if ((threadID==0 ? vflag1 : vflag0)==1  && turn==otherThreadID) {return false;}
		return true;
	}

}
