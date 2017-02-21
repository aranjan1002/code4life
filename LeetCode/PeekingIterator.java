// Java Iterator interface reference:
// https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html
class PeekingIterator implements Iterator<Integer> {
    Iterator<Integer> iterator;
    Integer next = null;

	public PeekingIterator(Iterator<Integer> iterator) {
	    // initialize any member here.
	    this.iterator = iterator;
	    if (iterator.hasNext()) {
	        next = iterator.next();
	    }
	}

    // Returns the next element in the iteration without advancing the iterator.
	public Integer peek() {
        return next;
	}

	// hasNext() and next() should behave the same as in the Iterator interface.
	// Override them if needed.
	@Override
	public boolean hasNext() {
	    return next != null;
	}
	
	@Override
	public Integer next() {
	    if (next == null) {
	        return null;
	    }
	    Integer result = next;
	    if (iterator.hasNext()) {
	        next = iterator.next();
	    } else {
	        next = null;
	    }
	    
	    return result;
	}
}