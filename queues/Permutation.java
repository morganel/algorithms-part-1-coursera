import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        
        RandomizedQueue<String> d = new RandomizedQueue<String>();
        
        String[] inputs = StdIn.readAllStrings();
        
        for (int i = 0; i < inputs.length; i++) {
            String input = inputs[i];
            // System.out.println(input);
            d.enqueue(input);
        }
        
        int i = 0;
        Iterator<String> dIterator =  d.iterator();
        while (i < k && dIterator.hasNext()) {
            String toPrint = dIterator.next();
            System.out.println(toPrint);
            i++;
        }
       
    }
}

// to test: java-algs4 Permutation 100 < queues/tinyTale.txt