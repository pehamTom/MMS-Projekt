package pixels;

/**
 * Pixel in YCbCr Color Space
 * @author Tom
 *
 */
public class YCbCrPixel {

	private int y, cb, cr;
	
	/**
	 * Transforms red green and blue values into YCbCr Color space
	 * @param r
	 * @param g
	 * @param b
	 */
	public YCbCrPixel(int r, int g, int b) {
		y = byteRange((int) (0.299*r + 0.587*g + 0.114*b));
		cb = byteRange((int) (128 - 0.168736*r - 0.331264*g + 0.5*b));
		cr = byteRange((int) (128 + 0.5*r - 0.418688*g - 0.081312*b));
	}
	
	/**
	 * Creates YCbCr Pixel from RGBA Pixel
	 * @param rgbPix
	 */
	public YCbCrPixel(RGBAPixel rgbPix) {
		this(rgbPix.getR(), rgbPix.getG(), rgbPix.getB());
	}
	
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCb() {
		return cb;
	}

	public void setCb(int cb) {
		this.cb = cb;
	}

	public int getCr() {
		return cr;
	}

	public void setCr(int cr) {
		this.cr = cr;
	}

	/**
	 * Puts integer in range from 0-255
	 * @param input
	 * @return
	 */
	private int byteRange(int input) {
		return range(input, 0, 255);
	}
	
	/**
	 * Puts integers in range
	 * 
	 * @param input
	 * @param lower
	 * @param upper
	 * @return
	 */
	private int range(int input, int lower, int upper) {
		if(input > upper) {
			return upper;
		}else if(input < lower) {
			return lower;
		}
		return input;
	}
}
