package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.RGBAPixel;

/**
 * Changes amount of red in image dependent on factor
 * @author Tom
 *
 */
public class SetGreen implements FilterInterface {
private final double factor;
	
	/**
	 * Constructor for this filter
	 * @param percent
	 * 			percentage by which to increase/decrease amount of blue in image
	 */
	public SetGreen(int percent) {
		factor = percent / 100.0;
	}
	
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				RGBAPixel rgbPix = new RGBAPixel(img.getRGB(x, y));
				rgbPix.setG((int)(rgbPix.getB()*factor));
				newImg.setRGB(x, y, rgbPix.getRawRGBA());
			}
		}
		return newImg;
	}
	
	@Override
	public String toString() {
		return "Set Green";
	}

}
