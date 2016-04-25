public class Pixel {
	private int row;
	private int col;
	private int data;

	public Pixel() {
		this.row = 0;
		this.col = 0;
		this.data = 0;
	}
	public Pixel(int r, int c, int d) {
		this.row = r;
		this.col = c;
		this.data = d;
	}

	public int r() {
		return (this.data >> 16) & 0xFF;
	}
	public int g() {
		return (this.data >> 8) & 0xFF;
	}
	public int b() {
		return this.data & 0xFF;
	}

	public int data() {
		return this.data;
	}

}