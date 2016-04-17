/** Implementation of an edge class (for graphs), could be directed or not.
 */
public class WEdge<VT> implements Comparable<WEdge<VT>> {

    /** Starting vertex of an edge. */
    private GVertex<VT> source;
    /** Ending vertex of an edge. */
    private GVertex<VT> end;
    /** Weight of the edge */
    private double weight;

    /** Create an undirected edge.
     *  @param u the start
     *  @param v the end
     */

    // How do we initialize a default generic value N for weight?
    public WEdge(GVertex<VT> u, GVertex<VT> v) {
        this.source = u;
        this.end = v;
        this.weight = 0; // initialize weight to 0
    }

    /** Create an edge.
     *  @param u the start
     *  @param v the end
     *  @param dir true if directed, false otherwise
     */
    public WEdge(GVertex<VT> u, GVertex<VT> v, double w) {
        this.source = u;
        this.end = v;
        this.weight = w;
    }

    /** Is a vertex incident to this edge.
     *  @param v the vertex
     *  @return true if source or end, false otherwise
     */
    public boolean isIncident(GVertex<VT> v) {
        return this.source.equals(v) || this.end.equals(v);
    }

    /** Get the starting endpoint vertex.
     *  @return the vertex
     */
    public GVertex<VT> source() {
        return this.source;
    }

    /** Get the ending endpoint vertex.
     *  @return the vertex
     */
    public GVertex<VT> end() {
        return this.end;
    }

    public boolean hasVertex(GVertex<VT> v) {
        return (this.source.equals(v) || this.end.equals(v));
    }

    /** Create a string representation of the edge.
     *  @return the string as (source,end)
     */
    public String toString() {
        return "(" + this.source.toString() + ", " + this.end.toString() + ", " + this.weight + ")";
    }

    /** Check if two edges are the same.
     *  @param other the edge to compare to this
     *  @return true if directedness and endpoints match, false otherwise
     */
    public boolean equals(Object other) {
        if (other instanceof WEdge) {
            WEdge<VT> e = (WEdge<VT>) other;
            return this.source.equals(e.source)
                && this.end.equals(e.end)
                || this.source.equals(e.end)
                && this.end.equals(e.source);
        }
        return false;
    }
    
    /** Make a hashCode based on the toString.
     *  @return the hashCode
     */
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public int compareTo(WEdge<VT> other) {
        // not sure if this is good
        if (this.weight > other.weight) {
            return 1;
        } else if (this.weight < other.weight) {
            return -1;
        }
        return 0;
    }
    
    /** Gets the weight of WEdge.
     * 
     * @return the weight (double)
     */
    public double weight(){
        return this.weight;
    }
    
    /** Sets the weight of the WEdge to a new (double)
     * 
     * @param x : the new weight
     */
    public void setWeight(double x){
        this.weight = x;
    }

}
