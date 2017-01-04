import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
   
    private Node first;
    private Node last;
    private int s;
    
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }
   
    public Deque()  {                         // construct an empty deque
        first = null;
        last = null;
        s = 0;
    }
   
    public boolean isEmpty()  {               // is the deque empty?
        return first == null;
    }
   
    public int size() {                        // return the number of items on the deque
        return s;
    }
   
    public void addFirst(Item item) {         // add the item to the front
        if (item == null) {
            throw new java.lang.NullPointerException("addFirst: item is null");
        }
       
        Node oldfirst = first;
        first = new Node();
        first.next = oldfirst;
        first.previous = null;
        first.item = item;
        if (oldfirst != null) {
            oldfirst.previous = first;
        }
        if (s == 0) {
            last = first;
        }
        s++;
    }
   
    public void addLast(Item item)  {         // add the item to the end
        if (item == null) {
            throw new java.lang.NullPointerException("addLast: item is null");
        }
       
        Node oldlast = last;
        last = new Node();
        last.next = null;
        last.previous = oldlast;
        last.item = item;
        if (oldlast != null) {
            oldlast.next = last;
        }
        if (s == 0) {
            first = last;
        }
        s++;
    }
   
    public Item removeFirst() {                // remove and return the item from the front
        if (first == null) {
            throw new java.util.NoSuchElementException("removeFirst: first is null");
        }
        Item item = first.item;
        first = first.next;

        s--;
        
        if (s >= 1) {
            first.previous = null;
        }
        
        if (s == 0) {
            last = null;
            first = null;
        }
        return item;
    }
    
    public Item removeLast()  {               // remove and return the item from the end
        if (last == null) {
            throw new java.util.NoSuchElementException("removeLast: last is null");
        }
        Item item = last.item;
        last = last.previous;
        
        s--;
        
        if (s >= 1) {
            last.next = null;
        }

        if (s == 0) {
            last = null;
            first = null;
        }
        return item;
    }
    
    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        return new QueueIterator();
    } 
    
    private class QueueIterator implements Iterator<Item> {
        private Node current = first;
        private int sit = s;
        
        public boolean hasNext() {
            if (sit == 0) {
                current = null;
            }
            return sit > 0;
        }
        
        public void remove() {
            throw new java.lang.UnsupportedOperationException("remove operation not supported.");
        }
        
        public Item next() {
            if (sit == 0) {
                current = null;
                throw new java.util.NoSuchElementException("next: queue is empty");
            }
            Item item = current.item;
            current = current.next;
            sit--;
            return item;
        }
    }
    
    public static void main(String[] args) {  // unit testing
        Deque<Integer> d = new Deque<Integer>();
        System.out.println("Adding 1");
        d.addFirst(1);
        System.out.println("Adding 2");
        d.addFirst(2);
        System.out.println("Adding 3");
        d.addFirst(3);
        
        System.out.println(d.size());
        // d.addLast(4);
        int t = d.removeFirst();
        System.out.println("removed first:" + t);
        System.out.println("size:" + d.size());
        
        t = d.removeFirst();
        System.out.println("removed first:" + t);
        System.out.println("size:" + d.size());
        
        t = d.removeFirst();
        System.out.println("removed first:" + t);
        System.out.println("size:" + d.size());
        
        d.addFirst(1);
        d.addFirst(2);
        d.addFirst(3);
        
        System.out.println("LIST ITERATOR");
        for (int i : d) {
            System.out.println(i);
        }
        
        System.out.println("NEW TEST");
        
        d.addFirst(1);
        d.addFirst(2);
        d.addFirst(3);
        
        System.out.println("orginal size:" + d.size());
        Iterator<Integer> dIterator =  d.iterator();
        int i = 0;
        while (i < 2) {
            int toPrint = dIterator.next();
            System.out.println(toPrint);
            i++;
        }
        
        System.out.println("orginal size:" + d.size());
        
        System.out.println("FAILING TEST");
        d = new Deque<Integer>();
        
        d.addFirst(1);
        d.addLast(2);
        d.removeLast();
        System.out.println("new size:" + d.size());
        dIterator =  d.iterator();
        System.out.println("next:" + dIterator.next());
        // System.out.println("next:" + dIterator.next());
    }
}
