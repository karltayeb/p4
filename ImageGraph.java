import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class ImageGraph {

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

    WGraphP4<Pixel> image = makeGraph(load);
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

private static WGraphP4<Pixel> makeGraph(BufferedImage buffImage) {
	int height = buffImage.getHeight();
	int width = buffImage.getWidth();

	// Populate the graph with vertices
	// reads left to right, top down across image
	// the ID of pixel (i,j) is (j*width)+i
	WGraphP4<Pixel> image = new WGraphP4();
	for (int i = 0; i < height; i++) {
		for (int j = 0; j < width; j++) {
			Pixel pix = new Pixel(i, j, buffImage.getRGB(j, i));
			image.addVertex(pix);
		}
	}

	return image;
}

}
