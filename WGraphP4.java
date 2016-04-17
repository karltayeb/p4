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
        int count = 0; 
        for (list : adjlist) {
            count += list.size();
        }
        return count;
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
        this.vert.setID(this.nextID++);
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
                    this.adjlist(u.id()).remove(entry);
                    this.adjlist(v.id()).remove(entry);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean areAdjacent(GVertex<VT> v, GVertex<VT> u) {
        // You only need to check one list, since they should both be there
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

    public List<GVertex<VT>> depthFirst(GVertex<VT> v) {
        return null;
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
