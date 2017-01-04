import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item[] s;
    private int sizeN = 0;
    
    public RandomizedQueue()  {               // construct an empty randomized queue
        s = (Item[]) new Object[2];
    }
    
    public boolean isEmpty() {                // is the queue empty?
        return sizeN == 0;
    }
    
    public int size()    {                    // return the number of items on the queue
        return sizeN;
    }
    
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < sizeN; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }
    
  
    public void enqueue(Item item)  {         // add the item
        if (item == null) {
            throw new java.lang.NullPointerException("enqueue: item is null");
        }        
        if (sizeN == s.length) resize(2 * s.length);
        // add the item at the end of the array
        s[sizeN] = item;
        // then switch it randomly with an existing item
        if (sizeN > 0) {
            int switchIdx = StdRandom.uniform(sizeN + 1);
            // System.out.println(switchIdx);
            Item switchElement = s[switchIdx];
            s[switchIdx] = s[sizeN];
            s[sizeN] = switchElement;
        }
        sizeN++;
        
    }
    
    public Item dequeue() {                   // remove and return a random item
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("dequeue: queue is empty");
        }        
        // remove the last element of the array
        // System.out.println("N:" + N);
        // System.out.println("s[0]:" + s[0]);
        Item dequeuedItem = s[--sizeN];
        // System.out.println("sizeN:" + sizeN);
        s[sizeN] = null;
        if (sizeN > 0 && sizeN == s.length/4) resize(s.length/2);
        return dequeuedItem;
    }
    
    public Item sample()  {                   // return (but do not remove) a random item
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("sample: queue is empty");
        }         
        // get a random element of the array (otherwise it would always be the same element retruend)
        int pickElement = StdRandom.uniform(sizeN);
        return s[pickElement];
    }
    
    public Iterator<Item> iterator()  {       // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
    }
    
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int nit = sizeN;
        private Item[] sit = copy(s, nit); // (Item[]) new Object[nit];
       
        public Item[] copy(Item[] orig, int capacity) {
            // copy items to another array
            Object[] copy = new Object[capacity];
            for (int j = 0; j < nit; j++) {
                copy[j] = orig[j];
            }
            // shuffle array so that all iterators will be different
            StdRandom.shuffle(copy);
            return (Item[]) copy;
        }
              
        public boolean hasNext() {
            return nit != 0;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException("remove operation not supported.");
        }
        public Item next() {
            if (nit == 0) {
                throw new java.util.NoSuchElementException("next: queue is empty");
            } 
            return sit[--nit];
        }
    }
    
    public static void main(String[] args) {  // unit testing
        RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();
        System.out.println("size:" + d.size());
        System.out.println("isEmpty:" + d.isEmpty());
        
        System.out.println("Enqueuing : 1");
        d.enqueue(1);
        System.out.println("size:" + d.size());
        System.out.println("isEmpty:" + d.isEmpty());
        int t = d.dequeue();
        System.out.println("Dequeuing : " + t);
        System.out.println("size:" + d.size());
        System.out.println("isEmpty:" + d.isEmpty());
        
        System.out.println("########## NEW TEST ##########");
        System.out.println("Enqueuing : 1");
        d.enqueue(1);
        System.out.println("Enqueuing : 2");
        d.enqueue(2);
        System.out.println("Enqueuing : 3");
        d.enqueue(3);
        System.out.println("size:" + d.size());
        System.out.println("isEmpty:" + d.isEmpty());
        
        System.out.println("Sampling : " + d.sample());
        System.out.println("Sampling : " + d.sample());
        System.out.println("Sampling : " + d.sample());
        
        t = d.dequeue();
        System.out.println("Dequeuing : " + t);
        t = d.dequeue();
        System.out.println("Dequeuing : " + t);
        t = d.dequeue();
        System.out.println("Dequeuing : " + t);
        
        System.out.println("########## NEW TEST ##########");
        System.out.println("Enqueuing : 1");
        d.enqueue(1);
        System.out.println("Enqueuing : 2");
        d.enqueue(2);
        System.out.println("Enqueuing : 3");
        d.enqueue(3);
        System.out.println("size:" + d.size());
        for (int i : d) {
            System.out.println("I:" + i);
        }
        
        for (int i = 0; i < 5; i++) {
            d = new RandomizedQueue<Integer>();
            System.out.println("########## NEW TEST ##########");
            System.out.println("Enqueuing : 1");
            d.enqueue(1);
            System.out.println("Enqueuing : 2");
            d.enqueue(2);
            t = d.dequeue();
            System.out.println("Dequeuing : " + t);
            t = d.dequeue();
            System.out.println("Dequeuing : " + t);
        }
      
    }
}