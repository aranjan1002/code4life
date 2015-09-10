import java.lang.*;
import java.util.*;

public class LRU {
    int capacity = 0;
    int current_size = 0;
    public LRU(int capacity) {
        this.capacity = capacity;
    }
    
    public int get(int key) {
        Integer key_int = new Integer(key);
        if (map.containsKey(key_int) == true) {
            Node n = map.get(key_int);
            list.removeNodeAndBringToFront(n);
            return n.value;
        }
        
        return -1;
    }
    
    public void set(int key, int value) {
        Integer key_int = new Integer(key);
        if (map.containsKey(key_int) == true) {
            Node n = map.get(key_int);
            n.value = value;
	    list.removeNodeAndBringToFront(n);
        } else {
            Node n = new Node(key, value);
            map.put(new Integer(key), n);
            list.addToHead(n);
            if (current_size == capacity) {
                Node tail = list.removeFromTail();
		map.remove(tail.key);
            } else {
                current_size++;
            }
        }
	System.out.println("map");
	for (Integer k : map.keySet()) {
	    System.out.println(k.intValue());
	}
	System.out.println("list");
	list.print();
    }
    
    Map<Integer, LRU.Node> map = new HashMap<Integer, LRU.Node>();
    
    class DoublyLinkedList {
        DoublyLinkedList() {
            
        }
        
        void addToHead(Node n) {
            n.next = head;
            if (head != null) {
                head.prev = n;
            }
            head = n;
            if (tail == null) {
                tail = head;
            }
            n.prev = null;
        }
        
        Node removeFromTail() {
	    System.out.println("removing from tail");
	    Node t = tail;
            if (tail != null) {
                tail = tail.prev;
                if (tail != null) {
                    tail.next = null;
                }
            }
	    return t;
        }
        
        void removeNodeAndBringToFront(Node n) {
            if (head.equals(n)) {
                return;
            }
	    else if (n.equals(tail)) {
                removeFromTail();
            }
            else {
		n.prev.next = n.next;
		n.next.prev = n.prev;
            }
            addToHead(n);
        }

	void print() {
	    Node n = head;
	    while (n != null) {
		System.out.print(n.key + " ");
		n = n.next;
	    }
	    System.out.println();
	}
        
        Node head = null;
        Node tail = null;
    }
    
    class Node {
        int key;
        int value;
        Node prev = null;
        Node next = null;
        
        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
        
        boolean equals(Node n) {
            return (n.key == key);
        }
    }
    
    DoublyLinkedList list = new DoublyLinkedList();

    public static void main(String[] args) {
	LRU lru = new LRU(2);
	lru.set(2,1);
	lru.set(1,1);
	lru.set(2,3);
	lru.set(4,1);
	System.out.println(lru.get(1));
	System.out.println(lru.get(2));
    }
}