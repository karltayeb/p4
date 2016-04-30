/** Natasha Bornhorst nbornho1 Richard Ding rding2 Karl Tayeb ktayeb1 */
/** cs226 section 2 project 4 */
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;

/** WGraph Class. 
 *  @param <VT> vt. */
public class WGraphP4<VT> implements WGraph<VT> {

    /** Used to sequentially generate vertex IDs for this graph! */
    private int nextID;

    /** the vertices. */
    private ArrayList<GVertex<VT>> verts;
    /** the edges. */
    private ArrayList<ArrayList<WEdge<VT>>> adjlist;
    /** num edges. */
    private int numEdges;

    // I made it not take a max number of pixels 
    // since that doesnt make sense anymore
    /** WGraph constructor. */
    public WGraphP4() {
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
        return this.numEdges;
    }

    @Override
    public int nextID() {
        return this.nextID++;
    }

    @Override
    public boolean addVertex(Object data) {
        int properIndex = this.nextID;
        this.verts.add(new GVertex(data, this.nextID++));
        // add adjacency list for this vertex, 
        // index should corresponds with vertex ID
        // ensures that adjlist's size is the same as the index of the vertice
        for (int i = this.adjlist.size(); i < properIndex + 1; i++) {
            this.adjlist.add(new ArrayList<WEdge<VT>>());
        }
        return true;
    }


    @Override
    public boolean addVertex(GVertex<VT> v) {
        if (this.verts.contains(v)) {
            return false;  // there 
        }
        int properIndex = v.id();
        this.verts.add(v);
        // add adjacency list for this vertex, 
        // index should corresponds with vertex ID
        // ensures that adjlist's .size is the same as the 
        for (int i = this.adjlist.size(); i < properIndex + 1; i++) {
            this.adjlist.add(new ArrayList<WEdge<VT>>());
        }
        
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
        if (this.adjlist.size() < v.id()){
            for (int i = this.adjlist.size(); i <= v.id() + 1; i++) {
                this.adjlist.add(new ArrayList<WEdge<VT>>());
            }
            success = this.addVertex(v);            
        }
        if (this.adjlist.size() < u.id()){
            for (int i = this.adjlist.size(); i <= u.id() + 1; i++) {
                this.adjlist.add(new ArrayList<WEdge<VT>>());
            }
            success = this.addVertex(u);
        }

        if (!success)
            return false;
        }

        // put the edge in, if not already there
        boolean edgeExists = this.areAdjacent(v, u);
        if (!edgeExists) {
            this.adjlist.get(v.id()).add(new WEdge<VT>(v, u, w));
            this.adjlist.get(u.id()).add(
                this.adjlist.get(v.id()).get(this.adjlist.get(v.id()).size() - 1));
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
            if (marker.source().id() != v.id()) {
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
    /* Note: allEdges() returns edges such that source.id() < end.id();
     * (non-Javadoc)
     * @see WGraph#allEdges()
     */
    public List<WEdge<VT>> allEdges() {
        int nv = this.numVerts();
        HashSet<WEdge<VT>> edgeSet = new HashSet<WEdge<VT>>(nv);
        for (ArrayList<WEdge<VT>> edgelist : this.adjlist) {
            for (WEdge<VT> entry : edgelist) {
                if (!edgeSet.contains(entry)) {
                    edgeSet.add(entry);
                }
            }
        }

        ArrayList<WEdge<VT>> edges = new ArrayList<WEdge<VT>>();
        edges.addAll(edgeSet);
        return edges;
    }

    @Override
    public List<GVertex<VT>> allVertices() {
        return this.verts;
    }

    /** depthFirst search. 
 *      @param v v. 
 *      @return List<GVertex<VT>> vt. */
    public List<GVertex<VT>> depthFirst(GVertex<VT> v) {
        List<GVertex<VT>> vlist = new ArrayList<GVertex<VT>>();
        Stack<GVertex<VT>> slist = new Stack<GVertex<VT>>();
        Set<GVertex<VT>> visited = new HashSet<GVertex<VT>>();
        slist.push(v);
       
        while (!slist.empty()) {
            GVertex<VT> newV = (GVertex<VT>) slist.pop();
            if (!visited.contains(newV)) {
                visited.add(newV);
                vlist.add(newV);
 
                ArrayList<GVertex<VT>> nlist = this.neighbors(newV);    
                for (int i = 0; i < nlist.size(); i++) {
                    GVertex<VT> neighbor = nlist.get(i);
                    if (!visited.contains(neighbor)) {
                        slist.push(neighbor);
                    }
                }
            }
        }
        return vlist;
        
    }

    /** Return a list of all the edges incident on vertex v.  
     *  @param v the starting vertex
     *  @return the incident edges
     */
    @Override
    public List<WEdge<VT>> incidentEdges(GVertex<VT> v) {
        return this.adjlist.get(v.id());
    }
    
    /** Return a list of edges in a minimum spanning forest by
     *  implementing Kruskal's algorithm using fast union/finds.
     *  @return a list of the edges in the minimum spanning forest
     */
    @Override
    public List<WEdge<VT>> kruskals() {
        Partition roots = new Partition(this.allVertices().size());
        List<WEdge<VT>> mst = new ArrayList<WEdge<VT>>();
        List<WEdge<VT>> edges = this.allEdges();

        //heap contains all the edges of the graph
        PQHeap<WEdge<VT>> heap = new PQHeap<WEdge<VT>>(new ReverseComparator());
        heap.init(this.allEdges());
      
        // process all the edges IN ORDER OF WEIGHT    
        while (!heap.isEmpty()) {
            WEdge<VT> current = heap.peek();
            int root1 = roots.find(current.source().id());   
            int root2 = roots.find(current.end().id());     
             
            //if the two roots are NOT equal, then union and add to MST
            //otherwise, do nothing
            if (root1 != root2) {
                mst.add(current);
                roots.union(root1, root2);
            }
            //remove the processed edge
            heap.remove();
        }
         
        return mst;
    }

    /** This method removes all the edges in the graph. */
    public void clearEdges() {
        for (List<WEdge<VT>> list : this.adjlist) {
            list.clear();
        }
    }
     
    /** A comparator to make PQHeap a min-heap.
     *
     * @param <T>
     */
    private static class ReverseComparator<T extends Comparable<T>> 
                                                implements Comparator<T> {
        /** compares T values. 
 *          @param t1 t1.
 *          @param t2 t2.
 *          @return int. */
        public int compare(T t1, T t2) {
            return t2.compareTo(t1);
        }
    }
}
