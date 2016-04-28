import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
//import java.util.LinkedList;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashSet;

public class P4C {


    /** Convert an image to a graph of Pixels with edges between
     *  north, south, east and west neighboring pixels.
     *  @param image the image to convert
     *  @param pd the distance object for pixels
     *  @return the graph that was created
     */
    static WGraph<Pixel> imageToGraph(BufferedImage image, Distance<Pixel> pd) {
    	int height = image.getHeight();
    	int width = image.getWidth();
    	WGraphP4<Pixel> imageGraph = new WGraphP4();
    	List<GVertex<Pixel>> verts;


		// Populate the graph with vertices
		// reads left to right, top down across image
		// the ID of pixel (i,j) is (j*width)+i
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				Pixel pix = new Pixel(i, j, image.getRGB(j, i));
				imageGraph.addVertex(pix);
			}
		}

		// get a list of all the vertices for convenience
		verts = imageGraph.allVertices();

		// Assign edge weights
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				putEdges(imageGraph, verts, i, j, height, width, pd);
			}
		}

		return imageGraph;
	}

	/**
	 * Helper method gives the flat array index of pixel
	 * @param i the row of the current pixel
	 * @param j the col of the the current pixel
	 * @param width the width of the image
	 */
	private static int index (int i, int j, int width) {
		return (i * width) + j;
	}


	/**
	 * Helper method that creates the edges in a new image
	 * checks for boundary cases like edges and corners
	 * @param i the row of the current pixel
	 * @param j the col of the the current pixel
	 */    
	private static void putEdges(WGraphP4<Pixel> graphImage, 
		List<GVertex<Pixel>> verts, int i, int j,
		int height, int width, Distance<Pixel> pd) {

		int index = index(i, j, width);
		int downIndex = index(i + 1, j, width);
		int rightIndex = index(i, j + 1, width);

		GVertex<Pixel> v1 = verts.get(index);
		GVertex<Pixel> v2;

		// check bounds
		if (i < 0 || i >= height || j < 0 || j >= width) {
			// if any of these are true we are out of bounds
			return;
		}

		if (i < (height - 1)) {
			// add the down index if we're not on the last row
			v2 = verts.get(downIndex);
			graphImage.addEdge(v1, v2, pd.distance(v1.data(), v2.data()));
		}

		if (j < (width - 1)) {
			// add the right index if we're not on the rightmost column
			v2 = verts.get(rightIndex);
			graphImage.addEdge(v1, v2, pd.distance(v1.data(), v2.data()));
		}

	}
    
   /** A comparator to make PQHeap a min-heap.
    *
    * @param <T>
    */
   private static class ReverseComparator<T extends Comparable<T>> implements Comparator<T> {
       public int compare(T t1, T t2) {
           return t2.compareTo(t1);
       }
   }
   
    /** Return a list of MSTs of a graph by 
     *  modifying Kruskal's algorithm with partial unions
     *  Aka modified Kruskals.
     *  @param g the graph to segment
     *  @param kvalue the value to use for k in the merge test
     *  @return a list of the edges in the minimum spanning forest
     */

    public static List<WEdge<Pixel>> segmenter(WGraph<Pixel> g, double kvalue) {
        System.out.println("in segmenter");

        Partition roots = new Partition(g.allVertices().size());
        System.out.println("a");
        //ArrayList<ArrayList<WEdge<Pixel>>> MSTGroup = new ArrayList<ArrayList<WEdge<Pixel>>>();
        List<WEdge<Pixel>> MST = new ArrayList<WEdge<Pixel>>();
        List<WEdge<Pixel>> edges = g.allEdges();
        System.out.println("b");

        //set up MSTGroup: to have n arrayLists, where 
        //heap contains all the edges of the graph
        PQHeap<WEdge<Pixel>> heap = new PQHeap<WEdge<Pixel>>(new ReverseComparator<WEdge<Pixel>>());
        System.out.println("getting edges");
        heap.init(g.allEdges());
        System.out.println("got edges");
        //SUPER IMPORTANT!!!!!!!!!!!!!!!!!!!
        //Map to contain all known min/maxes of a set: for O(1) access
        //int[] is length 7: contians minR, minG, minB, maxR, maxG, maxB, SetSize
        //1!!!!!!!!!!1!!!!!!!
        Map<Integer, int[]> knownMinMax = new HashMap<Integer, int[]>();
        
        // process all the edges IN ORDER OF WEIGHT    
        while (!heap.isEmpty()) {
            WEdge<Pixel> current = heap.peek();
            int root1 = roots.find(current.source().id());   
            int root2 = roots.find(current.end().id());     
            
            //if the two roots are NOT equal, then union and add to MST
            //otherwise, do nothing
            //possibleUnion is the data for the unioned set, if it was to be unioned
            int[] possibleUnion = partialUnion(g, roots, root1, root2, kvalue, knownMinMax);
            int newRoot = roots.union2(root1, root2);
            if(newRoot > -1 && possibleUnion[0] == 0){
                //we union the sets
                MST.add(current);                
                knownMinMax.put(newRoot, Arrays.copyOfRange(possibleUnion, 1, possibleUnion.length));
                if (newRoot != root1) {
                    //root1 is the smaller set ABSORBED by the larger set
                    knownMinMax.remove(root1);
                } else {
                    knownMinMax.remove(root2);
                }
            }
            //remove the processed edge
            heap.remove();
        }
        
        //List<List<WEdge<Pixel>>> result = MSTGroup;
        return MST;
    }
    
    /** The Diff(A U B) <= MIN(Diff(A), Diff(B)) + K /(|A| + |B|)
     *  
     * @param p
     * @param root1
     * @param root2
     * @return Array of size 8:
     * index 0: 0 for true, 1 for false
     * index 1-6: minR, minG, minB, maxR, maxG, maxB
     * index 7: size of union
     */
    private static int[] partialUnion(WGraph<Pixel> g, Partition p, int root1, int root2, double k, Map<Integer, int[]> m){
        //int[] is length 7: minR, minG, minB, maxR, maxG, maxB, SetSize
        int[] A = new int[7];
        int[] B = new int[7];
        
        if (m.containsKey(root1)) {
            A = m.get(root1);
        } else {
            /* if "root1" vertex  does not exist in our map, then it has never been merged
             * with any other vertex.  Thus it is a Set of size 1!!!! 
             * 
             * Furthermore, in this case, the value of 
             */
            
            /*List<Pixel> A = new ArrayList<Pixel>();
            for (int i = 0; i < p.getSize(); i++){
                if (p.find(i) == root1) {
                   A.add(g.allVertices().get(i).data()); 
                }
            }
            diffA = diff(A);*/
            A[0] = g.allVertices().get(root1).data().r();
            A[1] = g.allVertices().get(root1).data().g();
            A[2] = g.allVertices().get(root1).data().b();           
            A[3] = g.allVertices().get(root1).data().r();
            A[4] = g.allVertices().get(root1).data().g();
            A[5] = g.allVertices().get(root1).data().b();
            A[6] = 1;
        }
        
        if (m.containsKey(root2)) {
            B = m.get(root2);
        } else {
            /* if "root1" vertex  does not exist in our map, then it has never been merged
             * with any other vertex.  Thus it is a Set of size 1!!!! 
             * 
             * Furthermore, in this case, the value of 
             */
            
            /*List<Pixel> A = new ArrayList<Pixel>();
            for (int i = 0; i < p.getSize(); i++){
                if (p.find(i) == root1) {
                   A.add(g.allVertices().get(i).data()); 
                }
            }
            diffA = diff(A);*/
            B[0] = g.allVertices().get(root2).data().r();
            B[1] = g.allVertices().get(root2).data().g();
            B[2] = g.allVertices().get(root2).data().b();           
            B[3] = g.allVertices().get(root2).data().r();
            B[4] = g.allVertices().get(root2).data().g();
            B[5] = g.allVertices().get(root2).data().b();
            B[6] = 1;
        }
        //A, B are Lists of the Pixels in each Disjoint Set
            
        int[] diffA = diff(A);
        int[] diffB = diff(B);
        int[] AUB = unionMinMax(A,B);   //has the min-max values of the union
        int[] diffAUB = diff(AUB);
        
        int[] minAB = minArray(diffA, diffB);
        
        //checking the condition
        int[] result = new int[8];
        result[0] = 0;  //true
        System.arraycopy(AUB, 0, result, 1, AUB.length);
        for (int i = 0; i < 3; i++){
            if (diffAUB[i] > (minAB[i] + k / (A[6] + B[6]))) {
                //Note: A[6] gives the size of the set A
                //When we fail the required condition:
                result[0] = 1;  //false
                break;
            }
        }
        return result;
    }
    
    /** the Diff function, knowing the min/ max values of the root already
     * 
     * @param seven: the data array of length 7 comprising of:
     * minR, minG, minB, maxR, maxG, maxB, Setsize
     * @return
     */
    private static int[] diff(int[] seven){
        int[] result = new int[3];
        result[0] = seven[3] - seven[0];    //r
        result[1] = seven[4] - seven[1];    //g
        result[2] = seven[5] - seven[2];    //b
        return result;        
    }
    
    /** Finds the min/max values of a union of 2 sets: A and B
     * 
     * array values are minR, minG, minB, maxR, maxG, maxB, Setsize
     * @param rootA
     * @param rootB
     * @return
     */
    private static int[] unionMinMax(int[] A, int[] B) {
        int[] result = new int[7];
        result[0] = Math.min(A[0], B[0]);   //minR
        result[1] = Math.min(A[1], B[1]);   //minG
        result[2] = Math.min(A[2], B[2]);   //minB
        result[3] = Math.max(A[3], B[3]);   //maxR
        result[4] = Math.max(A[4], B[4]);   //maxG
        result[5] = Math.max(A[5], B[5]);   //maxB
        result[6] = A[6] + B[6]; //Union  Setsize
        return result;        
    }
    
    /** Method to do min(Diff(a), diff(b))
     * 
     * @param a
     * @param b
     * @return
     */
    private static int[] minArray(int[] a, int[] b) {
        int[] result = new int[3];
        result[0] = Math.min(a[0], b[0]);   //r
        result[1] = Math.min(a[1], b[1]);   //g
        result[2] = Math.min(a[2], b[2]);   //b
        return result;
    }
    
    /** Internal PixelDistance class implements Distance interface
     *  distance is taken to be the square difference of the four bytes
     *  of the Pixel value integer. Since it considers all four bits
     *  it's a bit more general then a distance calculation for RGB only.
     */
    private static class PixelDistance implements Distance<Pixel> {
    	public PixelDistance() {
    	}

    	/* Gives the (distance)^2.
    	 * (non-Javadoc)
    	 * @see Distance#distance(java.lang.Object, java.lang.Object)
    	 */    	
    	public double distance(Pixel one, Pixel two) {
			int b11 = one.value() & 0xFF;
			int b12 = (one.value() >> 8) & 0xFF;
			int b13 = (one.value() >> 16) & 0xFF;
			int b14 = (one.value() >> 24) & 0xFF; 

			int b21 = two.value() & 0xFF;
			int b22 = (two.value() >> 8) & 0xFF;
			int b23 = (two.value() >> 16) & 0xFF;
			int b24 = (two.value() >> 24) & 0xFF;

			int diff = (b11 - b21) * (b11 - b21)
						+ (b12 - b22) * (b12 - b22)
						+ (b13 - b23) * (b13 - b23)
						+ (b14 - b24) * (b14 - b24);

			return diff;
    	}
    }

    public static void main(String[] args) {
       
        final int gray = 0x202020;
        
        try {
          // the line that reads the image file

            BufferedImage image = ImageIO.read(new File(args[0]));
            WGraphP4<Pixel> g = (WGraphP4) imageToGraph(image, new PixelDistance());
            System.out.println("Image loaded, now segmenting image...");
            List<WEdge<Pixel>> res = segmenter(g, Double.parseDouble(args[1]));

            System.out.print("result =  " + res.size() + "\n");
            System.out.print("NSegments =  "
                             + (g.numVerts() - res.size()) + "\n");

            // Clear out the edges in g
            // add back all the edges in res
            // we found this faster than trying to find which ones to remove
            g.clearEdges();

            for (WEdge<Pixel> edge : res) {
                g.addEdge(edge);
            }

            System.out.println("c");

            // We need to account for every vertex
            List<GVertex<Pixel>> vertices = g.allVertices();

            // Since the graph is now a minimally spanning forest doing
            // a depth first search on one vertex will uncover all vertices
            // in a given segment.
            HashSet<GVertex<Pixel>> visited = new HashSet<GVertex<Pixel>>();
            int numSegments = 0;
            
            for (GVertex<Pixel> vertex : vertices) {
              if (visited.contains(vertex)) {
                // we already added the sub graph this vertex belongs to
                continue;
              }

              System.out.println("d");
              List<GVertex<Pixel>> segment = g.depthFirst(vertex);
              System.out.println("e");
              visited.addAll(segment);
              System.out.println("f");
              numSegments++;

              // make a background image to put a segment into
              for (int i = 0; i < image.getHeight(); i++) {
                  for (int j = 0; j < image.getWidth(); j++) {
                      image.setRGB(j, i, gray);
                  }
              }

              // put the segment in the image
              for (GVertex<Pixel> i: segment)  {
                  Pixel d = i.data();
                  image.setRGB(d.col(), d.row(), d.value());
              }

              // save file
              String savename = "output" + numSegments + ".png";
              File f = new File(savename);
              ImageIO.write(image, "png", f);             

            }


        } catch (IOException e) {
            System.out.print("Missing File!\n");

            // log the exception
            // re-throw if desired
        }
    }

}