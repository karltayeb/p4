import java.util.Collection;
import java.util.ArrayList;
import java.util.Comparator;

/** This is specifies a general Priority Queues of ordered values.

    Note that the interface does not include methods to obtain
    minimum or maximum explicitly, but of course we want to be
    able to create PQs of either sort.

    Your implementation must provide two constructors, one that
    uses the "natural" ordering of java.util.Comparable<T>, and
    another that accepts an explicit java.util.Comparator<T> argument.

    It's a *very* good idea to take the time and read the Java
    documentation for those interfaces *before* you start hacking!

    @param <T> Type of element values.
*/
public class PQHeap<T extends Comparable<? super T>> 
                                     implements PriorityQueue<T> {

    /** Array List to put the heap in. */
    private ArrayList<T> heap;
    /** Comparator. */
    private Comparator<T> c;

    /** Default Constructor. */
    public PQHeap() {
        this.heap = new ArrayList();
        this.heap.add(null);
        this.c = new DefaultComparator();
    }

    /** Constructor with comparator. 
 *      @param comparator comparator. */
    public PQHeap(Comparator<T> comparator) {
        this.heap = new ArrayList();
        this.heap.add(null);
        this.c = comparator;
    }
    /** Insert a value. Duplicate values <b>do</b> end up in the
     *  queue, so inserting X three times means it has to be removed
     *  three times before it's gone again.
     *  @param t Value to add.
     */
    public void insert(T t) {
        this.heap.add(t);
        this.bubbleUp(this.heap.size() - 1);
    }
    
    /** Remove "best" value. This value is the "best" value in the
     *  queue as determined by the comparator for the queue.
     *  @throws QueueEmptyException If queue is empty.
     */
    public void remove() throws QueueEmptyException {
        if (this.heap.size() == 1) {
            throw new QueueEmptyException();
        }
        // If there is only one element
        if (this.heap.size() == 2) {
            this.heap.remove(1);
        } else {
            // If there is more than one, replace with the bottom element
            // and bubble down
            int last = this.heap.size() - 1; // get last index
            //swap last value to top of heap
            this.heap.set(1, this.heap.get(last));
            this.heap.remove(last); // remove the last one
            this.bubbleDown(1); // bubble down from the top
        }
    }
    
    /** Get the "best" value. This value is the "best" value in the
     *  queue as determined by the comparator for the queue.  [Note,
     *  "best" is at the root in a heap-based implementation.]
     *  @return best value in the queue.
     *  @throws QueueEmptyException If queue is empty.
     */
    public T peek() throws QueueEmptyException {
        if (this.heap.size() == 1) {
            throw new QueueEmptyException();
        }
        return this.heap.get(1); 
    }
    
    /** No elements?
     *  @return True if queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return (this.heap.size() == 1);
    }

    /** Get the number of elements in the queue.
     *  @return the numbers
     */
    public int size() {
        return this.heap.size() - 1;
    }

    /** Dump the contents of the priority queue.
     */
    public void clear() {
        this.heap.clear();
        this.heap.add(null);
    }

    /** Initialize a priority queue from a container of values.
     *  @param values the collection of starting values
     */
    public void init(Collection<T> values) {
        this.clear();  // first clear the heap
        this.heap.addAll(values); // add all the values to the heap

        int lastIndex = this.heap.size() - 1;

        int level = (int) (Math.floor(Math.log(lastIndex) 
                                        / Math.log(2) + 1e-10));
        int levelstart = (int) (Math.pow(2, level) + 1e-10);
        int levelend = lastIndex;

        while (level > 0) {
            level--;
            levelstart = (int) (Math.pow(2, level) + 1e-10);
            levelend = (int) (Math.pow(2, level + 1) + 1e-10) - 1;

            for (int i = levelstart; i <= levelend; i++) {
                int childIndex = this.getChildIndex(i);
                this.bubbleDown(i);
            }
        }
    }


    /** Helper method bubbles up based on copmarator.
     *  @param index the index we want to bubble up
     */
    private void bubbleUp(int index) {
        if (index == 1) {
            return; // no parent
        }
        int parentIndex = this.getParentIndex(index);
        // Swap and continue bubble up if heap is not in order
        if (this.c.compare(this.heap.get(index), 
                            this.heap.get(parentIndex)) > 0) {
            T temp = this.heap.get(index);
            this.heap.set(index, this.heap.get(parentIndex));
            this.heap.set(parentIndex, temp);
            this.bubbleUp(parentIndex);            
        }
    }

    /** Helper method bubbles down based on copmarator.
     *  @param index the index we want to bubble down
     */
    private void bubbleDown(int index) {
        int childIndex = this.getChildIndex(index);
        // if we are at the bottom of the heap (both children out of bounds)
        if (childIndex > (this.heap.size() - 1)) {
            return;
        }
        // if both children are valid, check which one to swap with
        if ((childIndex + 1) < this.heap.size()) {
            if (this.c.compare(this.heap.get(childIndex), 
                             this.heap.get(childIndex + 1)) < 0) {
                childIndex += 1; // switch to the second child
            }
        }
        // Swap and continue bubble down if we should
        if (this.c.compare(this.heap.get(index), 
                                this.heap.get(childIndex)) < 0) {
            T temp = this.heap.get(index);
            this.heap.set(index, this.heap.get(childIndex));
            this.heap.set(childIndex, temp);
            this.bubbleDown(childIndex);
        }
    }

    /** Returns index of parent entry in heap given index.
     *  @param index int index in the heap array we want the parent of
     *  @return index of the parent entry
     */
    public int getParentIndex(int index) {
        if (index == 1) {
            return 1;
        }
        // Every odd index shares a parent with an even index
        if (index % 2 == 1) {
            index -= 1;
        }

        int level = (int) Math.floor(Math.log(index) 
                               / Math.log(2) + 1e-10); // level index in heap
        int offset = index % (int) (Math.pow(2, level) + 1e-10);
        int plevel = level - 1;
        // the first index at the level
        int plevelstart = (int) (Math.pow(2, plevel) + 1e-10);
        int poffset = offset / 2;  // positions over level start
        return plevelstart + poffset;  // index of parent
    }

    /** Returns index of first child entry in heap given index.
     *  @param index int index in the heap array we want the child of
     *  @return index of the first child entry
     */
    public int getChildIndex(int index) {
        // Level of current index
        int level = (int) (Math.floor(Math.log(index) / Math.log(2) + 1e-10));
        // Offset of current index
        int offset = index % (int) (Math.pow(2, level) + 1e-10);
        // Level of child
        int clevel = level + 1;
        int childIndex = (int) (Math.pow(2, clevel) + 1e-10) + (offset * 2);

        // Ensure that we can get to it and it's sibling in the heap
        this.heap.ensureCapacity(childIndex + 2); 
        return childIndex; // index of first child in heap
    }
    /** DefaultComparator class. 
 *      @param <T> t. */
    private static class DefaultComparator<T extends Comparable<T>> 
                                               implements Comparator<T> {
        /** Compares two T values. 
 *        @param t1 t1.
 *        @param t2 t2. 
 *        @return int. */
        public int compare(T t1, T t2) {
            return t1.compareTo(t2);
        }
    }
    /** Returns true if it's a heap, false if otherwise. 
 *      @return boolean. */
    private boolean isHeap() {
        return this.isHeap(1);
    }

    /** Returns true if specified section is a heap, false otherwise. 
 *      @param index index.
 *      @return boolean. */
    private boolean isHeap(int index) {
        int child = this.getChildIndex(index);
        //check both children
        if (child >= this.heap.size()) {
            return true;
        }
        if (child + 1 >= this.heap.size()) {
            return true;
        }

        if (this.c.compare(this.heap.get(index), this.heap.get(child)) 
                            >= 0 && this.c.compare(this.heap.get(index), 
                                              this.heap.get(child + 1)) >= 0) {
            return this.isHeap(child) && this.isHeap(child + 1);
        }
        return false;
    }
    /** Convertes heap to string. 
 *      @return string version of heap. */
    public String toString() {
        return this.heap.toString() + this.isHeap();
    }
}
