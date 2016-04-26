import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;
import java.util.ArrayList;


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
		return (j * width) + i;
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

    /** Return a list of edges in a minimum spanning forest by
     *  implementing Kruskal's algorithm using fast union/finds.
     *  @param g the graph to segment
     *  @param kvalue the value to use for k in the merge test
     *  @return a list of the edges in the minimum spanning forest
     */

    static List<WEdge<Pixel>> segmenter(WGraph<Pixel> g, double kvalue) {
    	return null;
    }

    /** Internal PixelDistance class implements Distance interface
     *  distance is taken to be the square difference of the four bytes
     *  of the Pixel value integer. Since it considers all four bits
     *  it's a bit more general then a distance calculation for RGB only.
     */
    private static class PixelDistance implements Distance<Pixel> {
    	public PixelDistance() {
    	}

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

            // Remove all edges not in the minimal spanning forrest
            for (WEdge<Pixel> edge : g.allEdges()) {
            	if (!res.contains(edge)) {
            		g.deleteEdge(edge.source(), edge.end());
            	}
            }

            // We need to account for every vertex
            List<GVertex<Pixel>> vertices = g.allVertices();

            // Since the graph is now a minimally spanning forest doing
            // a depth first search on one vertex will uncover all vertices
            // in a given segment.
            List<GVertex<Pixel>> visited = new ArrayList<GVertex<Pixel>>();
            int numSegments = 0;
            
            for (GVertex<Pixel> vertex : vertices) {
            	if (visited.contains(vertex)) {
            		// we already added the sub graph this vertex belongs to
            		continue;
            	}

            	List<GVertex<Pixel>> segment = g.depthFirst(vertex);
            	visited.addAll(segment);
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
