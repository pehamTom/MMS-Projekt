package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.RGBAPixel;

/**
 * Filter out blue parts of image
 * @author Tom
 *
 */
public class FilterBlue implements FilterInterface {

	/**
	 * Filter out blue parts of image
	 */
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage greenImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				greenImg.setRGB(x, y, RGBAPixel.generateRGBAPixel(RGBAPixel.getRed(img.getRGB(x, y)), RGBAPixel.getGreen(img.getRGB(x, y)), 0, 255));
			}
		}
		return greenImg;
	}
	
	@Override
	public String toString() {
		return "Filter Blue";
	}

}
