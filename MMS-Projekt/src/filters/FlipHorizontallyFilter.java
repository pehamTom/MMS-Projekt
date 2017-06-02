package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Flips Image Horizontally
 * @author Tom
 *
 */
public class FlipHorizontallyFilter implements FilterInterface {
	
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage flippedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				flippedImg.setRGB(x, y, img.getRGB(x, img.getHeight()-y-1));
			}
		}
		return flippedImg;
	}

	@Override
	public String toString() {
		return "Flip Horizontally";
	}
}
