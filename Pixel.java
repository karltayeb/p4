/** Pixel Class. */
public class Pixel {
    /** Row number. */
    private int row;
    /** Column number. */
    private int col;
    /** Data in pixel. */
    private int data;

    /** Pixel constructor. */
    public Pixel() {
        this.row = 0;
        this.col = 0;
        this.data = 0;
    }
    /** Pixel constructor. 
 *      @param r r.
 *      @param c c.
 *      @param d d. */
    public Pixel(int r, int c, int d) {
        this.row = r;
        this.col = c;
        this.data = d;
    }

    /** Red. 
 *      @return int. */
    public int r() {
        return (this.data >> 16) & 0xFF;
    }
    /** Green. 
 *      @return int. */
    public int g() {
        return (this.data >> 8) & 0xFF;
    }
    /** Blue. 
 *      @return int. */
    public int b() {
        return this.data & 0xFF;
    }

    /** Return row number. 
 *      @return int. */
    public int row() {
        return this.row;
    }
    /** Return column number. 
 *      @return int. */
    public int col() {
        return this.col;
    }
    /** Return data in pixel. 
 *      @return int. */
    public int value() {
        return this.data;
    }
}
