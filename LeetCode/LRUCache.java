public class LRUCache {
    int capacity = 0;
    int current_size = 0;
    public LRUCache(int capacity) {
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
                int t = list.removeFromTail().key;
                map.remove(t);
            } else {
                current_size++;
            }
        }
    }
    
    Map<Integer, LRUCache.Node> map = new HashMap<Integer, LRUCache.Node>();
    
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
            } else {
                n.prev.next = n.next;
                n.next.prev = n.prev;
            }
            addToHead(n);
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
}