public class RemoveElement {
    public int removeElement(int[] A, int elem) {
        int head = findHead(A, elem, 0);
        int tail = findTail(A, elem, A.length - 1);
        
        while (head < tail) {
            int temp = A[head];
            A[head] = A[tail];
            A[tail] = temp;
            head = findHead(A, elem, head);
            tail = findTail(A, elem, tail);
        }
        return tail + 1;
    }
    
    int findHead(int[] A, int elem, int idx) {
        if (A.length - 1 < idx) {
            return -1;
        } else {
            while (idx <= A.length - 1 && A[idx] != elem) {
                idx++;
            }
        }
        
        return idx;
    }
    
    int findTail(int[] A, int elem, int idx) {
        if (A.length - 1 < idx) {
            return -1;
        } else {
            while (idx >= 0 && A[idx] == elem) {
                idx--;
            }
        }
        
        return idx;
    }
}