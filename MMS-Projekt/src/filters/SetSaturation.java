package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.HSVPixel;
import pixels.RGBAPixel;

/**
 * Set Saturation of image
 * @author Tom
 *
 */
public class SetSaturation implements FilterInterface {

	private final double factor;
	public SetSaturation(int factor) {
		this.factor = factor/100.0;
	}
	
	/**
	 * Transforms rgb value of pixel into HSV color space. 
	 * Then multiplies saturation with factor
	 * Then creates new image out of calculated rgb values
	 */
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage washedOut = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				int rgb = img.getRGB(x, y);
				HSVPixel hsvPix = new HSVPixel(rgb);
				hsvPix.setS(hsvPix.getS() * factor);
				RGBAPixel rgbPix = new RGBAPixel(hsvPix);
				washedOut.setRGB(x, y, rgbPix.getRawRGBA());
			}
		}
		return washedOut;
	}
	
	@Override 
	public String toString() {
		return "Set Saturation";
	}
}
