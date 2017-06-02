package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.RGBAPixel;

/**
 * Filter out blue and green parts of image
 * @author Tom
 *
 */
public class ExtractRedFilter implements FilterInterface {

	/**
	 * Filter out blue and green parts of image
	 */
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage redImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				redImg.setRGB(x, y, RGBAPixel.generateRGBAPixel(RGBAPixel.getRed(img.getRGB(x, y)), 0, 0, 255));
			}
		}
		return redImg;
	}
	
	@Override
	public String toString() {
		return "Extract Red";
	}

}
