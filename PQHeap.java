import java.util.Collection;
import java.util.ArrayList;
import java.lang.Math;
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
public class PQHeap<T extends Comparable<? super T>> implements PriorityQueue<T> {

    /** Array List to put the heap in. */
    private ArrayList<T> heap;
    private Comparator<T> c;

    /** Default Constructor */
    public PQHeap() {
    	this.heap = new ArrayList();
        this.c = new DefaultComparator();
    }

    /** Constructor with comparator */
    public PQHeap(Comparator<T> comparator) {
    	this.heap = new ArrayList();
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
        if (this.heap.size() == 0) {
            throw new QueueEmptyException();
        }
        // If there is only one element
        if (this.heap.size() == 1) {
            this.heap.remove(0);
        } else {
            // If there is more than one, replace with the bottom element
            // and bubble down
            int last = this.heap.size() - 1; // get last index
            this.heap.set(0,heap.get(last)); // swap last value to top of heap
            this.heap.remove(last); // remove the last one
            this.bubbleDown(0); // bubble down from the top
        }
    }
    
    /** Get the "best" value. This value is the "best" value in the
     *  queue as determined by the comparator for the queue.  [Note,
     *  "best" is at the root in a heap-based implementation.]
     *  @return best value in the queue.
     *  @throws QueueEmptyException If queue is empty.
     */
    public T peek() throws QueueEmptyException {
        if (this.heap.size() == 0) {
            throw new QueueEmptyException();
        }
        return this.heap.get(0); 
    }
    
    /** No elements?
     *  @return True if queue is empty, false otherwise.
     */
     public boolean isEmpty() {
        return (this.heap.size() == 0);
     }

    /** Get the number of elements in the queue.
     *  @return the numbers
     */
    public int size() {
        return this.heap.size();
    }

    /** Dump the contents of the priority queue.
     */
    public void clear() {
        this.heap.clear();
    }

    /** Initialize a priority queue from a container of values.
     *  @param values the collection of starting values
     */
    public void init(Collection<T> values) {
        return;
    }


    /** Helper method bubbles up based on copmarator
     *  @param index the index we want to bubble up
     */
    private void bubbleUp(int index) {
        if (index == 0) {
            return; // no parent
        }
        int parentIndex = this.getParentIndex(index);
        // Swap and continue bubble up if heap is not in order
        if (c.compare(this.heap.get(index), this.heap.get(parentIndex)) > 0) {
            T temp = this.heap.get(index);
            this.heap.set(index, this.heap.get(parentIndex));
            this.heap.set(parentIndex, temp);
            this.bubbleUp(parentIndex);            
        }
    }

    /** Helper method bubbles down based on copmarator
     *  @param index the index we want to bubble down
     */
    private void bubbleDown(int index) {
        int childIndex = this.getChildIndex(index);
        // if we are at the bottom of the heap (both children out of bounds)
        if (childIndex >= this.heap.size()) {
            return;
        }
        // if both children are valid, check which one to swap with
        if ((childIndex + 1) < this.heap.size()) {
            if (this.heap.get(childIndex).compareTo(this.heap.get(childIndex+1)) < 0) {
                childIndex += 1; // switch to the second child
            }
        }

        // Swap and continue bubble down if we should
        if (c.compare(this.heap.get(index), this.heap.get(childIndex)) < 0) {
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
    private int getParentIndex(int index) {
        if (index == 0) {
            return 0;
        }
        // Every odd index shares a parent with an even index
        if (index % 2 == 1) {
            index += 1;
        }

        int level = (int)Math.floor(Math.log(index) / Math.log(2) + 1e-10); // level in heap of parent
        // the first index at the level
        int levelstart = (int)(Math.pow(2, level - 1) - 1 + 1e-10);
        int offset = (index % (int)(Math.pow(2, level) + 1e-10)) / 2;  // positions over level start
        return levelstart + offset;  // index of parent


    }

    /** Returns index of first child entry in heap given index.
     *  @param index int index in the heap array we want the child of
     *  @return index of the first child entry
     */
    private int getChildIndex(int index) {
        // Level of parentMath.floor(Math.log(index + 1) / Math.log(2) + 1e-10) + 1;
        int plevel = (int) Math.floor(Math.log(index + 1) / Math.log(2) + 1 + 1e-10);
        // Offset of parent
        int poffset = (int)((index + 1) % (int)(Math.pow(2, plevel - 1) + 1e-10));
        // Level of child
        int clevel = plevel + 1;
        // Offset of first child
        int coffset = clevel + (2 * poffset);
        // the first index at child level
        int clevelstart = (int)(Math.pow(2, clevel - 1) - 1 + 1e-10);
        int childIndex = clevelstart + coffset;
    	// Ensure that we can get to it and it's sibling in the heap
        this.heap.ensureCapacity(childIndex + 2); 
        return childIndex; // index of first child in heap
    }

    private static class DefaultComparator<T extends Comparable<T>> implements Comparator<T> {
        public int compare(T t1, T t2) {
            return t1.compareTo(t2);
        }
    }
}
