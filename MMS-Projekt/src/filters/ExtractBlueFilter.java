package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import gui.FilterInterface;
import pixels.Pixel;

/**
 * Filters out red and green from the image
 * @author Tom
 *
 */
public class ExtractBlueFilter implements FilterInterface{

	/**
	 * Filter out red and green parts of image
	 */
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage blueImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				blueImg.setRGB(x, y, Pixel.generateRGBAPixel(0, 0, Pixel.getBlue(img.getRGB(x, y)), 255));
			}
		}
		return blueImg;
	}
	
	@Override 
	public String toString() {
		return "Extract Blue";
	}

}
