import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;


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
		int upIndex = index(i - 1, j, width);
		int downIndex = index(i + 1, j, width);
		int leftIndex = index(i, j - 1, width);
		int rightIndex = index(i, j + 1, width);

		GVertex<Pixel> v1 = verts.get(index);
		GVertex<Pixel> v2;

		// check bounds
		if (i < 0 || i >= height || j < 0 || j >= width) {
			// if any of these are true we are out of bounds
			return;
		}

		if (i > 0) {
			// add up index if we're not on the first row
			v2 = verts.get(upIndex);
			graphImage.addEdge(v1, v2, pd.distance(v1.data(), v2.data()));
		}

		if (i > (height - 1)) {
			// add the down index if we're not on the last row
			v2 = verts.get(downIndex);
			graphImage.addEdge(v1, v2, pd.distance(v1.data(), v2.data()));
		}

		if (j > 0) {
			// add the left index if we're not no the leftmost column
			v2 = verts.get(leftIndex);
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
        Partition roots = new Partition(g.allVertices().size());
        //ArrayList<ArrayList<WEdge<Pixel>>> MSTGroup = new ArrayList<ArrayList<WEdge<Pixel>>>();
        List<WEdge<Pixel>> MST = new ArrayList<WEdge<Pixel>>();
        List<WEdge<Pixel>> edges = g.allEdges();

        //set up MSTGroup: to have n arrayLists, where 
        //heap contains all the edges of the graph
        PQHeap<WEdge<Pixel>> heap = new PQHeap<WEdge<Pixel>>(new ReverseComparator<WEdge<Pixel>>());
        for (int i = 0; i < edges.size(); i++) {
            //add all edges into min-heap
            heap.insert(edges.get(i));
        }
     
        // process all the edges IN ORDER OF WEIGHT    
        while (!heap.isEmpty()) {
            WEdge<Pixel> current = heap.peek();
            int root1 = roots.find(current.source().id());   
            int root2 = roots.find(current.end().id());     
            
            //if the two roots are NOT equal, then union and add to MST
            //otherwise, do nothing
            if(root1 != root2 && partialUnion(g, roots, root1, root2, kvalue)){
                MST.add(current);
                roots.union(root1, root2);
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
     * @return true if partial union condition is satisfied
     */
    private static boolean partialUnion(WGraph<Pixel> g, Partition p, int root1, int root2, double k){
        //A, B are Lists of the Pixels in each Disjoint Set
        ArrayList<Pixel> A = new ArrayList<Pixel>();
        ArrayList<Pixel> B = new ArrayList<Pixel>();
        for (int i = 0; i < p.getSize(); i++){
            if (p.find(i) == root1) {
               A.add(g.allVertices().get(i).data()); 
            } else if (p.find(i) == root2) {
                A.add(g.allVertices().get(i).data()); 
            }
        }
        int[] diffA = diff(A);
        int[] diffB = diff(B);
        int[] minDiffAB = minArray(diffA, diffB);
        ArrayList<Pixel> AUB = new ArrayList<Pixel>();
        AUB.addAll(A);
        AUB.addAll(B);
        int[] diffAUB = diff(AUB);
        
        //checking the condition
        boolean result = true;
        for (int i = 0; i < 3; i++){
            if(diffAUB[i] > (minDiffAB[i] + k / (A.size() + B.size())) ) {
                //failed the required condition
                result = false;
            }
        }
        return result;
    }

    /** Finds the RGB Difference Tuple for an ArrayList of Pixels
     *  aka. the Set
     */
    private static int[] diff(ArrayList<Pixel> data) {
        int minR = data.get(0).r();
        int minG = data.get(0).g();
        int minB = data.get(0).b();
        int maxR = data.get(0).r();
        int maxG = data.get(0).g();
        int maxB = data.get(0).b();
        for (int i = 1; i < data.size(); i++){
            if (data.get(i).r() < minR) {
                minR = data.get(i).r();
            } else if (data.get(i).r() > maxR) {
                maxR = data.get(i).r();
            }
            
            if (data.get(i).g() < minG) {
                minG = data.get(i).g();
            } else if (data.get(i).g() > maxG) {
                maxG = data.get(i).g();
            }
            
            if (data.get(i).b() < minB) {
                minB = data.get(i).b();
            } else if (data.get(i).b() > maxB) {
                maxB = data.get(i).b();
            }
        }
        
        int[] result = {maxR - minR, maxG - minG, maxB - minB};
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
        result[0] = Math.min(a[0], b[0]);
        result[1] = Math.min(a[1], b[1]);
        result[2] = Math.min(a[2], b[2]);
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
            WGraph<Pixel> g = imageToGraph(image, new PixelDistance());
            List<WEdge<Pixel>> res = segmenter(g, Double.parseDouble(args[1]));

            System.out.print("result =  " + res.size() + "\n");
            System.out.print("NSegments =  "
                             + (g.numVerts() - res.size()) + "\n");

            // make a background image to put a segment into
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    image.setRGB(j, i, gray);
                }
            }

            /**
            // After you have a spanning tree connected component x, 
            // you can generate an output image like this:
            for (GVertex<Pixel> i: x)  {
                Pixel d = i.data();
                image.setRGB(d.col(), d.row(), d.value());
            }

            File f = new File("output.png");
            ImageIO.write(image, "png", f);

            // You'll need to do that for each connected component,
            // writing each one to a different file, clearing the
            // image buffer first
			*/
        } catch (IOException e) {
            System.out.print("Missing File!\n");

            // log the exception
            // re-throw if desired
        }
    }

}
