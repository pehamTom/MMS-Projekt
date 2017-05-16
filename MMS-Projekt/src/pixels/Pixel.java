package pixels;

import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;

/** Represent a single Pixel */
public class Pixel {
	
	private int rawRGBA;
	
	private int alpha;
	private int r, g, b;
	
	private int y, cb, cr;
	
	/**
	 * Initialize with a raw RGB value
	 * @param rawRBGA
	 */
	public Pixel(int rawRGBA) {
		setRawRGBA(rawRGBA);
	}
	
	/**
	 * Initialize with R G B and Alpha values
	 * @param r
	 * @param g
	 * @param b
	 * @param alpha
	 */
	public Pixel(int r, int g, int b, int alpha) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
		updateRawFromRGB();
		updateYCbCrFromRGB();
	}
	
	/**
	 * Initialize with YCbCr Model 
	 * @param y
	 * @param cb
	 * @param cr
	 */
	public Pixel(int y, int cb, int cr) {
		this.y = y;
		this.cb = cb;
		this.cr = cr;
		updateRGBFromYCbCr();
		updateRawFromRGB();
	}

	/** One color model per JVM is enough */
	private static DirectColorModel colorModel = null;
	
	/** Inline init of color model */
	private static DirectColorModel getColorModel() {
		if (colorModel == null) {
			colorModel = (DirectColorModel) ColorModel.getRGBdefault();
		}
		return colorModel;
	}

	/** Extract raw pixels red value */
	public static int getRed(int rgba) {
		return getColorModel().getRed(rgba);
	}

	/** Extract raw pixels green value */
	public static int getGreen(int rgba) {
		return getColorModel().getGreen(rgba);
	}

	/** Extract raw pixels blue value */
	public static int getBlue(int rgba) {
		return getColorModel().getBlue(rgba);
	}

	/** Extract raw pixels alpha channel */
	public static int getAlpha(int rgba) {
		return getColorModel().getAlpha(rgba);
	}
	
	/** Create a raw pixel from rgba Values */
	public static int generateRGBAPixel(int r, int g, int b, int a) {
		a = a << 24;
		r = r << 16;
		g = g << 8;
		return a | r | g | b;
	}
	
	/** Create a raw Integer value from single rgb a values */
	private void updateRawFromRGB() {
		rawRGBA = generateRGBAPixel(r, g, b, alpha);
	}
	
	/** Create single rgb values form a raw int */
	private void updateRGBFromInt() {
		r = Pixel.getRed(rawRGBA);
		g = Pixel.getGreen(rawRGBA);
		b = Pixel.getBlue(rawRGBA);
		alpha = Pixel.getAlpha(rawRGBA);
	}
	
	/** Generate YCbCr values from rgb
	 * @see http://en.wikipedia.org/wiki/YCbCr and lecture slides */
	//using the jpeg-standard conversion from the wikipedia article
	private void updateYCbCrFromRGB() {
		y = byteRange((int) (0.299*r + 0.587*g + 0.114*b));
		cb = byteRange((int) (128 - 0.168736*r - 0.331264*g + 0.5*b));
		cr = byteRange((int) (128 + 0.5*r - 0.418688*g - 0.081312*b));
	}
	
	/** Generate RGB from YCbCr values
	 * @see http://en.wikipedia.org/wiki/YCbCr and lecture slides */
	//using the jpeg-standard conversion from the wikipedia article
	private void updateRGBFromYCbCr() {
		r = byteRange((int) (y + 1.402*(cr-128)));
		g = byteRange((int) (y - 0.344136*(cb-128) - 0.714136*(cr-128)));
		b = byteRange((int) (y + 1.772*(cb-128)));
		alpha = 255;
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
	
	private int byteRange(int input) {
		return range(input, 0, 255);
	}
	
	// GETTERS / SETTERS
	
	public int getRawRGBA() {
		return rawRGBA;
	}

	public void setRawRGBA(int rawRGBA) {
		this.rawRGBA = rawRGBA;
		updateRGBFromInt();
		updateYCbCrFromRGB();
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
		updateRawFromRGB();
		updateYCbCrFromRGB();
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
		updateRawFromRGB();
		updateYCbCrFromRGB();
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
		updateRawFromRGB();
		updateYCbCrFromRGB();
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
		updateRawFromRGB();
		updateYCbCrFromRGB();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		updateRGBFromYCbCr();
		updateRawFromRGB();
	}

	public int getCb() {
		return cb;
	}

	public void setCb(int cb) {
		this.cb = cb;
		updateRGBFromYCbCr();
		updateRawFromRGB();
	}

	public int getCr() {
		return cr;
	}

	public void setCr(int cr) {
		this.cr = cr;
		updateRGBFromYCbCr();
		updateRawFromRGB();
	}

	public static void setColorModel(DirectColorModel colorModel) {
		Pixel.colorModel = colorModel;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("r ");
		buffer.append(r);
		buffer.append(" g ");
		buffer.append(g);
		buffer.append(" b ");
		buffer.append(b);
		buffer.append(" a ");
		buffer.append(alpha);
		buffer.append(" y ");
		buffer.append(y);
		buffer.append(" cb ");
		buffer.append(cb);
		buffer.append(" cr ");
		buffer.append(cr);
		
		return buffer.toString();
	}

}
