import java.lang.Exception;

public class CircularArrayQueue {
     
    private static final int capacity = 5;
    private int[] Q;
    private final int N; // capacity
    private int f = 0;
    private int r = 0;
 
     
    public CircularArrayQueue(){
        this(capacity);
    }
     
    public CircularArrayQueue(int capacity){
        N = capacity;
        Q = new int[N];
    }
 
    public int size() {
        if(r > f)
            return r - f;
        return N - f + r;
    }
 
    public boolean isEmpty() {
        return (r == f) ? true : false;
    }
 
    public boolean isFull() {
        int diff = r - f; 
        if(diff == -1 || diff == (N -1))
            return true;
        return false;
    }
 
    public void enqueue(int obj) throws Exception {
        if(isFull()){
            throw new Exception("Queue is Full.");
        }else{
            Q[r] = obj;
            r = (r + 1) % N;
        }
    }
 
    public int dequeue() throws Exception {
        int item; 
        if(isEmpty()){
            throw new Exception();
        }else{
            item = Q[f];
            Q[f] = -1;
            f = (f + 1) % N;
        }
	return item;
    }

    public static void main(String[] args) 
    throws Exception {
	CircularArrayQueue q = new CircularArrayQueue(5);
	q.enqueue(1);
	q.enqueue(2);
	q.enqueue(3);
	q.enqueue(4);
	q.enqueue(5);
	System.out.println(q.dequeue());
    }
}