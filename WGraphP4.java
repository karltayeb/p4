import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

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
        for
        return this.numEdges;
    }

    @Override
    public int nextID() {
        return nextID++;
    }

    @Override
    public boolean addVertex(Object data) {
        if (this.verts.size() == this.matrix.length) // full
            return false;
        this.verts.add(new GVertex(data, nextID++));
        return true;
    }

    @Override
    public boolean addVertex(GVertex<VT> v) {
        if (this.verts.size() == this.matrix.length) // full
            return false;
        if (this.verts.contains(v))
            return false;  // there 
        this.verts.add(v);
        return true;
    }
    
    @Override
    public boolean addEdge(WEdge<VT> e) {
        boolean added = false;
        added = addEdge(e.source(), e.end(), 1);
        if (added) {
            added = addEdge(e.end(), e.source(), 1);
            this.numEdges--;  // don't count it twice
        }
        return added;
    }

    @Override
    public boolean addEdge(GVertex<VT> v, GVertex<VT> u, double w) {
        w = 1; 
        boolean success = true;
        if (!this.verts.contains(v))
            success = this.addVertex(v);
        if (success && !this.verts.contains(u))
            success = this.addVertex(u);
        if (!success)
            return false;
        // put the edge in, if not already there
        if (! this.matrix[v.id()][u.id()]) {
            this.matrix[v.id()][u.id()] = true;
            this.numEdges++;
            return true;
        }
        return false;  // was already there
    }

    @Override
    public boolean deleteEdge(GVertex<VT> v, GVertex<VT> u) {
        if (this.verts.contains(v) && this.verts.contains(u)) {
            if (this.matrix[v.id()][u.id()]) {
                this.matrix[v.id()][u.id()] = false;
                this.numEdges--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean areAdjacent(GVertex<VT> v, GVertex<VT> u) {
        return this.matrix[v.id()][u.id()];
    }

    @Override
    public ArrayList<GVertex<VT>> neighbors(GVertex<VT> v) {
        ArrayList<GVertex<VT>> nbs = new ArrayList<GVertex<VT>>(this.numVerts());
        int row = v.id();
        for (int col=0; col < matrix.length; col++) {
            if (this.matrix[row][col]) {
                // add vertex associated with col to nbs
                nbs.add(this.verts.get(col));
            }
        }
        return nbs;
    }

    @Override
    public int degree(GVertex<VT> v) {
        return this.neighbors(v).size();
    }

    @Override
    public boolean areIncident(WEdge<VT> e, GVertex<VT> v) {
        return e.source().equals(v) || e.end().equals(v);
    }

    @Override
    public List<WEdge<VT>> allEdges() {
        int nv = this.numVerts();
        ArrayList<WEdge<VT>> edges = new ArrayList<WEdge<VT>>(nv);
        for (int r = 0; r < nv; r++) {
            for (int c = 0; c < nv; c++) {
                if (this.matrix[r][c]) {
                    // there is an edge, add to list
                    edges.add(new WEdge<VT>(this.verts.get(r), this.verts.get(c)));
                }
                // will create duplicate edges for an undirected graph
            }
        }
        return edges;
    }

    @Override
    public List<GVertex<VT>> allVertices() {
        return this.verts;
    }

    public List<GVertex<VT>> depthFirst(GVertex<VT> v) {
        ArrayList<GVertex<VT>> reaches = new ArrayList<GVertex<VT>>(this.numVerts());
        // using LinkedList<GVertex<VT>> as a Stack
        LinkedList<GVertex<VT>> stack = new LinkedList<GVertex<VT>>();
        boolean[] visited = new boolean[this.numVerts()];  // inits to false
        stack.addFirst(v);
        visited[v.id()] = true;
        while (! stack.isEmpty()) {
            v = stack.removeFirst();
            reaches.add(v);
            for (GVertex<VT> u: this.neighbors(v)) {
                if (! visited[u.id()]) {
                    visited[u.id()] = true;
                    stack.addFirst(u);
                }
            }
        }
        return reaches;
    }



    /** Return a list of all the edges incident on vertex v.  
     *  @param v the starting vertex
     *  @return the incident edges
     */
    @Override
    public List<WEdge<VT>> incidentEdges(GVertex<VT> v) {
        return null;
    }

    /** Return a list of edges in a minimum spanning forest by
     *  implementing Kruskal's algorithm using fast union/finds.
     *  @return a list of the edges in the minimum spanning forest
     */
    @Override
    public List<WEdge<VT>> kruskals() {
        return null;
    }
}
