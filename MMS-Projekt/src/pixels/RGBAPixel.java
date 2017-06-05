package pixels;

import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;

public class RGBAPixel {
	
	private int rawRGBA;
	
	private int alpha;
	private int r, g, b;
	
	
	/**
	 * Initialize with a raw RGB value
	 * @param rawRBGA
	 */
	public RGBAPixel(int rawRGBA) {
		setRawRGBA(rawRGBA);
	}
	
	/**
	 * Initialize with R G B and Alpha values
	 * @param r
	 * @param g
	 * @param b
	 * @param alpha
	 */
	public RGBAPixel(int r, int g, int b, int alpha) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
		updateRawFromRGB();
	}

	/**
	 * Initialize with HSV Pixel
	 * @param hsv
	 * 		Pixel in HSV Colorspace
	 * 
	 */
	public RGBAPixel(HSVPixel hsv) {
		double h = hsv.getH();
		double s = hsv.getS();
		double v = hsv.getV();
		
		h /= 60.0;
		int i = (int) h;
		double f = h - i;
		double p = v * (1.0 - s);
		double q = v *(1.0 - (s*f));
		double t = v *(1.0 - (s * (1.0-f)));
		
		int intP = mapToRGBRange(p);
		int intQ = mapToRGBRange(q);
		int intV = mapToRGBRange(v);
		int intT = mapToRGBRange(t);
		
		switch(i) {
		case 0: {
			r = intV;
			g = intT;
			b = intP;
		} break;
		
		case 1: {
			r = intQ;
			g = intV;
			b = intP;
		} break;
		
		case 2: {
			r = intP;
			g = intV;
			b = intT;
		} break;
		
		case 3: {
			r = intP;
			g = intQ;
			b = intV;
		} break;
		
		case 4: {
			r = intT;
			g = intP;
			b = intV;
		} break;
		
		default: {
			r = intV;
			g = intP;
			b = intQ;
		} break;
		}
		this.alpha = 255;
		updateRawFromRGB();
	}
	
	/**
	 * Initialize with YCbCr Pixel
	 * @param ycbcrPix
	 * 			Pixel in YCbCr Color Space
	 */
	public RGBAPixel(YCbCrPixel ycbcrPix) {
		int y = ycbcrPix.getY();
		int cb = ycbcrPix.getCb();
		int cr = ycbcrPix.getCr();
		r = byteRange((int) (y + 1.402*(cr-128)));
		g = byteRange((int) (y - 0.344136*(cb-128) - 0.714136*(cr-128)));
		b = byteRange((int) (y + 1.772*(cb-128)));
		alpha = 255;
	}
	

	/** Extract raw pixels red value */
	public static int getRed(int rgba) {
		return 0xFF & (rgba >> 16);	//mask out red part of rgba
	}

	/** Extract raw pixels green value */
	public static int getGreen(int rgba) {
		return 0xFF & (rgba >> 8); 	//mask out green part of rgba
	}

	/** Extract raw pixels blue value */
	public static int getBlue(int rgba) {
		return 0xFF & rgba;	//mask out blue part of rgba
	}

	/** Extract raw pixels alpha channel */
	public static int getAlpha(int rgba) {
		return 0xFF & (rgba >> 24);	//mask out alpha of rgba
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
	
	/** Create single rgb values from a raw int */
	private void updateRGBFromInt() {
		r = RGBAPixel.getRed(rawRGBA);
		g = RGBAPixel.getGreen(rawRGBA);
		b = RGBAPixel.getBlue(rawRGBA);
		alpha = RGBAPixel.getAlpha(rawRGBA);
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
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
		updateRawFromRGB();
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = byteRange(r);
		updateRawFromRGB();
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = byteRange(g);
		updateRawFromRGB();
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = byteRange(b);
		updateRawFromRGB();
	}
	private int mapToRGBRange(double val) {
		return (int)(val*255);
	}
}
