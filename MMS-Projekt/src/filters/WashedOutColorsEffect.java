package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.HSVPixel;
import pixels.RGBAPixel;

/**
 * Creates the effect of an image having "washed out colors"
 * Works well on panoramas
 * Doesn't look too good on images of faces
 * @author Tom
 *
 */
public class WashedOutColorsEffect implements FilterInterface {

	private static final double FACTOR = 0.6;
	
	/**
	 * Transforms rgb value of pixel into HSV color space. 
	 * Then changes the Saturation to make the image look 
	 * like it has less lively colors.
	 */
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage washedOut = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				int rgb = img.getRGB(x, y);
				HSVPixel hsvPix = new HSVPixel(rgb);
				hsvPix.setS(hsvPix.getS() * FACTOR);
				RGBAPixel rgbPix = new RGBAPixel(hsvPix);
				washedOut.setRGB(x, y, rgbPix.getRawRGBA());
			}
		}
		return washedOut;
	}
	
	@Override 
	public String toString() {
		return "Washed Out Effect";
	}
}
