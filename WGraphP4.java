import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;

public class WGraphP4<VT> implements WGraph<VT> {

    /** Used to sequentially generate vertex IDs for this graph! */
    private int nextID;

    /** the vertices */
    private ArrayList<GVertex<VT>> verts;
    private ArrayList<ArrayList<WEdge<VT>>> adjlist;
    private int numEdges;

    public WGraphP4(int maxVerts) {
        this.nextID = 0;
        this.numEdges = 0;
        this.verts = new ArrayList<GVertex<VT>>();
        this.adjlist = new ArrayList<ArrayList<WEdge<VT>>>();
    }

    @Override
    public int numVerts() {
        return this.verts.size();
    }

    @Override
    public int numEdges() {
        /**
        int count = 0; 
        for (ArrayList<WEdge<VT>> list : adjlist) {
            count += list.size();
        }
        return count/2;
        */
        return this.numEdges;
    }

    @Override
    public int nextID() {
        return nextID++;
    }

    @Override
    public boolean addVertex(Object data) {
        this.verts.add(new GVertex(data, this.nextID++));
        // add adjacency list for this vertex, inex should corresponds with vertex ID
        this.adjlist.add(new ArrayList<WEdge<VT>>());
        return true;
    }


    @Override
    public boolean addVertex(GVertex<VT> v) {
        if (this.verts.contains(v)) {
            return false;  // there 
        }
        this.verts.add(v);
        // add adjacency list for this vertex, inex should corresponds with vertex ID
        this.adjlist.add(new ArrayList<WEdge<VT>>());
        return true;
    }
    
    /**
     * @author Richard
     */
    @Override
    public boolean addEdge(WEdge<VT> e) {
        boolean added = false;
        added = this.addEdge(e.source(), e.end(), e.weight());
        return added;
    }

    /**
     * @author Richard
     */
    @Override
    public boolean addEdge(GVertex<VT> v, GVertex<VT> u, double w) {
        boolean success = true;
        if (!this.verts.contains(v))
            success = this.addVertex(v);
        if (success && !this.verts.contains(u))
            success = this.addVertex(u);
        if (!success)
            return false;

        // put the edge in, if not already there
        boolean edgeExists = this.areAdjacent(v, u);
        if (!edgeExists) {
            this.adjlist.get(v.id()).add(new WEdge<VT>(v, u, w));
            this.adjlist.get(u.id()).add(new WEdge<VT>(v, u, w));
            this.numEdges++;
            return true;
        }
        return false;  // was already there
    }

    
    @Override
    public boolean deleteEdge(GVertex<VT> v, GVertex<VT> u) {
        if (this.areAdjacent(v, u)) {
            // go to the list of vertex v and remove edge
            for (WEdge<VT> entry : this.adjlist.get(v.id())) {
                if (entry.hasVertex(u)) {
                    // remove edge from both vertices ajdacency lists
                    // TODO check if this works (does source/end matter here?)
                    this.adjlist.get(u.id()).remove(entry);
                    this.adjlist.get(v.id()).remove(entry);
                    this.numEdges--;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean areAdjacent(GVertex<VT> v, GVertex<VT> u) {
        // You only need to check one list, since they should both be there
        if (this.adjlist.isEmpty()) {
            return false;
        }
        if (this.adjlist.get(v.id()).isEmpty()) {
            return false;
        }
        for (WEdge<VT> entry : this.adjlist.get(v.id())) {
            if (entry.hasVertex(u)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<GVertex<VT>> neighbors(GVertex<VT> v) {
        ArrayList<GVertex<VT>> temp = new ArrayList<GVertex<VT>>();
        for (WEdge<VT> marker: this.adjlist.get(v.id())) {
            if(marker.source().id() != v.id()) {
                //the "source" vertex is the other vertex of the edge, not "v"
                temp.add(marker.source());
            } else {
                //the "end" vertex is the other vertex of the edge, not "v"
                temp.add(marker.end());
            }
        }
        return temp;
    }

    @Override
    public int degree(GVertex<VT> v) {
        return this.adjlist.get(v.id()).size();
    }

    @Override
    public boolean areIncident(WEdge<VT> e, GVertex<VT> v) {
        return e.hasVertex(v);
    }

    @Override
    public List<WEdge<VT>> allEdges() {
        // This is super inefficient right now
        int nv = this.numVerts();
        ArrayList<WEdge<VT>> edges = new ArrayList<WEdge<VT>>(nv);
        for (ArrayList<WEdge<VT>> edgelist : this.adjlist) {
            for (WEdge<VT> entry : edgelist) {
                if (!edges.contains(entry)){
                    edges.add(entry);
                }
            }
        }
        return edges;
    }

    @Override
    public List<GVertex<VT>> allVertices() {
        return this.verts;
    }

    //TODO
    public List<GVertex<VT>> depthFirst(GVertex<VT> v) {
        boolean bool = true;
        List<GVertex<VT>> vlist = new ArrayList<GVertex<VT>>();
        Stack slist = new Stack();
        Set<GVertex<VT>> visited = new HashSet();
        slist.push(v);
       
        while(!slist.empty()){
            GVertex<VT> newV = (GVertex<VT>)slist.pop();
            if(!visited.contains(newV)) {
                visited.add(newV);
                vlist.add(newV);
 
                ArrayList<GVertex<VT>> nlist = neighbors(newV);    
                for(int i = 0; i < nlist.size(); i++) {
                    GVertex<VT> neighbor = nlist.get(i);
                    if (!visited.contains(neighbor)) {
                        slist.push(neighbor);
                    }
                }
            }

        }
        return vlist;
        
    }


    //TODO
    /** Return a list of all the edges incident on vertex v.  
     *  @param v the starting vertex
     *  @return the incident edges
     */
    @Override
    public List<WEdge<VT>> incidentEdges(GVertex<VT> v) {
        return null;
    }

    //TODO
    /** Return a list of edges in a minimum spanning forest by
     *  implementing Kruskal's algorithm using fast union/finds.
     *  @return a list of the edges in the minimum spanning forest
     */
    @Override
    public List<WEdge<VT>> kruskals() {
        List<WEdge<VT>> edges = this.allEdges();
        //roots is the array that holds the roots of each node (Gvertex);
        int[] roots = new int[this.allVertices().size()];
        for (int i = 0; i< roots.length; i++){
            roots[i] = i;
        }
        //each node is its own root initially
                
        //heap contains all the edges of the graph
        PQHeap<WEdge<VT>> heap = new PQHeap<WEdge<VT>>(new ReverseComparator());
        for (int i = 0; i < edges.size(); i++) {
            //add all edges into min-heap
            heap.insert(edges.remove(0));
        }
        
        List<WEdge<VT>> MST = new ArrayList<WEdge<VT>>();
        WEdge<VT> temp;
        int sourceID, endID;
        //Use removemin() to process edges correctly
        for (int i = 0; i< heap.size(); i++) {
            temp = heap.peek(); //finds the min Edge
            sourceID = temp.source().id();
            endID = temp.end().id();
            if (findRoot(roots, sourceID) != findRoot(roots, endID)) {  
                //Union Nodes if different roots  
                roots[endID] = sourceID;
                //if different roots, then edge is part of MST
                MST.add(temp);
            }
            heap.remove();  //removes the proccessed min Edge
        }
        
        return MST;
    }
    
    /** For the min-heap.
     * 
     * @author Richard
     *
     * @param <T>
     */
    private static class ReverseComparator<T extends Comparable<T>> implements Comparator<T> {
        public int compare(T t1, T t2) {
            return t2.compareTo(t1);
        }
    }
    
    /** A small private method for tracing through an array to find 
     * the root of a node.  Specifically for use in the kruskals() method. 
     * Assume roots is built correctly as in kruskals.
     * 
     * @param roots the root array
     * @param index the initial node whose root we want
     * @return the final root
     */
    private int findRoot(int[] roots, int index) {
        int finalR = roots[index];
        while(roots[finalR] != finalR){
            //a root should point to itself
            //if it doesn't, then keep going until it does
            finalR = roots[finalR];
        }
        return finalR;
    }
    
    public static void main (String[] args){
        WGraphP4<Character> g = new WGraphP4<Character>(100);
        GVertex<Character> v = new GVertex<Character>('v', g.nextID());
        GVertex<Character> u = new GVertex<Character>('u', g.nextID());
        GVertex<Character> x = new GVertex<Character>('x', g.nextID());
        WEdge e = new WEdge<Character>(u, v, 1);
        WEdge e2 = new WEdge<Character>(v, x, 2);
        g.addVertex(v);
        g.addVertex(u);
        g.addVertex(x);
        g.addEdge(e);
        g.addEdge(e2);


        
        List dfirst = g.depthFirst(u);

        for (int i = 0; i < dfirst.size(); i++) {
 
            System.out.println(i + ". " + dfirst.get(i));

        }

   }
}
