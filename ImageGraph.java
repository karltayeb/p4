import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class ImageGraph {

	private ArrayList<GVertex<Pixel>> verts;
	private int height;
	private int width;

	public ImageGraph() {
	}

	public static void main(String[] args) {
		// Load the image
		BufferedImage load = null;
	    try {
	        load = ImageIO.read(new File(args[0]));

	        System.out.println(load.getHeight());
	        System.out.println(load.getWidth());

	    } catch (IOException e) {
	        System.out.print("Missing File!\n");
	        return;
	    }

	    WGraphP4<Pixel> image = createGraph(load);
	    System.out.println(image.numVerts());


	}

	private static double distance(Pixel one, Pixel two) {
		int b11 = one.data() & 0xFF;
		int b12 = (one.data() >> 8) & 0xFF;
		int b13 = (one.data() >> 16) & 0xFF;
		int b14 = (one.data() >> 24) & 0xFF; 

		int b21 = two.data() & 0xFF;
		int b22 = (two.data() >> 8) & 0xFF;
		int b23 = (two.data() >> 16) & 0xFF;
		int b24 = (two.data() >> 24) & 0xFF;

		int diff = (b11 - b21) * (b11 - b21)
					+ (b12 - b22) * (b12 - b22)
					+ (b13 - b23) * (b13 - b23)
					+ (b14 - b24) * (b14 - b24);

		return diff;
	}

	private static WGraphP4<Pixel> createGraph(BufferedImage image) {

		// Populate the graph with vertices
		// reads left to right, top down across image
		// the ID of pixel (i,j) is (j*width)+i
		WGraphP4<Pixel> imageGraph = new WGraphP4();
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				Pixel pix = new Pixel(i, j, image.getRGB(j, i)); // Is this right? It looks like images go col, row
				imageGraph.addVertex(pix);
			}
		}

		List<GVertex<Pixel>> verts = imageGraph.allVertices();

		// Assign edge weights
		for (int i = 0; i < ig.height; i++) {
			for (int j = 0; j < ig.width; j++) {
				putEdges(imageGraph, verts, i, j);
			}
		}

		return image;
	}

	/**
	 * Helper method that creates the edges in a new image
	 * checks for boundary cases like edges and corners
	 * @param i the row of the current pixel
	 * @param j the col of the the current pixel
	 */
	private static void putEdges(WGraphP4<Pixel> graphImage, List<GVertex<Pixel>> verts, int i, int j) {
		int index = index(i, j);
		int upIndex = index(i - 1, j);
		int downIndex = index(i + 1, j);
		int leftIndex = index(i, j - 1);
		int rightIndex = index(i, j + 1);

		GVertex<Pixel> v1 = this.verts.get(index);
		GVertex<Pixel> v2;

		if (upIndex > 0) {
			v2 = this.verts.get(upIndex);
			graphImage.addEdge(v1, v2, distance(v1, v2));
		}

		if (downIndex < this.verts.size()) {
			v2 = this.verts.get(downIndex);
			graphImage.addEdge(v1, v2, distance(v1, v2));
		}

		if (j > 0) {
			v2 = this.verts.get(leftIndex);
			graphImage.addEdge(v1, v2, distance(v1, v2));
		}
		if (j < this.width) {
			v2 = this.verts.get(rightIndex);
			graphImage.addEdge(v1, v2, distance(v1, v2));
		}

	}

	/**
	 * Helper method gives the flat array index of pixel
	 * @param i the row of the current pixel
	 * @param j the col of the the current pixel
	 * @param width the width of the image
	 */
	private int index (int i, int j, int width) {
		return (j * width) + i;
	}

}
