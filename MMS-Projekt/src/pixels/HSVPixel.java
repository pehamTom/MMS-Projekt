package pixels;

/**
 * A pixel in HSV color space
 * @author Tom
 *
 */
public class HSVPixel {

	private double h, s, v;
	
	/**
	 * Generate Pixel in the HSV Color Space from RGB Value. 
	 * Works with flaoting point numbers, which is more precise
	 *  of course slower. But the author thinks that the benefits 
	 *  outweigh the costs.
	 * @param red
	 * @param green
	 * @param blue
	 */
	public HSVPixel(int red, int green, int blue) {
		double min, max, delta;
		double r,g,b;
		
		r = mapToInterval(red);
		g = mapToInterval(green);
		b = mapToInterval(blue);
		
		min = r < g ? r : g;
		min = min < b ? min : b;
		
		max = r > g ? r : g;
		max = max > b ? max : b;
		
		v = max;
		delta = max - min;
		if(delta < 0.00001) {
			s = 0;
			h = 0;
			return;
		}
		
		if(max > 0.0) {
			s = delta / max;
		} else {
			s = 0.0;
			h = -1; //undefined
			return;
		}
		if(r >= max) {
			h = (g-b)/delta;
		} else if(g >= max) {
			h = 2.0 + (b-r)/delta;
		} else {
			h = 4.0+ (r-g)/delta;
		}
		h *= 60;
		
		if(h < 0) {
			h += 360;
		}
	}
	
	public HSVPixel(int rgba) {
		this(RGBAPixel.getRed(rgba),RGBAPixel.getGreen(rgba),RGBAPixel.getBlue(rgba));
	}
	
	/**
	 * maps rgb value to interval [0,1]
	 * @param val
	 * 		red, green or blue value to map
	 * @return
	 * 		mapped value
	 */
	private double mapToInterval(int val) {
		return val / 255.0;
	}

	/**
	 * Get hue
	 * @return
	 * 		this pixels hue
	 */
	public double getH() {
		return h;
	}

	/**
	 * Get saturation
	 * @return
	 * 		this pixels saturation
	 */
	public double getS() {
		return s;
	}
	
	/**
	 * Get Value
	 * @return
	 * 		this pixels value
	 */
	public double getV() {
		return v;
	}

	/**
	 * Set this pixels saturation
	 * @param s
	 * 		saturation to be set
	 */
	public void setS(double s) {
		this.s = s > 1.0 ? 1.0 : s;
	}

	public void setH(double h) {
		this.h = h;
	}

	public void setV(double v) {
		this.v = v > 1.0 ? 1.0 : v;
	}
}
