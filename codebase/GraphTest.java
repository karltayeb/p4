/**
 *Karl Tayeb ktayeb1
 *Natasha Bornhorst nbornho1
 *Richard Ding rding2
 *CS226.02 
 *P4C
 */

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import static org.junit.Assert.assertTrue;

import java.util.List;

import static org.junit.Assert.assertFalse;

/** Set of Junit tests for our Graph implementations.
 */
public class WGraphTest {
    WGraphP4<Character> g;
    GVertex<Character> v, u, x, y, a, b, c, q, w, r;
    WEdge<Character> ac, aq, qb, bw, wr, br, wq, cw, rc, e, f;

    @Before
    public void setupGraph() {
        g = new WGraphP4();
        v = new GVertex<Character>('v', g.nextID());
        u = new GVertex<Character>('u', g.nextID());
        x = new GVertex<Character>('x', g.nextID());
        y = new GVertex<Character>('y', g.nextID());
        a = new GVertex<Character>('a', g.nextID());
        b = new GVertex<Character>('b', g.nextID());
        c = new GVertex<Character>('c', g.nextID());
        q = new GVertex<Character>('q', g.nextID());
        w = new GVertex<Character>('w', g.nextID());
        r = new GVertex<Character>('r', g.nextID());


        ac = new WEdge<Character>(a, c, 1);
        aq = new WEdge<Character>(a, q, 2);
        qb = new WEdge<Character>(q, b, 3);
        bw = new WEdge<Character>(b, w, 4);
        wr = new WEdge<Character>(w, r, 5);
        br = new WEdge<Character>(b, r, 6);
        wq = new WEdge<Character>(w, q, 7);
        cw = new WEdge<Character>(c, w, 8);
        rc = new WEdge<Character>(r, c, 9);

        e = new WEdge<Character>(v, u, 1);
        f = new WEdge<Character>(v, x, 2);
    }

    @Test
    public void testEmpty() {
        assertEquals(0, g.numEdges());
        assertEquals(0, g.numVerts());
    }

    @Test
    public void testAddVertex() {
        assertEquals(true, g.addVertex(v));
        assertEquals(true, g.addVertex(u));
        assertEquals(false, g.addVertex(v));
    }

    @Test
    public void testAddEdge() {
        assertEquals(true, g.addEdge(e));
        assertEquals(true, g.addEdge(v, x, 1));
        assertEquals(false, g.addEdge(v, u, 1));
        assertEquals(false, g.addEdge(f));
    }

    @Test
    public void testAdjacency() {
        g.addVertex(v);
        g.addVertex(u);
        g.addVertex(x);
        g.addVertex(y);
        assertEquals(false, g.areAdjacent(u, v));
        g.addEdge(e);
        g.addEdge(f);
        assertEquals(true, g.areAdjacent(u, v));
        assertEquals(true, g.areAdjacent(v, u));
        assertEquals(true, g.areAdjacent(v, x));
        assertEquals(false, g.areAdjacent(x, u));
        assertEquals(false, g.areAdjacent(v, y));
    }

    @Test
    public void testIncidence() {
        g.addVertex(v);
        g.addVertex(u);
        g.addVertex(x);
        g.addVertex(y);
        g.addEdge(e);
        assertEquals(false, g.areIncident(e, x));
        assertEquals(false, g.areIncident(e, y));
        assertEquals(true, g.areIncident(e, v));
        assertEquals(true, g.areIncident(e, u));
        g.addEdge(f);
        assertEquals(true, g.areIncident(f, x));
        assertEquals(false, g.areIncident(f, u));
        assertEquals(4, g.numVerts());
        assertEquals(2, g.numEdges());
        //add tests for incidentEdges:

    }

    @Test
    public void testDegree() {
        g.addVertex(v);
        g.addVertex(u);
        g.addVertex(x);
        g.addVertex(y);
        assertEquals(0, g.degree(v));
        g.addEdge(e);
        assertEquals(1, g.degree(v));
        g.addEdge(f);
        assertEquals(2, g.degree(v));
        assertEquals(1, g.degree(x));
        assertEquals(0, g.degree(y));
    }


    @Test
    public void testNeighbors() {
        g.addVertex(v);
        g.addVertex(u);
        g.addVertex(x);
        g.addVertex(y);
        assertEquals("[]", g.neighbors(v).toString());
        g.addEdge(e);
        //        System.out.println(g.neighbors(v).toCharacter());
        assertEquals("[1]", g.neighbors(v).toString());
        assertEquals("[0]", g.neighbors(u).toString());
        g.addEdge(f);
        assertEquals("[1, 2]", g.neighbors(v).toString());
        assertEquals("[0]", g.neighbors(u).toString());
        assertEquals("[0]", g.neighbors(x).toString());
        assertEquals("[]", g.neighbors(y).toString());
    }

    @Test
    public void testVertices() {

        assertEquals("[]", g.allVertices().toString());
               
        g.addVertex(a);
        assertEquals("[4]", g.allVertices().toString());
        g.addVertex(b);
        assertEquals("[4, 5]", g.allVertices().toString());
        g.addVertex(c);
        assertEquals("[4, 5, 6]", g.allVertices().toString());
        g.addVertex(q);
        assertEquals("[4, 5, 6, 7]", g.allVertices().toString()); 
        g.addVertex(w);
        assertEquals("[4, 5, 6, 7, 8]", g.allVertices().toString());
        g.addVertex(r);
        assertEquals("[4, 5, 6, 7, 8, 9]", g.allVertices().toString());
        assertFalse(g.addVertex(r));

    }

    @Test
    public void testEdges() {
        assertEquals("[]", g.allEdges().toString());
               
        g.addVertex(a);
        g.addVertex(b);
        g.addVertex(c);
        g.addVertex(q);
        g.addVertex(w);
        g.addVertex(r);
        g.addEdge(ac);        
        assertEquals("[(4, 6, 1.0)]", g.allEdges().toString());
        g.addEdge(aq);
        assertEquals("[(4, 6, 1.0), (4, 7, 2.0)]", g.allEdges().toString());        
        g.addEdge(qb);
        /* NOTE:
         * AllEdges returns edges such that source.id() < end.id()
         * aka: WEdge(source, end, weight) where source < end 
         */
        assertEquals("[(4, 6, 1.0), (4, 7, 2.0), (5, 7, 3.0)]", g.allEdges().toString());
        g.addEdge(bw);
        assertEquals("[(4, 6, 1.0), (4, 7, 2.0), (5, 7, 3.0), (5, 8, 4.0)]", g.allEdges().toString());
        g.addEdge(wr);
        assertEquals("[(4, 6, 1.0), (4, 7, 2.0), (5, 7, 3.0), (5, 8, 4.0), (8, 9, 5.0)]", g.allEdges().toString());
        g.addEdge(br);
        assertEquals("[(4, 6, 1.0), (4, 7, 2.0), (5, 7, 3.0), (5, 8, 4.0), (5, 9, 6.0), (8, 9, 5.0)]", g.allEdges().toString());
        assertFalse(g.addEdge(bw));
        assertEquals("[(4, 6, 1.0), (4, 7, 2.0), (5, 7, 3.0), (5, 8, 4.0), (5, 9, 6.0), (8, 9, 5.0)]", g.allEdges().toString());
        assertTrue(g.deleteEdge(q, b));
        assertEquals("[(4, 6, 1.0), (4, 7, 2.0), (5, 8, 4.0), (5, 9, 6.0), (8, 9, 5.0)]", g.allEdges().toString());
        assertTrue(g.deleteEdge(w, r));
        assertEquals("[(4, 6, 1.0), (4, 7, 2.0), (5, 8, 4.0), (5, 9, 6.0)]", g.allEdges().toString());
        assertTrue(g.deleteEdge(b, r));
        assertEquals("[(4, 6, 1.0), (4, 7, 2.0), (5, 8, 4.0)]", g.allEdges().toString());
        assertTrue(g.deleteEdge(a, q));
        assertEquals("[(4, 6, 1.0), (5, 8, 4.0)]", g.allEdges().toString());
        assertTrue(g.deleteEdge(a, c));
        assertEquals("[(5, 8, 4.0)]", g.allEdges().toString());
        assertTrue(g.deleteEdge(b, w));
        assertFalse(g.deleteEdge(w, b));
        
    }
    
    /**
     * Uses the example from the DSA textbook to test Kruskals
     */
    @Test
    public void testKruskals() {
        /** Use these vertices
        0. v = new GVertex<Character>('v', g.nextID());
        1. u = new GVertex<Character>('u', g.nextID());
        2. x = new GVertex<Character>('x', g.nextID());
        3. y = new GVertex<Character>('y', g.nextID());
        4. a = new GVertex<Character>('a', g.nextID());
        5. b = new GVertex<Character>('b', g.nextID());
        */

        WEdge<Character> xu = new WEdge<Character>(x, u, 5);
        WEdge<Character> xy = new WEdge<Character>(x, y, 1);
        WEdge<Character> xb = new WEdge<Character>(x, b, 2);
        WEdge<Character> yb = new WEdge<Character>(y, b, 2);
        WEdge<Character> ab = new WEdge<Character>(a, b, 1);
        WEdge<Character> bu = new WEdge<Character>(b, u, 6);
        WEdge<Character> vx = new WEdge<Character>(v, x, 7);
        WEdge<Character> va = new WEdge<Character>(v, a, 9);
        

        g.addEdge(xu);
        g.addEdge(xy);
        g.addEdge(xb);
        g.addEdge(yb);
        g.addEdge(ab);
        g.addEdge(bu);
        g.addEdge(vx);
        g.addEdge(va);
        
        List<WEdge<Character>> mst = g.kruskals();
        assertTrue(mst.contains(xy));
        assertTrue(mst.contains(ab));
        assertTrue(mst.contains(yb));
        assertTrue(mst.contains(xu));
        assertTrue(mst.contains(vx));
    }

    @Test
    public void testDepthFirst() {
    
        g.addVertex(c);
        List dfirst = g.depthFirst(c);
        assertEquals(1, dfirst.size());
        assertEquals(c, dfirst.get(0));

        g.addVertex(a);
        g.addVertex(q);
        g.addVertex(b);
        g.addVertex(w);
        g.addVertex(r);
        g.addVertex(v);

        g.addEdge(ac);
        g.addEdge(aq);
        g.addEdge(qb);
        g.addEdge(bw);
        g.addEdge(wr);


        g.addEdge(rc);
        dfirst = g.depthFirst(c);
        assertEquals(6, dfirst.size());   
        assertEquals(c, dfirst.get(0));
        assertEquals(r, dfirst.get(1));
        assertEquals(w, dfirst.get(2));
        assertEquals(b, dfirst.get(3));
        assertEquals(q, dfirst.get(4));
        assertEquals(a, dfirst.get(5));

        g.deleteEdge(r, c);
        dfirst = g.depthFirst(c);
        assertEquals(6, dfirst.size());   
        assertEquals(c, dfirst.get(0));
        assertEquals(a, dfirst.get(1));
        assertEquals(q, dfirst.get(2));
        assertEquals(b, dfirst.get(3));
        assertEquals(w, dfirst.get(4));
        assertEquals(r, dfirst.get(5));

        g.addEdge(wq);
        dfirst = g.depthFirst(c);

        assertEquals(6, dfirst.size());   
        assertEquals(c, dfirst.get(0));
        assertEquals(a, dfirst.get(1));
        assertEquals(q, dfirst.get(2));
        assertEquals(w, dfirst.get(3));
        assertEquals(r, dfirst.get(4));
        assertEquals(b, dfirst.get(5));

        g.addEdge(cw);
        dfirst = g.depthFirst(c);
        assertEquals(6, dfirst.size());   
        assertEquals(c, dfirst.get(0));
        assertEquals(w, dfirst.get(1));
        assertEquals(q, dfirst.get(2));
        assertEquals(b, dfirst.get(3));
        assertEquals(a, dfirst.get(4));
        assertEquals(r, dfirst.get(5));
 


    }



 


}
