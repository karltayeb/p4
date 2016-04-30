/**
 * Karl Tayeb ktayeb1
 * Natasha Bornhorst nborno1
 * Richard Ding rding2
 * Assignment: P4
 * Due: 4/30/2016
 */

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.Math;
import java.util.List;
import java.util.LinkedList;

public class PQHeapTest {
	static PQHeap<Integer> empty;
	static PQHeap<Integer> smalltolarge;
	static PQHeap<Integer> largetosmall;
	static PQHeap<Integer> allsame;
	static PQHeap<Integer> randomgen;
	static PQHeap<Integer>[] differentsizes;

    @Before
    public void initialize() {
    	/* To test empty heap. */
        empty = new PQHeap();

        /* More than 1 to make sure heap order is maintained regardless
         * of what order the heap is built in, and tests when repeated
         * priority values are present. */
        smalltolarge = new PQHeap();
        largetosmall = new PQHeap();
        allsame = new PQHeap();
        randomgen = new PQHeap();
        differentsizes = new PQHeap[100];

        for (int i = 0; i < 100; i++) {
            smalltolarge.insert((Integer) i);
            largetosmall.insert((Integer) (99 - i));
            allsame.insert((Integer) 1);
        }

        int random = 99;
        for (int i = 0; i < 100; i++) {
        	randomgen.insert((Integer) random);
        	// Ensures repeats and that 99 is the largest value
        	random = (Integer)(int)(Math.random() * 50 + 1);
        }

        for (int i = 0; i < 100; i++) {
            differentsizes[i] = new PQHeap();
        	for (int j = 0; j <= i; j++) {
        		differentsizes[i].insert((Integer) j);
        	}
        }
    }

    @ Test
    public void testParentIndex() {
        for (int i = 1; i < 1000000; i++) {
            assertTrue("",empty.getParentIndex(i*2) == empty.getParentIndex(i*2+1));
        }

        for (int i = 2; i < 10000000; i*=2) {
            assertTrue("", empty.getParentIndex(i) == (i/2));
        }
    }

    @Test
    public void testChildIndex() {
        return;
    }
    /* Test size() on empty MaxPQ */
    @Test
    public void testSizeEmpty() {
        assertEquals("Empty MaxPQ should have size of 0", 0, empty.size());
    }

    /* Test size() on non-empty MaxPQ */
    @Test
    public void testSizeNonEmpty() {
    	// Correct size regardless of how heap is built
    	assertTrue("MaxPQ not the correct size, expected 100, was " + smalltolarge.size(), smalltolarge.size() == 100);
    	assertTrue("MaxPQ not the correct size", largetosmall.size() == 100);

    	// Correct size regardless of presence of equal priorities
    	assertTrue("MaxPQ not the correct size", allsame.size() == 100);
    	assertTrue("MaxPQ not the correct size", randomgen.size() == 100);

    	// Check all sizes 0 - 99, covers cases where heap is different # of levels
    	for (int i = 0; i < differentsizes.length; i++) {
    		assertTrue(differentsizes[i].size() == i+1);
    	}
    }

    /* Test isEmpty() on empty MaxPQ */
    @Test
    public void testIsEmptyWithEmpty() {
        assertTrue("isEmpty() returns not empty when is empty", empty.isEmpty());
    }

    /* Test isEmpty() on non-empty MaxPQ */
    @Test
    public void testIsEmptyNonEmpty() {
    	// Correct result regardless of how heap is built    	
    	assertTrue("isEmpty() return empty when is non-empty", !smalltolarge.isEmpty());
    	assertTrue("isEmpty() return empty when is non-empty", !largetosmall.isEmpty());
    	// Correct result regardless of how heap is built
     	assertTrue("isEmpty() return empty when is non-empty", !allsame.isEmpty());
    	assertTrue("isEmpty() return empty when is non-empty", !randomgen.isEmpty());

    	// Checks all sizes of MaxPQ 1 - 99, size 1 is boundary condition
    	for (int i = 0; i < differentsizes.length; i++) {
    		assertTrue("isEmpty() returns empty when MaxPQ is not empty", !differentsizes[i].isEmpty());
    	}
    }

    /* Test clear() on empty MaxPQ */
    @Test
    public void testClearEmpty() {
        empty.clear();
        assertEquals("Size not updated correctly on clear() for empty list", 0, empty.size());
        assertTrue("MaxPQ is not empty after clear()", empty.isEmpty());
    }

    /* Test clear() on non-empty MaxPQ */
    @Test
    public void testClearNonEmpty() {

    	smalltolarge.clear();
    	assertTrue("Size not updated correctly on clear()", smalltolarge.size() == 0);
		assertTrue("MaxPQ is not empty after clear()", smalltolarge.isEmpty());

    	largetosmall.clear();
    	assertTrue("Size not updated correctly on clear()", largetosmall.size() == 0);
		assertTrue("MaxPQ is not empty after clear()", largetosmall.isEmpty());

    	allsame.clear();
    	assertTrue("Size not updated correctly on clear()", allsame.size() == 0);
		assertTrue("MaxPQ is not empty after clear()", allsame.isEmpty());

    	randomgen.clear();
    	assertTrue("Size not updated correctly on clear()", randomgen.size() == 0);
		assertTrue("MaxPQ is not empty after clear()", randomgen.isEmpty());

		// Checks clear on a range of different sized MaxPQs, size 1 is boundary condition
		for (int i = 0; i < differentsizes.length; i++) {
	    	differentsizes[i].clear();
	    	assertTrue("Size not updated correctly on clear()", differentsizes[i].size() == 0);
			assertTrue("MaxPQ is not empty after clear()", differentsizes[i].isEmpty());			
		}
    }

    /* Test peek() on empty MaxPQ (check that error is thrown) */
    @Test
    public void testGetMaxEmpty() {
        Integer i = null;
        try {
            i = empty.peek();
            fail("Expected a QueueEmptyException to be thrown");
        } catch (QueueEmptyException q) {
            assertEquals("peek() returns a value with empty list", 
                    null, i);
        }
    }

    /* Test peek() on non-empty MaxPQ */
    @Test
    public void testGetMaxNonEmpty() {
    	assertTrue("Max element is incorrect for heap built from small to large values",
    					smalltolarge.peek() == (Integer) 99);
    	assertTrue("Max element is incorrect for heap built from large to small values",
    					largetosmall.peek() == (Integer) 99);
    	assertTrue("Max element is incorrect for heap built with all identical values",
    					allsame.peek() == (Integer) 1);
    	assertTrue("Max element is incorrect for heap built with largest value put in first",
    					randomgen.peek() == (Integer) 99);
    	// Insert another value on random to check largest-inserted-last condition
    	randomgen.insert((Integer) 100); 
    	assertTrue("Max element is incorrect for heap built with largest value put in last",
    					randomgen.peek() == (Integer) 100);

    	// Now check that the max value is true over a range of values
    	for (int i = 0; i < differentsizes.length; i++) {
    		assertTrue("Max element was incorrect when values vary.", differentsizes[i].peek() == (Integer) i);
    	}
    }

    /* Test remove() on empty MaxPQ (check that error is thrown) */
    @Test
    public void testRemoveMaxEmpty() {
        Integer i = null;
        try {
            i = empty.peek();
            empty.remove();
            fail("Expected a QueueEmptyException to be thrown");
        } catch (QueueEmptyException q) {
            assertEquals("remove() returns a value with empty list", 
                    null, i);
            assertEquals("remove() changes queue size with empty list", 0, empty.size());
            assertTrue("remove() makes empty queue no longer empty", 
                    empty.isEmpty());
        }
    }

    /* Test remove() on non-empty MaxPQ */
    @Test
    public void removeSmallToLarge() {
    	// Note the removed value should also be the size of the remaining MaxPQ
    	// This construction means we don't have to rely on peek() to know what size to check for
    	// This construction allows us to not rely on size() to know the size of the remaining PQ
    	// but still requires us to use size() to check that the size is as expected.
    	// Tests the range of values up to but not including empty MaxPQ
    	Integer removed = smalltolarge.peek();
        smalltolarge.remove();
    	assertTrue("Remove Max didn't return correct value.", removed == (99));
		assertTrue("Remove Max didn't update size.", smalltolarge.size() == (99));
    	while(!smalltolarge.isEmpty()) {
            removed--;
    		assertTrue("Remove Max didn't return correct value. Got " + smalltolarge.peek() + " expected " +
                removed, smalltolarge.peek() == (removed));
            smalltolarge.remove();
    		assertTrue("Remove Max didn't update size.", smalltolarge.size() == (removed));
    	}
    }
    
    @Test
    public void removeLargeToSmall() {
    	// Note the removed value should also be the size of the remaining MaxPQ
    	// This construction means we don't have to rely on peek() to know what size to check for
    	// This construction allows us to not rely on size() to know the size of the remaining PQ
    	// but still requires us to use size() to check that the size is as expected.
    	// Tests the range of values up to but not including empty MaxPQ
    	Integer removed = largetosmall.peek();
        largetosmall.remove();
    	assertTrue("Remove Max didn't return correct value.", removed == (99));
		assertTrue("Remove Max didn't update size.", largetosmall.size() == (99));
    	while(!largetosmall.isEmpty()) {
    		removed--;
    		assertTrue("Remove Max didn't return correct value.", largetosmall.peek() == (removed));
            largetosmall.remove();
    		assertTrue("Remove Max didn't update size.", largetosmall.size() == (removed));
    	}
    }

    @Test
    public void removeAllSame() {
    	int size = 100;
    	while(!allsame.isEmpty()) {
    		size--;
    		assertTrue("Remove Max didn't return correct value.", allsame.peek() == (1));
            allsame.remove();
    		assertTrue("Remove Max didn't update size.", allsame.size() == (size));
    	}
    }

    /* test insert from empty list */
    @Test
    public void testInsertEmpty() {
        empty.insert(1);
        assertFalse("Previously empty is still empty after insert", 
                empty.isEmpty());
        assertEquals("Size does not update with insert of empty size", 
                1, empty.size());
        try {
            assertTrue("MaxVal not updated after insert on empty list",
                empty.peek() == 1);
        } catch (QueueEmptyException q) {
            fail("MaxVal was not updated and QueueEmptyException was thrown");
        }
    }

    /* Test insert() on non-empty lists */

    /* Inserts values smaller than the max value */
    @Test
    public void testInsertSmallerValsNonEmpty() {
    	for (int i = 1; i <= 20; i++) {
    		smalltolarge.insert(i - 11);
    		largetosmall.insert(i - 11);
    		randomgen.insert(i - 11);

    		// Check that size gets updated
    		assertTrue("Size not updated on insert", smalltolarge.size() == (100 + i));
    		assertTrue("Size not updated on insert", largetosmall.size() == (100 + i));
    		assertTrue("Size not updated on insert", randomgen.size() == (100 + i));

    		// Check that maxVal is unchanged
    		assertTrue("MaxVal changed on insert of value < max", smalltolarge.peek() == (99));
    		assertTrue("MaxVal changed on insert of value < max", largetosmall.peek() == (99));
    		assertTrue("MaxVal changed on insert of value < max", randomgen.peek() == (99));

    	}
    }

    /* Inserts values larger than/ equal to the max value */
    @Test
    public void testInsertLargerValsNonEmpty() {
    	for (int i = 1; i <= 11; i++) {
    		smalltolarge.insert(i + 99);
    		largetosmall.insert(i + 99);
    		randomgen.insert(i + 99);

    		// Check that size gets updated
    		assertTrue("Size not updated on insert", smalltolarge.size() == (100 + i));
    		assertTrue("Size not updated on insert", largetosmall.size() == (100 + i));
    		assertTrue("Size not updated on insert", randomgen.size() == (100 + i));

    		// Check that maxVal is correct
    		assertTrue("MaxVal not updated when insert value >= max", smalltolarge.peek() == (99 + i));
    		assertTrue("MaxVal not updated when insert value >= max", largetosmall.peek() == (99 + i));
    		assertTrue("MaxVal not updated when insert value >= max", randomgen.peek() == (99 + i));

    	}
    }

    @Test
    public void testInertionOfDuplicates() {
    	// Repeatedly insert 1's to allsame
    	for (int i = 1; i <= 11; i++) {
    		allsame.insert(1);
    		assertTrue("Size not updated when inserting duplicates", allsame.size() == (100 + i));
    		assertTrue("MaxVal changed when inserting a duplicate value", allsame.peek() == 1);
    	}

    	//Repeatedly insert duplicate random values to randomgen
        int random;
        for (int i = 1; i < 100; i++) {
            // Ensures repeats and that 99 is the largest value
        	random = (Integer)(int)(Math.random() * 50 + 1);
        	randomgen.insert((Integer) random);

    		assertTrue("Size not updated when inserting duplicates", randomgen.size() == (100 + i));
    		assertTrue("MaxVal changed when inserting submaximal duplicate value", randomgen.peek() == 99);
        }
    }

    @Test
    public void testInit() {
        List<Integer> list = new LinkedList();
        List<Integer> bigSmall = new LinkedList();
        List<Integer> smallBig = new LinkedList();
        List<Integer> same = new LinkedList();

        for (int i = 0; i < 10; i++) {
            bigSmall.add(9-i);
            smallBig.add(i);
            same.add(1);
        }

        list.add(6); list.add(3); list.add(9); list.add(2); list.add(3); list.add(8); list.add(5); list.add(2); list.add(9); list.add(10);

        // initialize PQs with arraylists
        empty.init(list);
        smalltolarge.init(smallBig);
        largetosmall.init(bigSmall);
        allsame.init(same);

        assertTrue("Empty queue is still empty after init()", !empty.isEmpty());
        assertTrue("Size of queue does not match size of Collection in init()", empty.size() == 10);
        assertTrue("MaxVal is incorrect after init to empty list" + empty.peek(), empty.peek() == 10);
        assertTrue("Non-empty queue is empty after init()", !smalltolarge.isEmpty());
        assertTrue("Size of queue does not match size of Collection in init()", smalltolarge.size() == 10);
        assertTrue("MaxVal is incorrect after init to non-empty list", smalltolarge.peek() == 9);
        assertTrue("Non-empty queue is empty after init()", !largetosmall.isEmpty());
        assertTrue("Size of queue does not match size of Collection in init()", largetosmall.size() == 10);
        assertTrue("MaxVal is incorrect after init to non-empty list" + largetosmall.peek(), largetosmall.peek() == 9);
        assertTrue("Non-empty queue is empty after init()", !allsame.isEmpty());
        assertTrue("Size of queue does not match size of Collection in init()", allsame.size() == 10);
        assertTrue("MaxVal is incorrect after init to non-empty list", allsame.peek() == 1);
    
        for (int i = 0; i < 10; i++) {
            assertEquals("Queue with increasing values' init does not match up with expected values", smallBig.get(9 - i), smalltolarge.peek());
            assertEquals("Queue with decreasing values' init does not match up with expected values", bigSmall.get(i), largetosmall.peek());
            assertEquals("Queue with same values' init does not match up with expected values", same.get(i), allsame.peek());

            empty.remove();
            smalltolarge.remove();
            largetosmall.remove();
            allsame.remove();

        }
    }
}